package com.edu.memory.ui

import android.content.Context
import android.graphics.Rect
import android.support.annotation.DimenRes
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by edu
 */
class CardItemDecoration(private val itemOffset: Int) : RecyclerView.ItemDecoration() {

    constructor(context: Context, @DimenRes itemOffsetResId: Int) :
        this(context.resources.getDimensionPixelSize(itemOffsetResId))

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(itemOffset, itemOffset, itemOffset, itemOffset)
    }
}