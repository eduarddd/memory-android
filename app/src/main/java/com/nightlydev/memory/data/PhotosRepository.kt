package com.nightlydev.memory.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.nightlydev.memory.data.flickrapi.FlickrService
import com.nightlydev.memory.data.flickrapi.PhotoSearchResponse
import com.nightlydev.memory.extensions.apiSubscribe

/**
 * Created by Edu
 */
class PhotosRepository {

    private val flickrService: FlickrService = provideFlickrService()

    fun searchPhotos(searchQuery: String): LiveData<Resource<List<PhotoSearchResponse.PhotoObject>?>> {
        val result = MutableLiveData<Resource<List<PhotoSearchResponse.PhotoObject>?>>()
        result.value = Resource.loading()

        flickrService.searchPhotos(searchQuery).apiSubscribe(object : ApiObserver<PhotoSearchResponse>() {
            override fun onSuccess(response: PhotoSearchResponse) {
                result.value = Resource.success(response.responseObject?.photoList)
            }

            override fun onError(e: Throwable) {
                result.value = Resource.error(e.message)
            }
        })
        return result
    }
}