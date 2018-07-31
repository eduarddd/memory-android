package com.nightlydev.memory.data

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class ApiObserver<T> : Observer<T> {
    override fun onComplete() {}

    override fun onSubscribe(d: Disposable) {}

    override fun onNext(t: T) { onSuccess(t) }

    abstract fun onSuccess(response: T)
}