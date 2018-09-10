package com.edu.memory.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.edu.memory.BuildConfig
import com.edu.memory.data.flickrapi.FlickrService
import com.edu.memory.data.flickrapi.PhotoObject
import com.edu.memory.data.flickrapi.PhotoSearchResponse
import com.edu.memory.extensions.apiSubscribe
import com.edu.memory.extensions.savePhotosInCache
import javax.inject.Inject

/**
 * Created by Edu
 */
class PhotosRepository
@Inject constructor(val flickrService: FlickrService) {

    fun fetchPhotos(searchQuery: String, count: Int): LiveData<Resource<List<PhotoObject>?>> {
        val result = MutableLiveData<Resource<List<PhotoObject>?>>()
        result.value = Resource.loading()

        flickrService
                .searchPhotos(
                        searchQuery = searchQuery,
                        apiKey = BuildConfig.FLICKR_API_KEY,
                        itemCount = count)
                .apiSubscribe(object : ApiObserver<PhotoSearchResponse>() {
                    override fun onSuccess(response: PhotoSearchResponse) {
                        savePhotosInCache(response.responseObject?.photoList)
                        result.value = Resource.success(response.responseObject?.photoList)
                    }

                    override fun onError(e: Throwable) {
                        result.value = Resource.error(e.message)
                    }
                })
        return result
    }
}