package com.nightlydev.memory

import android.app.Application

/**
 * @author edu (edusevilla90@gmail.com)
 * @since 31-7-18
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
            private set
    }
}