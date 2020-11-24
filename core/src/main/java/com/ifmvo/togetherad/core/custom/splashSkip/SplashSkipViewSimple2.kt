package com.ifmvo.togetherad.core.custom.splashSkip

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.ifmvo.togetherad.core.R

/**
 * 简单模板2
 * 提供了跳过按钮和倒计时展示，样式比广点通默认的样式要小一点
 *
 * Created by Matthew Chen on 2020-04-17.
 */
class SplashSkipViewSimple2 : BaseSplashSkipView() {

    private lateinit var tvTime: TextView

    /**
     * 创建跳过按钮的布局
     */
    override fun onCreateSkipView(context: Context): View {
        val skipView = View.inflate(context, R.layout.layout_splash_skip_view_simple2, null)
        tvTime = skipView.findViewById(R.id.time)
        return skipView
    }

    /**
     * 处理倒计时的展示，单位：秒
     */
    override fun handleTime(second: Int) {
        tvTime.text = second.toString()
    }

    /**
     * 获取布局参数，控制跳过按钮的位置
     *
     * 注意：LayoutParams 的类型取决于 请求开屏广告时 container 参数的类型。
     * Demo 中是使用的 FrameLayout，所以这里就是 FrameLayout.LayoutParams；
     * LayoutParams类型必须要一致，否则会崩溃
     */
    override fun getLayoutParams(): ViewGroup.LayoutParams {
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.END or Gravity.TOP
        layoutParams.topMargin = 50
        layoutParams.rightMargin = 30
        return layoutParams
    }

}