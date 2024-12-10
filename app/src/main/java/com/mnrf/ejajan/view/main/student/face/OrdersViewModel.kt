package com.mnrf.ejajan.view.main.student.face

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mnrf.ejajan.data.repository.CartRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.File
import java.util.UUID

class OrderViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CartRepository(application.applicationContext)

    private val _orderStatus = MutableLiveData<Boolean>()
    val orderStatus: LiveData<Boolean> get() = _orderStatus

    class OrderViewModel(application: Application) : AndroidViewModel(application) {

        private val repository = CartRepository(application.applicationContext)

        private val _orderStatus = MutableLiveData<Boolean>()
        val orderStatus: LiveData<Boolean> get() = _orderStatus

        suspend fun createOrder(): Boolean {
            val cartItems = repository.getCartItems()
            if (cartItems.isEmpty()) {
                _orderStatus.postValue(false)
                return false
            }

            val result = coroutineScope {
                cartItems.map { cartItem ->
                    async {
                        orders(
                            menuUid = cartItem.menuUid,
                            merchantUid = cartItem.merchantUid,
                            studentUid = "student_uid_here",
                            ordersUid = UUID.randomUUID().toString(),
                            menuName = cartItem.menuName,
                            menuDescription = cartItem.menuDescription,
                            menuPreparationtime = cartItem.menuPreparationTime,
                            menuPrice = cartItem.menuPrice,
                            menuQty = cartItem.menuQty,
                            menuImageFile = File(cartItem.menuImageFile)
                        )
                    }
                }.awaitAll()
            }

            val success = result.all { it }
            _orderStatus.postValue(success)

            return success
        }

        private suspend fun orders(
            menuUid: String,
            merchantUid: String,
            studentUid: String,
            ordersUid: String,
            menuName: String,
            menuDescription: String,
            menuPreparationtime: String,
            menuPrice: String,
            menuQty: String,
            menuImageFile: File
        ): Boolean {
            return try {
                Log.d("OrderViewModel", "Processing order $ordersUid")
                true // Simulasi pemesanan berhasil
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Failed to process order", e)
                false
            }
        }
    }
}

