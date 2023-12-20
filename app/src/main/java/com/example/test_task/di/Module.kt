package com.example.test_task.di

import android.content.Context
import android.content.SharedPreferences
import com.example.test_task.database.PostsDatabase
import com.example.test_task.network.PostPictureService
import com.example.test_task.repository.PictureRepository
import com.example.test_task.repository.PostRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://picsum.photos/v2/"
/**
 * Dagger Hilt Module providing dependencies for the application related to data and networking.
 *
 * The module includes the provision of:
 * - Application context
 * - PostsDatabase instance
 * - PostRepository instance
 * - PictureRepository instance
 * - SharedPreferences instance for authentication
 * - Moshi instance for JSON parsing
 * - Retrofit instance for network requests
 * - PostPictureService instance for communication with the Picsum API
 *
 * @constructor Creates an instance of the Dagger Hilt Module.
 */
@Module
@InstallIn(SingletonComponent::class)
class Module {
    /**
     * Provides the application context.
     *
     * @param context The application context.
     * @return The application context.
     */
    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context) :Context {
        return context
    }
    /**
     * Provides the singleton instance of PostsDatabase.
     *
     * @param context The application context.
     * @return The PostsDatabase instance.
     */
    @Provides
    fun provideDatabase(context: Context) : PostsDatabase{
        return PostsDatabase.getDatabase(context)
    }
    /**
     * Provides the singleton instance of PostRepository.
     *
     * @param database The PostsDatabase instance.
     * @return The PostRepository instance.
     */
    @Provides
    fun providePostRepository(database: PostsDatabase) : PostRepository {
        return PostRepository(database)
    }
    /**
     * Provides the singleton instance of PictureRepository.
     *
     * @param retrofit The PostPictureService instance.
     * @return The PictureRepository instance.
     */
    @Provides
    fun providePictureRepository(retrofit: PostPictureService) : PictureRepository {
        return PictureRepository(retrofit)
    }
    /**
     * Provides the singleton instance of SharedPreferences for authentication.
     *
     * @param context The application context.
     * @return The SharedPreferences instance.
     */
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context) : SharedPreferences {
        return context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    }
    /**
     * Provides the singleton instance of Moshi for JSON parsing.
     *
     * @return The Moshi instance.
     */
    @Provides
    fun provideMoshi() : Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
    /**
     * Provides the singleton instance of Retrofit for network requests.
     *
     * @param moshi The Moshi instance.
     * @return The Retrofit instance.
     */
    @Provides
    fun provideRetrofit(moshi: Moshi) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
    /**
     * Provides the singleton instance of PostPictureService for communication with the Picsum API.
     *
     * @param retrofit The Retrofit instance.
     * @return The PostPictureService instance.
     */
    @Provides
    fun providePostPictureService(retrofit: Retrofit): PostPictureService {
        return retrofit.create(PostPictureService::class.java)
    }
}