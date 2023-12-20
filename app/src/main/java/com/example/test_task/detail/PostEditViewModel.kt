package com.example.test_task.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.test_task.R
import com.example.test_task.database.Post
import com.example.test_task.database.PostPicture
import com.example.test_task.repository.PictureRepository
import com.example.test_task.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject
/**
 * The `PostEditViewModel` class is responsible for managing the data and business logic
 * related to editing or creating a new post in the application. It extends the AndroidViewModel
 * class and is annotated with HiltViewModel for integration with Hilt dependency injection.
 *
 * @property application The application context required for resources and dependencies.
 * @property postRepository The repository providing access to post-related data.
 * @property pictureRepository The repository providing access to post picture data.
 */
@HiltViewModel
class PostEditViewModel @Inject constructor (
    private val application: Application,
    private val postRepository: PostRepository,
    private val pictureRepository: PictureRepository
) : AndroidViewModel(application) {
        // Coroutine related properties
        private var viewModelJob = Job()
        private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


        // LiveData for post pictures
        private val _pictures = MutableLiveData<List<PostPicture>>()
        val pictures : LiveData<List<PostPicture>>
            get() = _pictures

        // Properties for managing post data
        var postId: Long = -1
        private var _postColor : String? = null
        var postCreationDate : String = ""
        var postPictureUrl: String? = null
        var postComment : String? = null
        var isPostEditing : Boolean = false
        // LiveData for navigation events
        private val _navigateToOverview = MutableLiveData<Boolean?>()
        val navigateToOverview: LiveData<Boolean?>
            get() = _navigateToOverview

        private val _navigateToAuth = MutableLiveData<Boolean?>()
        val navigateToAuth: LiveData<Boolean?>
            get() = _navigateToAuth
        // LiveData for Snackbar messages
        private val _snackbarMessage = MutableLiveData<String?>()
        val snackbarMessage: LiveData<String?>
            get() = _snackbarMessage

        // Array of color options for posts
        private val colorsList = application.resources.getStringArray(R.array.color_array_values)
        /**
         * Initializes the ViewModel by loading post pictures.
         */
        init {
            Log.i("EditFragment", "created")
            getPictures()
        }
        /**
         * Cancels the ViewModel's coroutine job when it's no longer needed.
         */
        override fun onCleared() {
            super.onCleared()
            viewModelJob.cancel()
        }
        /**
         * Saves the post data, either by updating an existing post or creating a new one.
         * Displays Snackbar messages for success or failure.
         */
        fun savePost() {
            // Validation checks for post data
            if (postComment.isNullOrEmpty()) {
                snackbarMessage("Please, add some comment")
                return
            }
            if (postPictureUrl.isNullOrEmpty()) {
                snackbarMessage("Please, choose post picture")
                return
            }
            if (_postColor == null) {
                snackbarMessage("Please, choose post color")
                return
            }
            // Coroutine launched in IO dispatcher for database operations
            coroutineScope.launch(Dispatchers.IO) {
                try {
                    Log.i("EditFragment", "Save post called")
                    //editing existing post
                    if(isPostEditing) {
                        val updatedPost = Post(
                            postId,
                            postComment!!,
                            LocalDate.parse(postCreationDate),
                            LocalDate.now(),
                            postPictureUrl!!,
                            _postColor!!
                        )
                        postRepository.updatePost(updatedPost)
                        Log.i("EditFragment", "Post with id $postId is updated")
                        withContext(Dispatchers.Main) {
                            snackbarMessage("Post is updated!")
                        }
                    //creating a new one
                    } else {
                        val newPost = Post(
                            null,
                            postComment!!,
                            LocalDate.now(),
                            null,
                            postPictureUrl!!,
                            _postColor!!
                        )
                        postRepository.createPost(newPost)
                        Log.i("EditFragment", "New post created")
                        withContext(Dispatchers.Main) {
                            snackbarMessage("Post is created!")
                        }
                    }
                    withContext(Dispatchers.Main) {
                        navigateToOverview()
                    }
                } catch (t: Throwable) {
                    //handling exceptions
                    Log.e("EditFragment", "New post creation failed: ${t.message}")
                    snackbarMessage("New post creation failed: ${t.message}")
                }
            }
        }
        /**
         * Sets the post color based on the selected position in the color options array.
         *
         * @param position The position of the selected color.
         */
        fun setColorByPosition(position: Int) {
            _postColor = colorsList[position]
        }
        /**
         * Sets the post color by name.
         *
         * @param color The name of the selected color.
         */
        fun setColorByName(color: String) {
            _postColor = color
        }
        /**
         * Gets the index of the color in the color options array.
         *
         * @param color The name of the color.
         * @return The index of the color in the array.
         */
        fun getColorIndex(color: String) : Int {
            return colorsList.indexOf(color)
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
         * Marks the navigation to the overview screen as completed.
         */
        fun navigateToOverviewCompleted() {
            _navigateToOverview.value = false
        }
        /**
         * Initiates navigation to the overview screen.
         */
        fun navigateToOverview() {
            _navigateToOverview.value = true
        }
        /**
         * Marks the completion of displaying a Snackbar message by resetting the value of
         * [snackbarMessage].
         */
        fun snackbarMessageCompleted() {
            _snackbarMessage.value = null
        }
        /**
         * Displays a Snackbar message to the user.
         *
         * @param message The message to be displayed.
         */
        fun snackbarMessage(message: String) {
            _snackbarMessage.value = message
        }
        /**
         * Gets the list of pictures for posts using the [pictureRepository] and coroutines.
         * If successful, updates the [_pictures] LiveData with the received data.
         * If unsuccessful, logs an error and displays a Snackbar message.
         */
        private fun getPictures() {
            coroutineScope.launch {
                Log.i("EditFragment", "Getting pictures started")
                try {
                    _pictures.value = pictureRepository.getPostPictures()
                    Log.i("EditFragment", "Getting pictures succeeded, received ${_pictures.value!!.size}")
                } catch (t: Throwable) {
                    _pictures.value = ArrayList()
                    Log.e("EditFragment", "Getting pictures failed: ${t.message}")
                    withContext(Dispatchers.Main) {
                        snackbarMessage("Getting pictures failed, check your internet connection")
                    }
                }
            }
        }
}