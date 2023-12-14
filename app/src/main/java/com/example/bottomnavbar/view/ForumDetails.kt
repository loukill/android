package com.example.bottomnavbar.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.bottomnavbar.R
import com.squareup.picasso.Picasso

class ForumDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum_details)

        var forumImage = intent.getStringExtra("forumImage")
        var title : TextView = findViewById(R.id.titleTextView)
        var description : TextView = findViewById(R.id.descriptionTextView)
        var image : ImageView = findViewById(R.id.forumImageView)
        Picasso.get()
            .load("http://10.0.2.2:3000/img/" + forumImage)
            .fit()
            .centerInside()
            .into(image);

        title.text = intent.getStringExtra("forumTitle")
        description.text = intent.getStringExtra("description")

    }



}