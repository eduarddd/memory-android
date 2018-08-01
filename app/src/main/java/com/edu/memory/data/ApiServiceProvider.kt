package com.edu.memory.data

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.edu.memory.BuildConfig
import com.edu.memory.data.flickrapi.FlickrService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

fun provideFlickrService(): FlickrService {
    return provideRetrofit(FLICKR_BASE_URL).create(FlickrService::class.java)
}

private fun provideRetrofit(baseUrl: String): Retrofit {
    return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(provideGson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(provideOkHttpClient())
            .build()
}

private fun provideOkHttpClient(): OkHttpClient {
    val httpClient = OkHttpClient.Builder()

    if (BuildConfig.DEBUG) {
        httpClient.addInterceptor(provideHttpLoggingInterceptor())
    }

    return httpClient.build()
}

private fun provideGson(): Gson = GsonBuilder().create()

private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

private const val FLICKR_BASE_URL = "https://api.flickr.com/"
