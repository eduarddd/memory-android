package com.edu.memory.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.edu.memory.BuildConfig
import com.edu.memory.data.flickrapi.FlickrService
import com.edu.memory.data.flickrapi.PhotoSearchResponse
import com.edu.memory.extensions.apiSubscribe
import javax.inject.Inject

/**
 * Created by Edu
 */
class PhotosRepository
@Inject constructor(val flickrService: FlickrService) {

    fun searchPhotos(searchQuery: String): LiveData<Resource<List<PhotoSearchResponse.PhotoObject>?>> {
        val result = MutableLiveData<Resource<List<PhotoSearchResponse.PhotoObject>?>>()
        result.value = Resource.loading()

        flickrService.searchPhotos(searchQuery = searchQuery, apiKey = BuildConfig.FLICKR_API_KEY)
                .apiSubscribe(object : ApiObserver<PhotoSearchResponse>() {
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