package com.example.test_task.overview

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.test_task.database.Post
import com.example.test_task.databinding.PostItemBinding
import javax.inject.Singleton
/**
 * RecyclerView Adapter for displaying a list of [Post] items.
 *
 * @param listener Click listener for handling item clicks.
 */
@Singleton
class PostAdapter(private val listener: ClickPostListener) :
    ListAdapter<Post, PostAdapter.PostViewHolder>(PostDiffCallback) {
    /**
     * ViewHolder for individual [Post] items.
     *
     * @param binding Data binding object for the layout of each item.
     */
    inner class PostViewHolder(private var binding: PostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        /**
         * Binds the [Post] data to the UI elements.
         *
         * @param post The [Post] object to bind.
         */
        fun bind(post: Post) {
            Log.i("adapter", "bind called")
            binding.post = post
            binding.changeDate.text = if (post.editDate != null) "Edited: ${post.editDate.toString()}" else "${post.creationDate}"
            binding.executePendingBindings()
        }
    }
    /**
     * Callback for calculating the differences between two non-null items in a list.
     */
    companion object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }
    }
    /**
     * Creates and returns a new [PostViewHolder] for the specified [ViewGroup].
     *
     * @param parent The [ViewGroup] into which the new [View] will be added.
     * @param viewType The view type of the new [View].
     * @return A new [PostViewHolder] that holds the [PostItemBinding] of each item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        Log.i("adapter", "view holder called")
        return PostViewHolder(PostItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        )
    }
    /**
     * Called by [RecyclerView] to display the data at the specified position.
     *
     * @param holder The [PostViewHolder] that holds the [PostItemBinding] of each item.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        Log.i("adapter", "view holder bound")
        val post = getItem(position)
        holder.bind(post)
        holder.itemView.setOnClickListener {
            listener.onClick(post)
        }
    }
    /**
     * Listener for handling clicks on [Post] items.
     *
     * @param clickListener The lambda function that will be executed on item click.
     */
    @Singleton
    class ClickPostListener(val clickListener: (post: Post) -> Unit) {
        /**
         * Handles the click event for a [Post] item.
         *
         * @param post The clicked [Post] object.
         */
        fun onClick(post: Post) = clickListener(post)
    }
}