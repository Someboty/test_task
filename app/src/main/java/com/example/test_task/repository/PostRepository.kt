package com.example.test_task.repository

import android.util.Log
import com.example.test_task.database.Post
import com.example.test_task.database.PostsDatabase
/**
 * Repository class for managing Post entities.
 *
 * This class provides methods to interact with the underlying PostsDatabase.
 *
 * @property database The underlying database for Post entities.
 */
class PostRepository(private val database: PostsDatabase) {
    /**
     * List of posts retrieved from the database. It is initially uninitialized and
     * gets populated by calling [refreshPosts] method.
     */
    private lateinit var _posts : List<Post>
    /**
     * Retrieves the list of posts. It first refreshes the posts by calling [refreshPosts]
     * and then returns the list of posts.
     *
     * @return A list of posts.
     */
    suspend fun getPosts() : List<Post> {
        refreshPosts()
        Log.i("post repository", "${_posts.size} posts called")
        return _posts
    }
    /**
     * Creates a new post by inserting it into the database using the PostDao.
     *
     * @param post The post to be created.
     */
    suspend fun createPost(post: Post) {
        database.postDao.createPost(post)
        Log.i("post repository", "post created")
    }
    /**
     * Updates an existing post in the database using the PostDao.
     *
     * @param post The post to be updated.
     */
    suspend fun updatePost(post: Post) {
        database.postDao.editPost(post)
        Log.i("post repository", "post updated")
    }
    /**
     * Refreshes the list of posts by fetching them from the database using the PostDao.
     * The refreshed list is stored in the [_posts] property.
     */
    private suspend fun refreshPosts() {
        Log.i("post repository", "posts refreshed")
        _posts = database.postDao.getPosts()
    }
}