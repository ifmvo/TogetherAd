package com.ifmvo.togetherad.core.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import org.jetbrains.annotations.NotNull

/**
 *
 * Created by Matthew Chen on 2020/9/9.
 */
val Float.dp get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)

/**
 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
 */
fun px2dp(@NotNull context: Context, px: Int): Float {
    val scale = context.applicationContext.resources.displayMetrics.density
    return px / scale + 0.5f
}

fun dpToPx(context: Context, dp: Int): Int {
    val r: Resources = context.applicationContext.resources
    val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics)
    return px.toInt()
}