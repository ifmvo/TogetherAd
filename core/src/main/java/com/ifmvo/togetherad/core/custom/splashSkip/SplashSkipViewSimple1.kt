package com.ifmvo.togetherad.core.custom.splashSkip

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.ifmvo.togetherad.core.R

/*
 *
 * 
 * Created by Matthew Chen on 2020-04-17.
 */
class SplashSkipViewSimple1 : BaseSplashSkipView() {

    override fun onCreateSkipView(context: Context): View {
        return View.inflate(context, R.layout.layout_splash_skip_view_simple1, null)
    }

    override fun getLayoutParams(): ViewGroup.LayoutParams {
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.END or Gravity.TOP
        layoutParams.topMargin = 50
        layoutParams.rightMargin = 30
        return layoutParams
    }

}