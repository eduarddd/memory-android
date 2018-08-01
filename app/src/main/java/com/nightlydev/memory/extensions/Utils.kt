package com.nightlydev.memory.extensions

import android.content.res.Resources
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE

/**
 * @author edu (edusevilla90@gmail.com)
 * @since 31-7-18
 */
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