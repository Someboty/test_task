package com.example.test_task.repository

import android.util.Log
import com.example.test_task.database.PostPicture
import com.example.test_task.network.PostPictureService
/**
 * Repository class for managing [PostPicture] data.
 *
 * This repository uses the provided [PostPictureService] to fetch the list of post pictures.
 * It employs a caching mechanism to store the fetched pictures and avoids redundant network calls.
 *
 * @param retrofitService The Retrofit service interface for fetching post pictures.
 */
class PictureRepository(private val retrofitService: PostPictureService) {
    /**
     * Cached list of post pictures to avoid redundant network calls.
     */
    private var _postPictures : List<PostPicture>? = null
    /**
     * Suspend function to asynchronously fetch the list of post pictures.
     *
     * @return A list of [PostPicture] objects.
     */
    suspend fun getPostPictures() : List<PostPicture> {
        Log.i("picture repository", "pictures called")
        // Check if pictures are already fetched, if not, fetch them using the service
        if (_postPictures == null) {
            _postPictures = retrofitService.getPictures()
        }
        // Return the cached pictures or an empty list if null
        return _postPictures ?: ArrayList()
    }
}