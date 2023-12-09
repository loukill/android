package com.example.bottomnavbar.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bottomnavbar.R
import com.example.bottomnavbar.api.RetrofitClient
import com.example.bottomnavbar.model.TextCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DisplayTextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_text)


            fetchTextCategories()

    }

    private fun fetchTextCategories() {
        Log.d("DisplayTextActivity", "Fetching text categories")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getTextCategories()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        setupSpinner(response.body()!!)
                    }else {
                        Log.e("DisplayTextActivity", "Error fetching categories: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("DisplayTextActivity", "Exception in fetching categories", e)
            }
        }
    }

    private fun setupSpinner(categories: List<TextCategory>) {
        val spinner: Spinner = findViewById(R.id.spinnerTextCategory)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories.map { it.title })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedCategory = categories[position]
                fetchTextByCategory(selectedCategory.id)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional: Handle the case where no category is selected
            }
        }
    }


    private fun showCategoryDialog(categories: List<TextCategory>) {
        Log.d("DisplayTextActivity", "Showing categories in dialog: ${categories.map { it.title }}")
        val categoryTitles = categories.map { it.title }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Choose Category")
            .setItems(categoryTitles) { _, which ->
                val selectedCategory = categories[which]
                Log.d("DisplayTextActivity", "Category selected: ${selectedCategory.title}")
                fetchTextByCategory(selectedCategory.id)
            }
            .show()
    }

    private fun fetchTextByCategory(categoryId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getTextByCategoryId(categoryId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val text = response.body()!!
                        Log.d("DisplayTextActivity", "Fetched text: ${text.contenu}")
                        val textView = findViewById<TextView>(R.id.tvTextContent)
                        textView.text = text.contenu
                    } else {
                        Log.e("DisplayTextActivity", "Error fetching text: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("DisplayTextActivity", "Exception in fetching text", e)
            }
        }
    }



}


