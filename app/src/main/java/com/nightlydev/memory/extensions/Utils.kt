package com.nightlydev.memory.extensions

import android.content.res.Resources
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author edu (edusevilla90@gmail.com)
 * @since 31-7-18
 */
fun <T> Observable<T>.apiSubscribe(observer: Observer<in T>) {
    observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(observer)
}

fun getScreenWidth(): Int = Resources.getSystem().displayMetrics.widthPixels

fun getScreenHeight(): Int = Resources.getSystem().displayMetrics.heightPixels

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