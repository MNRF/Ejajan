package com.mnrf.ejajan.view.main.merchant.ui.activeorder

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import com.mnrf.ejajan.R
import com.mnrf.ejajan.data.model.MerchantOrderModel
import com.mnrf.ejajan.databinding.ActivityLoginStudentBinding
import com.mnrf.ejajan.view.main.merchant.MerchantActivity
import com.mnrf.ejajan.view.main.merchant.ui.setting.SettingViewModel
import com.mnrf.ejajan.view.main.student.StudentActivity
import com.mnrf.ejajan.view.utils.ViewModelFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class OrderConfirmationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginStudentBinding
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var order: MerchantOrderModel
    private var imageCapture: ImageCapture? = null

    private val orderConfirmationViewModel: OrderConfirmationViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        order = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(SELECTED, MerchantOrderModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<MerchantOrderModel>(SELECTED)
        } ?: throw IllegalArgumentException("Order is missing")

        cameraExecutor = Executors.newSingleThreadExecutor()

        binding.buttonStartCamera.setOnClickListener {
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            startCamera()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.surfaceProvider = binding.viewFinder.surfaceProvider
                }

            imageCapture = ImageCapture.Builder().build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { analysis ->
                    analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                        processDetectorFace(imageProxy)
                    }
                }

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture,
                    imageAnalyzer
                )
            } catch (exc: Exception) {
                Toast.makeText(this, "Gagal memunculkan kamera.", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "startCamera: ${exc.message}", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    @OptIn(ExperimentalGetImage::class)
    private fun processDetectorFace(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val options = FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .enableTracking()
                .build()

            val detector = FaceDetection.getClient(options)

            detector.process(image)
                .addOnSuccessListener { faces ->
                    runOnUiThread {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    if (faces.isNotEmpty()) {
                        for (face in faces) {
                            Log.d(TAG, "Face detected with bounds: ${face.boundingBox}")

                            val leftEar = face.getLandmark(FaceLandmark.LEFT_EAR)
                            leftEar?.let {
                                val leftEarPos = it.position
                                Log.d(TAG, "Left Ear Position: $leftEarPos")
                            }

                            val leftEyeContour = face.getContour(FaceContour.LEFT_EYE)?.points
                            leftEyeContour?.forEach { point ->
                                Log.d(TAG, "Left Eye Contour Point: $point")
                            }

                            face.trackingId?.let { id ->
                                Log.d(TAG, "Tracking ID: $id")
                            }

                            runOnUiThread {
                                //Implement Facial Recognition ML Model Here
                                //TODO Implement facial recognition feature

                                val faceContour = "th1sIs4dUmMyf4Cec0nToUr" // Hypothetical data
                                orderConfirmationViewModel.getStudentUidbyFaceContour(faceContour) { studentUid ->
                                    if (studentUid != null) {
                                        if (studentUid == order.studentUid) {
                                            orderConfirmationViewModel.confirmPickup(order.id)
                                            Log.d(TAG, "Pickup confirmed for order ID: ${order.id}")
                                        } else {
                                            Log.e(TAG, "Student UID mismatch. Expected: ${order.studentUid}, Found: $studentUid")
                                        }
                                    } else {
                                        Log.e(TAG, "Failed to fetch student UID for faceContour: $faceContour")
                                    }
                                }
                                Toast.makeText(this, "Wajah berhasil terdeteksi.", Toast.LENGTH_SHORT).show()
                            }
                            Toast.makeText(this,
                                "Pesanan selesai, saldo sudah diteruskan ke akun anda.",
                                Toast.LENGTH_LONG).show()
                            val intent = Intent(this, MerchantActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this, "Tidak ada wajah terdeteksi.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .addOnFailureListener { e ->
                    runOnUiThread {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, "Deteksi wajah gagal: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                    Log.e(TAG, "Face detection failed: ${e.message}", e)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }

        } else {
            imageProxy.close()
        }
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
        const val TAG = "OrderConfirmationActivity"
        const val SELECTED = "selected"
    }
}