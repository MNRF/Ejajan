package com.mnrf.ejajan.view.main.student.face

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
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
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import com.mnrf.ejajan.databinding.ActivityStudentFaceConfirmBinding
import com.mnrf.ejajan.view.main.student.summary.OrderSummaryActivity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FaceConfirmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentFaceConfirmBinding
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentFaceConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Order Confirmation "
        }

        requestCameraPermission()

        cameraExecutor = Executors.newSingleThreadExecutor()
        binding.progressBar.visibility = View.GONE

    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
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

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            startCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            Toast.makeText(this, "Izin kamera ditolak.", Toast.LENGTH_SHORT).show()
        }
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
                        binding.progressBar.visibility = View.GONE
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
                                Toast.makeText(this, "Konfirmasi Pesanan Berhasil.", Toast.LENGTH_SHORT).show()
//                                email = "student.1234@student.ejajan.com"
//                                password = "student.1234"
//                                handleLogin(email, password)
                            }

                            val intent = Intent(this@FaceConfirmActivity, OrderSummaryActivity::class.java)
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


    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

//    private fun showAlert(message: String, onDismiss: (() -> Unit)? = null) {
//        Log.d(TAG, "showAlert dipanggil dengan pesan: $message")
//        AlertDialog.Builder(this).apply {
//            setTitle("Informasi")
//            setMessage(message)
//            setPositiveButton("Lanjut") { _, _ ->
//                Log.d(TAG, "Dialog ditutup, memanggil onDismiss.")
//                onDismiss?.invoke()
//            }
//            create()
//            show()
//        }
//    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        private const val TAG = "LoginStudent"
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
    }
}