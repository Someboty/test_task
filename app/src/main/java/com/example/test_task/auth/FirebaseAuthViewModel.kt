package com.example.test_task.auth

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * The `FirebaseAuthViewModel` class is responsible for handling authentication using Firebase.
 * It provides methods for user login, handling login errors, remembering user authentication,
 * and navigating to the overview screen upon successful login.
 *
 * @property application The [Application] instance used to access application-specific resources.
 * @property sharedPreferences The [SharedPreferences] instance used for storing authentication data.
 */
@HiltViewModel
class FirebaseAuthViewModel @Inject constructor (
    private val application: Application,
    private val sharedPreferences: SharedPreferences
) : AndroidViewModel(application) {

    /**
     * LiveData for navigation to the overview screen. Observers can listen to changes in this
     * LiveData to determine when to navigate to the overview screen.
     */
    private val _navigateToOverview = MutableLiveData<Boolean?>()
    val navigateToOverview: LiveData<Boolean?>
        get() = _navigateToOverview

    /**
     * LiveData for snackbar messages. Observers can listen to changes in this
     * LiveData to determine when to show snackbar message.
     */

    private val _snackbarMessage = MutableLiveData<String?>()
    val snackbarMessage: LiveData<String?>
        get() = _snackbarMessage

    /**
     * Initializes the `FirebaseAuthViewModel`. Checks if the user is already authenticated based
     * on the data stored in [sharedPreferences]. If authenticated, it triggers navigation to the
     * overview screen.
     */
    init {
        if (sharedPreferences.getBoolean("authenticated", false)) {
            displayOverview()
        }
    }

    /**
     * Attempts to log in the user using the provided email and password. If successful,
     * triggers navigation to the overview screen. If an error occurs, handles the error and
     * displays an appropriate error message.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @param rememberMe Indicates whether the user wants to be remembered for future logins.
     */
    fun login(email: String, password: String, rememberMe: Boolean) {
        // Input validation
        if (email.length < 6) {
            return snackbarMessage("Email can't be less than 6 characters")
        }
        if (password.length < 6) {
            return snackbarMessage("Password can't be less than 6 characters")
        }

        // Firebase authentication
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("Auth", "Login successful")
                    if (rememberMe) {
                        rememberAuthentication()
                    }
                    displayOverview()
                } else {
                    handleLoginError(task.exception)
                }
            }
    }

    /**
     * Triggers navigation to the overview screen by updating the value of [_navigateToOverview].
     */
    fun displayOverview() {
        _navigateToOverview.value = true
    }

    /**
     * Marks the completion of navigation to the overview screen by resetting the value of
     * [_navigateToOverview].
     */
    fun displayOverviewCompleted() {
        _navigateToOverview.value = false
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
     * Marks the completion of displaying a Snackbar message by resetting the value of
     * [snackbarMessage].
     */

    fun snackbarMessageCompleted() {
        _snackbarMessage.value = null
    }

    /**
     * Stores the information about the user's authentication status in [sharedPreferences].
     */
    private fun rememberAuthentication() {
        sharedPreferences
            .edit()
            .putBoolean("authenticated", true)
            .apply()
    }

    /**
     * Handles login errors by logging a warning and displaying an error message to the user.
     *
     * @param exception The exception representing the login error.
     */
    private fun handleLoginError(exception: Exception?) {
        Log.w("Auth", "signInWithEmail:failure", exception)
        snackbarMessage("Authentication failed: ${exception?.message}")
    }
}
