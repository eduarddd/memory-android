package com.edu.memory.data.flickrapi

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Edu
 *
 * {
 *    "photos": { "page": 1, "pages": "2855", "perpage": 100, "total": "285481",
    "photo": [
        {   "id": "43703550792",
            "owner": "93635264@N04",
            "secret": "309b566a12",
            "server": "1814",
            "farm": 2, "title": "8Q4A2514.jpg", "ispublic": 1, "isfriend": 0, "isfamily": 0
        },
        ...
        }

    Download url: https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
 */
class PhotoSearchResponse: Serializable {
    @SerializedName("photos")
    val responseObject: ResponseObject? = null

    inner class ResponseObject {
        @SerializedName("photo")
        val photoList: List<PhotoObject>? = null
    }

    inner class PhotoObject: Serializable {
        @SerializedName("id")
        val id: String? = null

        @SerializedName("owner")
        val owner: String? = null

        @SerializedName("secret")
        val secret: String? = null

        @SerializedName("server")
        val serverId: String? = null

        @SerializedName("farm")
        val farmId: String? = null
    }
}

fun PhotoSearchResponse.PhotoObject?.getDownloadUrl(): String {
    if (this == null) return ""
    return "https://farm$farmId.staticflickr.com/$serverId/${id}_$secret.jpg"
}