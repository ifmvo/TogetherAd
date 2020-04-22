package com.ifmvo.togetherad.core.custom.splashSkip

import android.content.Context
import android.view.View
import android.view.ViewGroup

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-17.
 */
abstract class BaseSplashSkipView {

    abstract fun onCreateSkipView(context: Context): View

    abstract fun getLayoutParams(): ViewGroup.LayoutParams

    open fun handleTime(second: Int) {

    }
}