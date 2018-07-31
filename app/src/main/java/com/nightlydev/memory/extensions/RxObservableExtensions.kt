package com.nightlydev.memory.extensions

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Edu
 */
fun <T> Observable<T>.apiSubscribe(observer: Observer<in T>) {
    observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(observer)
}