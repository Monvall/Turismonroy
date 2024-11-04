package com.example.turismonroy

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Botón para capturar fotos
        val photoButton: Button = findViewById(R.id.button_capture_photo)
        photoButton.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        // Botón para grabar video
        val videoButton: Button = findViewById(R.id.button_record_video)
        videoButton.setOnClickListener {
            val intent = Intent(this, VideoCaptureActivity::class.java)
            startActivity(intent)
        }

        // Botón para abrir la galería del dispositivo
        val galleryButton: Button = findViewById(R.id.button_open_gallery)
        galleryButton.setOnClickListener {
            openDeviceGallery()
        }
    }

    // Método para abrir la galería de fotos del dispositivo
    private fun openDeviceGallery() {
        val intent = Intent(Intent.ACTION_VIEW, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivity(intent)
    }
}
