package com.example.test_task.database

import android.content.Context
import android.util.Log
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Update
import com.example.test_task.utils.Converters
/**
 * The `PostDao` interface provides methods to interact with the "posts" table in the database.
 * It includes operations for retrieving, creating, and updating posts.
 */
@Dao
interface PostDao {
    /**
     * Retrieves all posts from the "posts" table.
     *
     * @return A list of [Post] objects representing all posts in the database.
     */
    @Query("SELECT * FROM posts")
    suspend fun getPosts() : List<Post>
    /**
     * Inserts a new post into the "posts" table.
     *
     * @param post The [Post] object to be inserted.
     */
    @Insert(entity = Post::class)
    suspend fun createPost(post: Post)
    /**
     * Updates an existing post in the "posts" table.
     *
     * @param post The [Post] object with updated information.
     */
    @Update(entity = Post::class)
    suspend fun editPost(post: Post)
}

/**
 * The `PostsDatabase` class is a Room Database that contains the "posts" table.
 * It is responsible for creating and managing instances of the database.
 */
@Database(entities = [Post::class], version = 2)
@TypeConverters(Converters::class)
abstract class PostsDatabase : RoomDatabase() {
    /**
     * Provides access to the [PostDao] for performing database operations.
     */
    abstract val postDao: PostDao

    /**
     * A companion object to provide methods for creating or retrieving the database instance.
     */
    companion object {
        /**
         * Database instance.
         * It uses the Singleton pattern to ensure only one instance of the database is created.
         */
        private var INSTANCE :PostsDatabase? = null
        /**
         * Creates or retrieves the [PostsDatabase] instance.
         *
         * @param context The application context.
         * @return The [PostsDatabase] instance.
         */
        fun getDatabase(context: Context): PostsDatabase {
            Log.i("DB", "called")
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PostsDatabase::class.java,
                    "posts"
                ).fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
