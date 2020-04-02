package com.matthewchen.togetherad.utils

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

/* 
 * (●ﾟωﾟ●) 通用的 Decoration
 * 
 * Created by Matthew_Chen on 2018/11/23.
 */
class CommonItemDecoration(pxBottom: Int) : RecyclerView.ItemDecoration() {

    private var mPxTop: Int = 0
    private var mPxBottom: Int = 0
    private var mPxLeft: Int = 0
    private var mPxRight: Int = 0

    init {
        mPxBottom = pxBottom
    }

    constructor(pxTop: Int, pxBottom: Int, pxLeft: Int, pxRight: Int) : this(pxBottom) {
        mPxTop = pxTop
        mPxLeft = pxLeft
        mPxRight = pxRight
    }

    /**
     *
     * @param outRect 边界
     * @param view recyclerView ItemView
     * @param parent recyclerView
     * @param state recycler 内部数据管理
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.set(mPxLeft, mPxTop, mPxRight, mPxBottom)
    }
}