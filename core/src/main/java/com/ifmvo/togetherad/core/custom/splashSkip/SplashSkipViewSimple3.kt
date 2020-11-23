package com.ifmvo.togetherad.core.custom.splashSkip

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.ifmvo.togetherad.core.R

/**
 * Created by Matthew Chen on 2020-04-17.
 */
class SplashSkipViewSimple3 : BaseSplashSkipView() {

    private lateinit var tvTime: TextView
    /**
     * 创建跳过按钮的布局
     */
    override fun onCreateSkipView(context: Context): View {
        val view = View.inflate(context, R.layout.layout_splash_skip_view_simple3, null)
        tvTime = view.findViewById(R.id.text_count_down)
        return tvTime
    }

    override fun handleTime(second: Int) {
        tvTime.text = second.toString()
    }

}