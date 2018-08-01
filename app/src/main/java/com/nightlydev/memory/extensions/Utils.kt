package com.nightlydev.memory.extensions

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.nightlydev.memory.App
import com.nightlydev.memory.GlideApp
import com.nightlydev.memory.R
import com.nightlydev.memory.data.flickrapi.PhotoSearchResponse
import com.nightlydev.memory.data.flickrapi.getDownloadUrl
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.math.sqrt

/**
 * @author edu (edusevilla90@gmail.com)
 * @since 31-7-18
 */
fun <T> Observable<T>.apiSubscribe(observer: Observer<in T>) {
    observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(observer)
}

fun View?.setVisible(visible: Boolean) {
    this?.visibility = if (visible) {
        VISIBLE
    } else {
        GONE
    }
}

fun Long?.toFormattedTime(): String {
    if (this == null) return "00:00"
    val minutes = (this / 60)
    val seconds = (this % 60)
    return "$minutes:$seconds"
}

fun inflateView(container: ViewGroup, @LayoutRes layoutId: Int): View {
    return LayoutInflater.from(container.context).inflate(layoutId, container, false)
}

fun downloadPhotos(photos: List<PhotoSearchResponse.PhotoObject>?) {
    photos?.forEach { photo ->
        GlideApp.with(App.instance)
                .load(photo.getDownloadUrl())
                .downloadOnly(500, 500)
    }
}

fun determineCardItemWidth(container: View, itemCount: Int): Int {
    val spanCount = sqrt(itemCount.toDouble()).toInt()
    val separation = container.context.resources.getDimension(R.dimen.card_grid_separation)
    return ((container.width - separation - (2 * spanCount * separation) ) / spanCount).toInt()
}

fun determinegetCardItemHeight(container: View, itemCount: Int): Int {
    val spanCount = sqrt(itemCount.toDouble()).toInt()
    val separation = container.context.resources.getDimension(R.dimen.card_grid_separation)
    return ((container.height - separation - (2 * spanCount * separation) ) / spanCount).toInt()
}