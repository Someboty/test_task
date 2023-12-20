package com.example.test_task.network

import com.example.test_task.database.PostPicture
import retrofit2.http.GET
/**
 * PostPictureService is an interface that defines methods for interacting with a remote service
 * to retrieve information about post pictures.
 *
 * @see [PostPicture] Represents a data class for a post picture.
 */
interface PostPictureService {
    /**
     * Retrieves a list of post pictures from the remote service.
     *
     *
     * The function is marked with the suspend modifier and it should be called
     * from a coroutine or another suspend function.
     *
     * @return A list of [PostPicture] objects representing post pictures.
     */
    @GET("list")
    suspend fun getPictures() : List<PostPicture>
}