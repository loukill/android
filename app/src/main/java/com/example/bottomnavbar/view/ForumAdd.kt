package com.example.bottomnavbar.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.bottomnavbar.R
import com.example.bottomnavbar.viewModel.ForumViewModel
import com.google.android.material.button.MaterialButton

class ForumAdd : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var contentTextView: TextView
    private lateinit var imageView: MaterialButton
    private lateinit var createForumButton: MaterialButton

    // Initialisez cette variable avec l'URI de l'image sélectionnée
    private var imageUri: Uri? = null
    private lateinit var viewModel: ForumViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum_add)

        // Initialisez les vues après l'appel à setContentView
        titleTextView = findViewById(R.id.titleEditText)
        contentTextView = findViewById(R.id.descriptionEditText)
        imageView = findViewById(R.id.chooseImageButton)
        createForumButton = findViewById(R.id.createForumButton)

        imageView.setOnClickListener {
            openGallery()
        }

        createForumButton.setOnClickListener {
            viewModel = ViewModelProvider(this).get(ForumViewModel::class.java)


            viewModel.addForum(imageUri, titleTextView.text.toString(), contentTextView.text.toString())
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    // Gestion de la réponse de la galerie
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            // Mettez à jour l'image affichée dans votre interface utilisateur si nécessaire
            // imageView.setImageURI(imageUri)
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 123
    }
}