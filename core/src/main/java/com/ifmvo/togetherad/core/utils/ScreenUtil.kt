package com.ifmvo.togetherad.core.utils

import android.content.Context

/* 
 *
 * 
 * Created by Matthew Chen on 2020-05-15.
 */
object ScreenUtil {

    fun getDisplayMetricsWidth(context: Context): Int {
        return context.applicationContext.resources.displayMetrics.widthPixels
    }

    fun getDisplayMetricsHeight(context: Context): Int {
        return context.applicationContext.resources.displayMetrics.heightPixels
    }
}