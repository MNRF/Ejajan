package com.mnrf.ejajan.view.main.student.face

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import com.mnrf.ejajan.data.model.CartModel
import com.mnrf.ejajan.data.model.UserModel
import com.mnrf.ejajan.data.pref.CartPreferences
import com.mnrf.ejajan.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.time.Instant
import java.util.UUID

class FaceConfirmViewModel(private val repository: UserRepository, private val cartPreferences: CartPreferences) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _orderResponse = MutableLiveData<Boolean>()
    val orderResponse: LiveData<Boolean> = _orderResponse

    private var ordersId = ""

    private val db = Firebase.firestore
    private val storage = Firebase.storage

    fun getSession(): LiveData<UserModel> {
        _isLoading.value = true
        return repository.getSession().asLiveData().also {
            _isLoading.value = false
        }
    }

    fun fetchUidsAndCreateOrders(
        totalOrders: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.collection("studentprofiles")
            .get()
            .addOnSuccessListener { studentSnapshot ->
                val studentUid = studentSnapshot.documents.firstOrNull()?.getString("uid") ?: ""

                db.collection("merchantprofiles")
                    .get()
                    .addOnSuccessListener { merchantSnapshot ->
                        val merchantUid = merchantSnapshot.documents.firstOrNull()?.getString("uid") ?: ""

                        db.collection("parentprofiles")
                            .get()
                            .addOnSuccessListener { parentSnapshot ->
                                val parentUid = parentSnapshot.documents.firstOrNull()?.getString("uid") ?: ""

                                val newTransactionRef = db.collection("transactions").document()
                                val transactionId = newTransactionRef.id
                                onSuccess()

                                orders(
                                    merchantUid = merchantUid,
                                    studentUid = studentUid,
                                    parentUid = parentUid,
                                    totalOrders = totalOrders,
                                    transactionId = transactionId,
                                )
                            }
                            .addOnFailureListener { e ->
                                onFailure("Failed to fetch parent UID: ${e.message}")
                            }
                    }
                    .addOnFailureListener { e ->
                        onFailure("Failed to fetch merchant UID: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                onFailure("Failed to fetch student UID: ${e.message}")
            }
    }

    private fun orders(
        merchantUid: String,
        studentUid: String,
        parentUid: String,
        totalOrders: String,
        transactionId: String,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val isSuccess = createOrder(
                    merchantUid,
                    studentUid,
                    parentUid,
                    totalOrders,
                    transactionId
                )
                _orderResponse.postValue(isSuccess)
            } catch (e: Exception) {
                _orderResponse.postValue(false)
            }
        }
    }

    private suspend fun createOrder(
        merchantUid: String,
        studentUid: String,
        parentUid: String,
        totalOrders: String,
        transactionId: String,
    ): Boolean {
        return try {
            val orders = mapOf(
                "order_id" to UUID.randomUUID().toString(),
                "merchant_uid" to merchantUid,
                "student_uid" to studentUid,
                "parent_uid" to parentUid,
                "total_orders" to totalOrders,
                "transaction_id" to transactionId,
                "date_created" to Instant.now().toEpochMilli().toString()
            )
            db.collection("orders").add(orders).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun orderCreate(onFailure: (String) -> Unit) {
        val cartItems = cartPreferences.getCartItems()

        if (cartItems.isNotEmpty()) {
            fetchUidsAndCreateOrder(cartItems, onFailure)
        } else {
            onFailure("Cart is empty")
        }
    }

    private fun fetchUidsAndCreateOrder(
        cartItems: List<CartModel>,
        onFailure: (String) -> Unit
    ) {
        db.collection("studentprofiles")
            .get()
            .addOnSuccessListener { studentSnapshot ->
                val studentUid = studentSnapshot.documents.firstOrNull()?.getString("uid") ?: ""

                db.collection("merchantprofiles")
                    .get()
                    .addOnSuccessListener { merchantSnapshot ->
                        val merchantUid = merchantSnapshot.documents.firstOrNull()?.getString("uid") ?: ""

                        // Create a new orderId from Firestore (to represent the order)
                        val newOrderRef = db.collection("orders").document()
                        val orderId = newOrderRef.id

                        // Set the status of the order
                        val orderStatus = "Pending"

                        // Prepare each menu item for the order
                        for (cartItem in cartItems) {
                            val discountedPrice = cartItem.discountedPrice // Ambil harga diskon dari `cartItem`

                            orderAdd(
                                menuUid = cartItem.id,
                                merchantUid = merchantUid,
                                studentUid = studentUid,
                                ordersUid = orderId,
                                orderStatus = orderStatus,
                                menuName = cartItem.name,
                                menuPrice = cartItem.price,
                                menuQty = cartItem.quantity,
                                menuImageUrl = cartItem.imageurl,
                                menuDiscountedPrice = discountedPrice
                            )
                        }
                    }
                    .addOnFailureListener { e ->
                        onFailure("Failed to fetch merchant UID: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                onFailure("Failed to fetch student UID: ${e.message}")
            }
    }

    private fun orderAdd(
        menuUid: String,
        merchantUid: String,
        studentUid: String,
        ordersUid: String,
        orderStatus: String,
        menuName: String,
        menuPrice: String,
        menuQty: String,
        menuImageUrl: String,
        menuDiscountedPrice: String?
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val isSuccess = orderCreate(
                    menuUid,
                    merchantUid,
                    studentUid,
                    ordersUid,
                    orderStatus,
                    menuName,
                    menuPrice,
                    menuQty,
                    menuImageUrl,
                    menuDiscountedPrice // Pass harga diskon
                )
                _orderResponse.postValue(isSuccess)
            } catch (e: Exception) {
                _orderResponse.postValue(false)
            }
        }
    }

    private suspend fun orderCreate(
        menuUid: String,
        merchantUid: String,
        studentUid: String,
        ordersUid: String,
        orderStatus: String,
        menuName: String,
        menuPrice: String,
        menuQty: String,
        menuImageUrl: String,
        menuDiscountedPrice: String?
    ): Boolean {
        return try {
            val order = hashMapOf(
                "orders_id" to ordersUid,
                "menu_id" to menuUid,
                "merchant_uid" to merchantUid,
                "student_uid" to studentUid,
                "order_status" to orderStatus,
                "menu_name" to menuName,
                "menu_price" to menuPrice,
                "menu_discounted_price" to menuDiscountedPrice, // Simpan harga diskon jika tersedia
                "menu_qty" to menuQty,
                "order_pickuptime" to Instant.now().toEpochMilli().toString(),
                "menu_imageurl" to menuImageUrl, // Simpan URL gambar
                "date_created" to Instant.now().toEpochMilli().toString()
            )

            db.collection("order")
                .add(order)
                .await()

            Log.d("OrderCreate", "Order created successfully: $ordersUid")
            true
        } catch (e: Exception) {
            Log.e("OrderCreate", "Failed to create order: ${e.message}", e)
            false
        }
    }


//    private suspend fun image(image: File): String {
//        var downloadUrl = ""
//        try {
//            val storageRef: StorageReference = FirebaseStorage.getInstance().reference
//            val fileRef = storageRef.child("order/${image.name}")
//            val stream = withContext(Dispatchers.IO) {
//                FileInputStream(image)
//            }
//            val uploadTask = fileRef.putStream(stream)
//            uploadTask.await()
//            downloadUrl = fileRef.downloadUrl.await().toString()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return downloadUrl
//    }

    companion object {
        const val TAG = "order"
    }
}