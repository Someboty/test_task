package com.example.test_task.detail

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.test_task.R
import com.example.test_task.database.PostPicture
import com.example.test_task.databinding.PhotoItemBinding
import javax.inject.Singleton
/**
 * The `PicturesAdapter` class is a custom RecyclerView adapter designed to display a list
 * of `PostPicture` items. It supports item click events and highlights the selected item.
 *
 * @property listener A listener for item click events.
 * @property selectedItemPosition The position of the currently selected item.
 */
class PicturesAdapter(private val listener: ClickPictureListener) : ListAdapter<PostPicture, PicturesAdapter.PictureViewHolder>(PicturesDiffCallback) {
    private var selectedItemPosition: Int = RecyclerView.NO_POSITION
    /**
     * ViewHolder for individual picture items in the RecyclerView.
     *
     * @param binding The data binding object for the item layout.
     */
    class PictureViewHolder(private var binding: PhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        /**
         * Binds the `PostPicture` data to the item layout.
         *
         * @param picture The `PostPicture` item to bind.
         */
        fun bind(picture: PostPicture) {
            Log.i("adapter", "bind called")
            binding.picture = picture
            binding.executePendingBindings()
        }
    }
    /**
     * Callback for calculating the difference between two non-null items in a list.
     * Used by the ListAdapter to efficiently update the items.
     */
    companion object PicturesDiffCallback : DiffUtil.ItemCallback<PostPicture>() {
        override fun areItemsTheSame(oldItem: PostPicture, newItem: PostPicture): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PostPicture, newItem: PostPicture): Boolean {
            return oldItem.id == newItem.id
        }
    }
    /**
     * Inflates the layout for each item in the RecyclerView.
     *
     * @param parent The parent view group.
     * @param viewType The type of the view.
     * @return A new instance of the `PictureViewHolder`.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        Log.i("adapter", "view holder called")
        return PictureViewHolder(PhotoItemBinding.inflate(LayoutInflater.from(parent.context)))
    }
    /**
     * Binds the data to the `PictureViewHolder` and handles item click events.
     *
     * @param holder The `PictureViewHolder` for the current item.
     * @param position The position of the current item in the list.
     */
    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        Log.i("adapter", "view holder bound")
        val picture = getItem(position)
        holder.bind(picture)
        // Handle item click events
        holder.itemView.setOnClickListener {
            val clickedPosition = holder.adapterPosition
            if (clickedPosition != selectedItemPosition) {
                listener.onClick(getItem(clickedPosition))
                notifyItemChanged(selectedItemPosition)
                selectedItemPosition = clickedPosition
                notifyItemChanged(selectedItemPosition)
            }
        }
        // Highlight the selected item
        holder.itemView.setBackgroundResource(
            if (position == selectedItemPosition) R.drawable.image_border else 0
        )
    }
    /**
     * The `ClickPictureListener` class is a listener for handling item click events.
     *
     * @param clickListener The function to be executed when an item is clicked.
     */
    @Singleton
    class ClickPictureListener(val clickListener: (picture: PostPicture) -> Unit) {
        /**
         * Invokes the click listener function when an item is clicked.
         *
         * @param picture The clicked `PostPicture` item.
         */
        fun onClick(picture: PostPicture) = clickListener(picture)
    }
}
