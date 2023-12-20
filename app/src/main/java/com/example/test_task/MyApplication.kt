package com.example.test_task

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
/**
 * Application class for project.
 *
 * This class extends the [Application] class and is annotated with [@HiltAndroidApp][dagger.hilt.android.HiltAndroidApp].
 * It is responsible for initializing the Firebase application in the [onCreate] method.
 *
 * @property MyApplication - Custom application class for My Application.
 * @property onCreate - Called when the application is starting, before any other application objects have been created.
 *                   Initializes the Firebase application using [FirebaseApp.initializeApp].
 */
@HiltAndroidApp
class MyApplication : Application() {
    /**
     * Called when the application is starting, before any other application objects have been created.
     * Initializes the Firebase application using [FirebaseApp.initializeApp].
     */
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)
    }
}