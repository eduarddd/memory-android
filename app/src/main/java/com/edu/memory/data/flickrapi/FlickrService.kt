package com.edu.memory.data.flickrapi

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

/**
 * Created by Edu
 *
 * services/rest/?method=flickr.photos.search&api_key=0646d49711fd65f9157fd4ab856f39c2&text=kittens&privacy_filter=1&format=json&nojsoncallback=1
 */
interface FlickrService {

    @GET("services/rest")
    fun searchPhotos(@Query("text") searchQuery: String,
                     @Query("method") method: String = "flickr.photos.search",
                     @Query("api_key") apiKey: String,
                     @Query("privacy_filter") privacyFilter: Int = 1,
                     @Query("page") page: Int = Random().nextInt(100),
                     @Query("per_page") itemCount: Int,
                     @Query("format") format: String = "json",
                     @Query("nojsoncallback") noJsonCallback: Int = 1): Observable<PhotoSearchResponse>
}