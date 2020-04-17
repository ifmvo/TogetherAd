package com.ifmvo.togetherad.core.custom.splashSkip

import android.content.Context
import android.view.View
import com.ifmvo.togetherad.core.R

/*
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-17.
 */
class Simple1SplashSkipView : BaseSplashSkipView() {

    override fun onCreateSkipView(context: Context): View {
        return View.inflate(context, R.layout.layout_splash_skip_view_simple1, null)
    }

    override fun handleTime(second: Int) {

    }
}