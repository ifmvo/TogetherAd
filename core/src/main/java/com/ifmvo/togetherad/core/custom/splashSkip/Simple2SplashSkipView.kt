package com.ifmvo.togetherad.core.custom.splashSkip

import android.content.Context
import android.view.View
import android.widget.TextView
import com.ifmvo.togetherad.core.R

/*
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-17.
 */
class Simple2SplashSkipView : BaseSplashSkipView() {

    private lateinit var tvTime: TextView

    override fun onCreateSkipView(context: Context): View {
        val skipView = View.inflate(context, R.layout.layout_splash_skip_view_simple2, null)
        tvTime = skipView.findViewById(R.id.time)
        return skipView
    }

    override fun handleTime(second: Int) {
        tvTime.text = second.toString()
    }

}