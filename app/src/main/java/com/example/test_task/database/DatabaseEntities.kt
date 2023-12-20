package com.example.test_task.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

/**
 * Data class representing a post in the application.
 *
 * This class is used to store information about a post, including its identifier,
 * comment, creation date, edit date (if applicable), picture URL, and associated color.
 *
 * @property id The unique identifier for the post. If null, it indicates a new post that hasn't been stored in the database yet.
 * @property comment The comment or description associated with the post.
 * @property creationDate The date when the post was initially created.
 * @property editDate The date when the post was last edited. It can be null if the post has never been edited.
 * @property picture The URL or path to the picture associated with the post.
 * @property color The color associated with the post.
 */
@Entity(tableName = "posts")
@Parcelize
data class Post (
    @PrimaryKey(autoGenerate = true)
    var id: Long?,
    var comment: String,
    var creationDate: LocalDate,
    var editDate: LocalDate?,
    var picture: String,
    var color: String
) : Parcelable


/**
 * Data class representing a post picture in the application.
 *
 * This class is used to store information about a post picture, including its identifier,
 * author, dimensions (width and height), URL, and download URL.
 *
 * @property id The unique identifier for the post picture.
 * @property author The author or source of the post picture.
 * @property width The width of the post picture in pixels.
 * @property height The height of the post picture in pixels.
 * @property url The URL of the post picture.
 * @property downloadUrl The download URL of the post picture.
 */
@Entity(tableName = "post_pictures")
data class PostPicture(
    @PrimaryKey
    val id: Long,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    @Json(name = "download_url")
    val downloadUrl: String)