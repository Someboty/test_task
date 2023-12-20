package com.example.test_task.auth

import android.content.Context
import javax.inject.Singleton

/**
 * The `AuthUtil` class provides utility methods for managing user authentication status.
 *
 * @property context The application context used to access SharedPreferences.
 */

@Singleton
class AuthUtil {
    /**
     * Logs the user out by updating the authentication status in SharedPreferences.
     *
     * @param context The application context.
     */
    companion object{
        /**
         * Logs the user out by updating the authentication status in SharedPreferences.
         *
         * @param context The application context.
         */
        fun logOut(context: Context) {
            context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("authenticated", false)
                .apply()
        }
    }
}