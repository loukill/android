package com.example.bottomnavbar.viewholders

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomnavbar.R
import com.example.bottomnavbar.model.Forum
import com.example.bottomnavbar.view.ForumDetails
import com.example.bottomnavbar.view.ForumUpdate
import com.squareup.picasso.Picasso

class ForumViewHolder(private val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
    private val contentTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
    private val detailButton: ImageButton = itemView.findViewById(R.id.detailsButton)
    private val modifyButton: ImageButton = itemView.findViewById(R.id.modifyButton)
    private val image: ImageView = itemView.findViewById(R.id.forumImageView)




    fun bind(forum: Forum) {
        Picasso.get()
            .load("http://10.0.2.2:3000/img/forum/" + forum.image)
            .fit()
            .centerInside()
            .into(image);
        titleTextView.text = forum.title
        contentTextView.text = forum.description

        detailButton.setOnClickListener {

            val intent = Intent(context, ForumDetails::class.java)
            intent.putExtra("forumTitle",forum.title)
            intent.putExtra("description",forum.description)
            intent.putExtra("forumImage",forum.image)

            context.startActivity(intent)

        }

        modifyButton.setOnClickListener {

            val intent = Intent(context, ForumUpdate::class.java)
            intent.putExtra("forumTitle",forum.title)
            intent.putExtra("description",forum.description)
            intent.putExtra("forumImage",forum.image)

            context.startActivity(intent)

        }

    }
}