package com.mnrf.ejajan.view.login

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.appcompat.app.AlertDialog
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
import com.mnrf.ejajan.data.pref.OnboardingPreferences
import com.mnrf.ejajan.databinding.ActivityLoginStudentBinding
import com.mnrf.ejajan.view.login.LoginParentMerchant.Companion
import com.mnrf.ejajan.view.main.student.StudentActivity
import com.mnrf.ejajan.view.utils.ViewModelFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class LoginStudent : AppCompatActivity() {

    private lateinit var binding: ActivityLoginStudentBinding
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    private var email = ""
    private var password = ""
    private lateinit var userRole: String

    private val loginStudentViewModel: LoginStudentViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil role pengguna dari OnboardingPreferences
        val onboardingPreferences = OnboardingPreferences(this)
        userRole = onboardingPreferences.getUserRole().toString()

//        onBackPressedDispatcher.addCallback(this) {
//            finishAffinity()
//        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        binding.buttonStartCamera.setOnClickListener {
            requestCameraPermission()
        }

        binding.progressBar.visibility = View.GONE
    }

    private fun handleLogin(email: String, password: String) {

        if (email.isBlank() || password.isBlank() ||
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.length < 8
        ) {
            showAlert("Isi dengan lengkap dan benar!")
            return
        }

        // Call the ViewModel's login function
        loginStudentViewModel.login(email, password)
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
                                Toast.makeText(this, "Wajah berhasil terdeteksi.", Toast.LENGTH_SHORT).show()
                                email = "student.1234@student.ejajan.com"
                                password = "student.1234"
                                handleLogin(email, password)
                            }

                            val intent = Intent(this@LoginStudent, StudentActivity::class.java)
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

    private fun showAlert(message: String, onDismiss: (() -> Unit)? = null) {
        Log.d(TAG, "showAlert dipanggil dengan pesan: $message")
        AlertDialog.Builder(this).apply {
            setTitle("Informasi")
            setMessage(message)
            setPositiveButton("Lanjut") { _, _ ->
                Log.d(TAG, "Dialog ditutup, memanggil onDismiss.")
                onDismiss?.invoke()
            }
            create()
            show()
        }
    }

    companion object {
        private const val TAG = "LoginStudent"
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
    }
}
