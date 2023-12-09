package com.example.bottomnavbar.view

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bottomnavbar.R

class CameraActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_ATTACH_FILE = 2
        private const val TAG = "CameraActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        findViewById<ImageButton>(R.id.button_open_camera).setOnClickListener {
            openCamera()
        }

        findViewById<ImageButton>(R.id.button_attach_file).setOnClickListener {
            attachFile()
        }
    }

    private fun openCamera() {
        Log.d(TAG, "openCamera called")
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } else {
            Log.d(TAG, "No activity found to handle camera intent")
        }
    }

    private fun attachFile() {
        Log.d(TAG, "attachFile called")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"  // Vous pouvez spécifier un type MIME ici, par exemple "image/*" pour les images
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_ATTACH_FILE)
        } else {
            Toast.makeText(this, "Aucune application de gestion de fichiers trouvée", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult called with requestCode: $requestCode, resultCode: $resultCode")

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            Log.d(TAG, "Received image bitmap from camera")
            // Traitez l'imageBitmap ici
        }

        if (requestCode == REQUEST_ATTACH_FILE && resultCode == RESULT_OK) {
            val fileUri = data?.data
            Log.d(TAG, "Received file uri: $fileUri")
            // Traitez fileUri ici
        }
    }
}