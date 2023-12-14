package com.example.bottomnavbar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomnavbar.R
import com.example.bottomnavbar.model.Forum
import com.example.bottomnavbar.view.fragment.ForumFragment
import com.example.bottomnavbar.viewModel.ForumViewModel
import com.example.bottomnavbar.viewholders.ForumViewHolder
import kotlinx.coroutines.launch

class ForumAdapter(val forums: List<Forum>, private val viewModel: ForumViewModel, private val lifecycleOwner: ForumFragment) : RecyclerView.Adapter<ForumViewHolder>() {

    private val mutableForums: MutableList<Forum> = forums.toMutableList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_item_forum, parent, false)
        return ForumViewHolder(parent.context, view)
    }

    override fun onBindViewHolder(holder: ForumViewHolder, position: Int) {
        val forum = mutableForums[position]
        holder.bind(forum)

//        // Ajoutez un clic sur le bouton pour chaque élément
//        holder.itemView.findViewById<ImageButton>(R.id.detailsButton).setOnClickListener {
//            val intent = Intent(context, ForumDetails::class.java)
//            intent.putExtra("forumTitle",forum.title)
//            intent.putExtra("description",forum.description)
//            intent.putExtra("forumImage",forum.image)
//
//            context.startActivity(intent)
//        }
    }

    override fun getItemCount(): Int {
        return mutableForums.size
    }

    fun getForumIdAtPosition(position: Int): String {
        return mutableForums[position].id
    }

    fun removeForum(position: Int) {
        if (position >= 0 && position < mutableForums.size) {
            // Récupérer l'ID du forum à la position spécifiée
            val forumId = getForumIdAtPosition(position)

            if (forumId != null) {
                viewModel.viewModelScope.launch {
                    viewModel.deleteForum(forumId)

                    mutableForums.removeAt(position)

                    notifyItemRemoved(position)
                }
            }
        }
    }


}