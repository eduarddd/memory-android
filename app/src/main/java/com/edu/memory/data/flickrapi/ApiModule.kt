package com.edu.memory.data.flickrapi

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by edu
 */
@Module
class ApiModule {

    @Module
    companion object {
        private const val FLICKR_BASE_URL = "https://api.flickr.com/"

        @JvmStatic
        private fun getFlickrUrl(): String = FLICKR_BASE_URL

        @JvmStatic
        @Provides
        @Singleton
        fun provideFLickrService(retrofit: Retrofit): FlickrService =
                retrofit.create(FlickrService::class.java)

        @JvmStatic
        @Provides
        @Singleton
        fun provideRetrofit(okHttpClient: OkHttpClient,
                            converterFactory: GsonConverterFactory,
                            callAdapterFactory: RxJava2CallAdapterFactory): Retrofit =
                Retrofit.Builder()
                        .baseUrl(getFlickrUrl())
                        .addConverterFactory(converterFactory)
                        .addCallAdapterFactory(callAdapterFactory)
                        .client(okHttpClient)
                        .build()

        @JvmStatic
        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient = OkHttpClient()

        @JvmStatic
        @Provides
        @Singleton
        fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
                GsonConverterFactory.create(gson)

        @JvmStatic
        @Provides
        @Singleton
        fun provideGson(): Gson = GsonBuilder().create()

        @JvmStatic
        @Provides
        @Singleton
        fun provideCallAdapterFactory(): RxJava2CallAdapterFactory =
                RxJava2CallAdapterFactory.create()
    }
}




