package com.example.test_task

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
/**
 * The `MainActivity` serves as the main entry point for the Android application.
 *
 * @constructor Creates a new instance of `MainActivity`.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     * shut down, this Bundle contains the data it most recently supplied in onSaveInstanceState.
     * Note: Otherwise, it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("App", "started")
        super.onCreate(savedInstanceState)
        // Set the content view of the activity to the layout defined in `activity_main.xml`
        setContentView(R.layout.activity_main)
    }
}