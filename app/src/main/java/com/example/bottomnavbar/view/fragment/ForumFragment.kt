package com.example.bottomnavbar.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomnavbar.R
import com.example.bottomnavbar.adapter.ForumAdapter
import com.example.bottomnavbar.api.ForumService
import com.example.bottomnavbar.view.ForumAdd
import com.example.bottomnavbar.viewModel.ForumViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForumFragment : Fragment() {

    private lateinit var forumAdapter: ForumAdapter
    private lateinit var forumApiService: ForumService
    private lateinit var recyclerView: RecyclerView
    private val viewModel: ForumViewModel by viewModels()
    private lateinit var addButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forum, container, false)

        recyclerView = view.findViewById(R.id.rvforum)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        addButton = view.findViewById(R.id.addButton)

        // Initialize Retrofit service
        forumApiService = com.example.bottomnavbar.api.RetrofitClient.retrofit.create(ForumService::class.java)

        // Fetch forum data from the API
        fetchForumData()

        addButton.setOnClickListener {
            val intent = Intent(requireContext(), ForumAdd::class.java)
            startActivity(intent)
        }

        // Ajouter ItemTouchHelper pour la suppression par glissement
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback())
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return view
    }

    private fun fetchForumData() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = forumApiService.getAllForums()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        forumAdapter = ForumAdapter(
                            response.body() ?: emptyList(),
                            viewModel,
                            this@ForumFragment
                        )
                        recyclerView.adapter = forumAdapter
                    } else {
                        // Handle API error
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle network failure
            }
        }
    }

    private inner class SwipeToDeleteCallback : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            // Récupérer l'ID du forum à partir de l'adapter
            val forumId = forumAdapter.getForumIdAtPosition(viewHolder.adapterPosition)

            // Appeler deleteForum à l'intérieur d'une coroutine
            lifecycleScope.launch {
                viewModel.deleteForum(forumId)
                forumAdapter.removeForum(viewHolder.adapterPosition)

            }
        }
    }


}