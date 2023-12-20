package com.example.test_task.overview

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.test_task.database.Post
import com.example.test_task.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
/**
 * ViewModel class for managing data related to the Overview screen.
 *
 * This class extends AndroidViewModel and is responsible for handling the data and business logic
 * associated with the Overview screen. It interacts with the PostRepository to fetch and manage
 * posts data.
 *
 * @property application The application context.
 * @property repository The repository for accessing post-related data.
 *
 * @property viewModelJob The [Job] for managing coroutines.
 * @property coroutineScope The [CoroutineScope] for launching coroutines in the main dispatcher.
 *
 * @property PostStatus An enumeration class representing different states of post data loading.
 *
 * @constructor Initializes the OverviewViewModel.
 *
 * @see AndroidViewModel
 */
@HiltViewModel
class OverviewViewModel @Inject constructor (
    application: Application,
    private val repository: PostRepository
) : AndroidViewModel(application) {
    // Coroutine related properties
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _status = MutableLiveData<PostStatus>()
    val status: LiveData<PostStatus>
        get() = _status
    // List of posts
    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>>
        get() = _posts
    // LiveData for navigation events
    private val _navigateToPostEditing = MutableLiveData<Post?>()
    val navigateToPostEditing: LiveData<Post?>
        get() = _navigateToPostEditing

    private val _navigateToPostCreating = MutableLiveData<Boolean?>()
    val navigateToPostCreating: LiveData<Boolean?>
        get() = _navigateToPostCreating

    private val _navigateToAuth = MutableLiveData<Boolean?>()
    val navigateToAuth: LiveData<Boolean?>
        get() = _navigateToAuth
    /**
     * Initializes the ViewModel by loading posts.
     */
    init {
        Log.i("OverviewFragment", "Inited")
        getNewPosts()
    }
    /**
     * Cancels the ViewModel's coroutine job when it's no longer needed.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
    /**
     * Initiates navigation to the post editing screen.
     */
    fun navigateToPostEditing(post: Post) {
        Log.i("OverviewFragment", "Navigated")
        _navigateToPostEditing.value = post
    }
    /**
     * Marks the navigation to the post editing screen as completed.
     */
    fun navigateToPostEditingCompleted() {
        Log.i("OverviewFragment", "Navigate completed")
        _navigateToPostEditing.value = null
    }
    /**
     * Initiates navigation to the post creating screen.
     */
    fun navigateToPostCreating() {
        Log.i("OverviewFragment", "Navigated")
        _navigateToPostCreating.value = true
    }
    /**
     * Marks the navigation to the post creating screen as completed.
     */
    fun navigateToPostCreatingCompleted() {
        Log.i("OverviewFragment", "Navigate completed")
        _navigateToPostCreating.value = false
    }
    /**
     * Marks the navigation to the authentication screen as completed.
     */
    fun navigateToAuthCompleted() {
        _navigateToAuth.value = false
    }
    /**
     * Initiates navigation to the authentication screen.
     */
    fun navigateToAuth() {
        _navigateToAuth.value = true
    }
    /**
     * Gets the list of posts using the [postRepository] and coroutines.
     * If successful, updates the [posts] LiveData with the received data.
     * If unsuccessful, logs an error.
     */
    fun getNewPosts() {
        coroutineScope.launch {
            Log.i("OverviewFragment", "Getting posts started")
            _status.value = PostStatus.LOADING
            try {
                _posts.value = repository.getPosts()
                _status.value = PostStatus.DONE
                Log.i("OverviewFragment", "Getting posts succeeded")
            } catch (t: Throwable) {
                _status.value = PostStatus.ERROR
                _posts.value = ArrayList()
                Log.e("OverviewFragment", "Getting posts failed: ${t.message}")
            }
        }
    }
}

enum class PostStatus { LOADING, ERROR, DONE }
