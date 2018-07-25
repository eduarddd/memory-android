package com.nightlydev.memory.extensions

import android.content.Context
import android.widget.Toast

/**
 * Created by edu
 */
fun Context?.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}