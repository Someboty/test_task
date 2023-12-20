package com.example.test_task.utils

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.test_task.R
import com.example.test_task.database.Post
import com.example.test_task.database.PostPicture
import com.example.test_task.detail.PicturesAdapter
import com.example.test_task.overview.PostAdapter
import com.example.test_task.overview.PostStatus
/**
 * Binds an image URL to an ImageView using Glide for efficient loading and caching.
 *
 * @param imgView The target ImageView.
 * @param imgUrl The URL of the image to be loaded.
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    Log.i("binding adapters", "picture adapter called")
    imgUrl?.let {
        val imgUri = it.toUri()
            .buildUpon()
            .scheme("https")
            .build()

        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .override(300, 300)
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .into(imgView)
    }
}
/**
 * Binds a list of posts to a RecyclerView using the PostAdapter.
 *
 * @param recyclerView The target RecyclerView.
 * @param data The list of posts to be displayed.
 */
@BindingAdapter("listPostsData")
fun bindPostsRecyclerView(recyclerView: RecyclerView, data: List<Post>?) {
    Log.i("binding adapters", "post adapter called")
    val adapter = recyclerView.adapter as PostAdapter
    adapter.submitList(data)
}
/**
 * Binds a list of pictures to a RecyclerView using the PicturesAdapter.
 *
 * @param recyclerView The target RecyclerView.
 * @param data The list of pictures to be displayed.
 */
@BindingAdapter("listPicturesData")
fun bindPicturesRecyclerView(recyclerView: RecyclerView, data: List<PostPicture>?) {
    Log.i("binding adapters", "picture adapter called")
    val adapter = recyclerView.adapter as PicturesAdapter
    adapter.submitList(data)
}

/**
 * Binds a background color to a ConstraintLayout.
 *
 * @param view The target ConstraintLayout.
 * @param color The color code (in string format) to set as the background.
 */
@BindingAdapter("setBackground")
fun bindPostBackground(view: ConstraintLayout, color: String) {
    view.setBackgroundColor(Color.parseColor(color))
}
/**
 * Binds a status to an ImageView, controlling its visibility and setting an appropriate image.
 *
 * @param statusImageView The target ImageView.
 * @param status The status to be displayed.
 */
@BindingAdapter("postStatus")
fun bindStatus(statusImageView: ImageView, status: PostStatus?) {
    Log.i("binding adapters", "post status adapter called")
    statusImageView.visibility = when (status) {
        PostStatus.DONE -> View.GONE
        else -> View.VISIBLE
    }

    val imageResource = when (status) {
        PostStatus.LOADING, null -> R.drawable.loading_animation
        PostStatus.ERROR -> R.drawable.ic_connection_error
        PostStatus.DONE -> 0
    }

    if (imageResource != 0) {
        statusImageView.setImageResource(imageResource)
    }
}