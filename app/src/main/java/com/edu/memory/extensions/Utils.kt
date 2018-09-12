package com.edu.memory.extensions

import android.arch.lifecycle.MutableLiveData
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.edu.memory.App
import com.edu.memory.GlideApp
import com.edu.memory.R
import com.edu.memory.data.flickrapi.PhotoObject
import com.edu.memory.data.flickrapi.getDownloadUrl
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.math.sqrt

/**
 * Created by edu
 */
fun <T> Observable<T>.apiSubscribe(observer: Observer<in T>) {
    observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(observer)
}

fun View?.setVisible(visible: Boolean) {
    this?.visibility = if (visible) VISIBLE else GONE
}

fun Long?.toFormattedDuration(): String {
    this ?: return "00:00"
    val absSeconds = Math.abs(this)
    val positive = String.format(
            "%d:%02d:%02d",
            absSeconds / 3600,
            absSeconds % 3600 / 60,
            absSeconds % 60)
    return if (this < 0) "-$positive" else positive
}

fun inflateView(container: ViewGroup, @LayoutRes layoutId: Int): View {
    return LayoutInflater.from(container.context).inflate(layoutId, container, false)
}

fun savePhotosInCache(photos: List<PhotoObject>?) {
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

fun determineCardItemHeight(container: View, itemCount: Int): Int {
    val spanCount = sqrt(itemCount.toDouble()).toInt()
    val separation = container.context.resources.getDimension(R.dimen.card_grid_separation)
    return ((container.height - separation - (2 * spanCount * separation) ) / spanCount).toInt()
}

fun <T> MutableLiveData<out List<T>>.addItem(item: T) {
    val oldList = value ?: listOf()
    oldList.toMutableList().let {
        it.add(item)
        value = it
    }
}

fun <T> MutableLiveData<out List<T>>.removeItem(item: T) {
    val oldList = value ?: listOf()
    oldList.toMutableList().let {
        it.remove(item)
        value = it
    }
}