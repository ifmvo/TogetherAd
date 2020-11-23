package com.ifmvo.togetherad.core.custom.splashSkip

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.ifmvo.togetherad.core.R

/**
 * 简单模板1
 * 没有显示倒计时，只有推过按钮
 *
 * Created by Matthew Chen on 2020-04-17.
 */
class SplashSkipViewSimple1 : BaseSplashSkipView() {

    /**
     * 创建跳过按钮的布局
     */
    override fun onCreateSkipView(context: Context): View {
        return View.inflate(context, R.layout.layout_splash_skip_view_simple1, null)
    }

}