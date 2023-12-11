package com.example.bottomnavbar.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomnavbar.R
import com.example.bottomnavbar.adapter.CategoryAdapter
import com.example.bottomnavbar.api.RetrofitClient
import com.example.bottomnavbar.model.Category
import com.example.bottomnavbar.view.DisplayTextActivity
import com.example.bottomnavbar.view.SimonGameActivity
import com.example.bottomnavbar.view.TicTacActivity
import kotlinx.coroutines.launch


class ActivityFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private var categoryList = listOf<Category>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycler_view)
        val layoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = layoutManager
        (activity as AppCompatActivity).supportActionBar?.hide()

        // Passez une lambda qui définit l'action à exécuter lors du clic sur une catégorie
        categoryAdapter = CategoryAdapter(categoryList) { category ->
            when (category.title) {
                "Cours" -> {
                    navigateToDispalyTextActivity()
                }
                "Simon dit" -> {
                    navigateToSimonGameActivity()
                }

                "Tic Tac Toe" -> {
                    navigateToTicTacActivity()
                }
                // Ajoutez d'autres cas au besoin
                else -> {
                    Toast.makeText(context, "Clicked on: ${category.title}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        recyclerView.adapter = categoryAdapter
        fetchCategories()
    }


    private fun fetchCategories() {
        lifecycleScope.launch {
            val response = RetrofitClient.apiService.getCategories()
            if (response.isSuccessful && response.body() != null) {
                categoryList = response.body()!!
                categoryAdapter.categories = categoryList // Update the existing adapter's data
                categoryAdapter.notifyDataSetChanged() // Notify the adapter of the data change
            } else {
                // Handle error
                Toast.makeText(context, "Failed to fetch categories", Toast.LENGTH_LONG).show()
                // Log the error or show an error message
            }
        }
    }

    private fun navigateToSimonGameActivity() {
        val intent = Intent(requireContext(), SimonGameActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToDispalyTextActivity() {
        val intent = Intent(requireContext(), DisplayTextActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToTicTacActivity() {
        val intent = Intent(requireContext(), TicTacActivity::class.java)
        startActivity(intent)
    }

}
