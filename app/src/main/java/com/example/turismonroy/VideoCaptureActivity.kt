package com.example.turismonroy

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class VideoCaptureActivity : AppCompatActivity() {

    private lateinit var videoCapture: VideoCapture<Recorder>
    private lateinit var camera: Camera
    private lateinit var cameraExecutor: ExecutorService
    private var recording: Recording? = null
    private lateinit var recordButton: ImageButton
    private lateinit var switchCameraButton: ImageButton
    private lateinit var zoomSeekBar: SeekBar

    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val TAG = "VideoCaptureActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_capture)

        cameraExecutor = Executors.newSingleThreadExecutor()

        // Configuración de los botones y el SeekBar
        val cancelButton: ImageButton = findViewById(R.id.button_cancel_recording)
        cancelButton.setOnClickListener {
            Toast.makeText(this, "Grabación cancelada", Toast.LENGTH_SHORT).show()
            finish()
        }

        recordButton = findViewById(R.id.button_record)
        recordButton.setOnClickListener {
            if (recording == null) {
                startRecording()
            } else {
                stopRecording()
            }
        }

        switchCameraButton = findViewById(R.id.button_switch_camera)
        switchCameraButton.setOnClickListener {
            toggleCamera()
        }

        // Configuración del SeekBar para el zoom
        zoomSeekBar = findViewById(R.id.zoom_seekbar)
        zoomSeekBar.max = 20 // Ajuste máximo del SeekBar
        zoomSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (::camera.isInitialized) {
                    val zoomLevel = 1.0f + (progress / 10.0f)
                    camera.cameraControl.setZoomRatio(zoomLevel)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Verificar permisos y comenzar la cámara si están concedidos
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val previewView: PreviewView = findViewById(R.id.previewView)

            // Configurar la vista previa de la cámara
            val preview = androidx.camera.core.Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            // Configuración de grabación de video
            val recorder = Recorder.Builder().build()
            videoCapture = VideoCapture.withOutput(recorder)

            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture)
            } catch (exc: SecurityException) {
                Log.e(TAG, "Permisos insuficientes para la cámara", exc)
                Toast.makeText(this, "No se tienen permisos para la cámara", Toast.LENGTH_SHORT).show()
            } catch (exc: Exception) {
                Log.e(TAG, "Error al iniciar la cámara: ${exc.message}", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun toggleCamera() {
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        startCamera()
    }

    private fun startRecording() {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "video_${System.currentTimeMillis()}.mp4")
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/Turismonroy")
        }

        val mediaStoreOutputOptions = MediaStoreOutputOptions.Builder(
            contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        ).setContentValues(contentValues).build()

        try {
            recording = videoCapture.output
                .prepareRecording(this, mediaStoreOutputOptions)
                .apply {
                    if (allPermissionsGranted()) {
                        withAudioEnabled()
                    }
                }
                .start(ContextCompat.getMainExecutor(this)) { recordEvent ->
                    when (recordEvent) {
                        is VideoRecordEvent.Start -> {
                            Toast.makeText(this, "Grabación iniciada", Toast.LENGTH_SHORT).show()
                            recordButton.setImageResource(R.drawable.ic_record_stop)
                        }
                        is VideoRecordEvent.Finalize -> {
                            if (recordEvent.hasError()) {
                                Log.e(TAG, "Error en la grabación: ${recordEvent.error}")
                                Toast.makeText(this, "Error en la grabación", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Grabación guardada", Toast.LENGTH_SHORT).show()
                            }
                            recordButton.setImageResource(R.drawable.ic_record_start)
                            recording = null
                        }
                    }
                }
        } catch (exc: SecurityException) {
            Log.e(TAG, "Permisos insuficientes para la grabación de video", exc)
            Toast.makeText(this, "No se tienen permisos para grabar video", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopRecording() {
        recording?.stop()
        recording = null
        Toast.makeText(this, "Grabación detenida", Toast.LENGTH_SHORT).show()
        recordButton.setImageResource(R.drawable.ic_record_start)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Permisos no concedidos", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        recording?.close()
        cameraExecutor.shutdown()
    }
}
