package com.ifmvo.togetherad.core.custom.splashSkip

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.ifmvo.togetherad.core.R

/*
 *
 * 
 * Created by Matthew Chen on 2020-04-17.
 */
class SplashSkipViewSimple2 : BaseSplashSkipView() {

    private lateinit var tvTime: TextView

    override fun onCreateSkipView(context: Context): View {
        val skipView = View.inflate(context, R.layout.layout_splash_skip_view_simple2, null)
        tvTime = skipView.findViewById(R.id.time)
        return skipView
    }

    override fun handleTime(second: Int) {
        tvTime.text = second.toString()
    }

    override fun getLayoutParams(): ViewGroup.LayoutParams {
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.END or Gravity.TOP
        layoutParams.topMargin = 50
        layoutParams.rightMargin = 30
        return layoutParams
    }

}