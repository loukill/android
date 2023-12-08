package com.example.bottomnavbar.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.bottomnavbar.R
import com.example.bottomnavbar.model.Category

class CategoryAdapter(
    var categories: List<Category>,
    private val onClick: (Category) -> Unit // Lambda to handle clicks
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    // ViewHolder that contains the view for an item in the list
    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryTitle: TextView = itemView.findViewById(R.id.categoryTitle)
        val categoryImage: ImageView = itemView.findViewById(R.id.categoryImage)
    }

    // Creating the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    // Returns the size of the category list
    override fun getItemCount(): Int = categories.size

    // Binding data to the view
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryTitle.text = category.title

        // Replace 'localhost' with '10.0.2.2' for testing in the emulator
        val imageUrl = category.image.replace("localhost", "10.0.2.2")
        Log.d("CategoryAdapter", "Image URL: $imageUrl") // Log to check the URL

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .apply(RequestOptions().fitCenter())
            .into(holder.categoryImage)

        // Set up the image click to execute the onClick lambda
        holder.categoryImage.setOnClickListener { onClick(category) }
    }
}
