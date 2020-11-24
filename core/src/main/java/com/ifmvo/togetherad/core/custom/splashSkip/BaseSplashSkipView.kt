package com.ifmvo.togetherad.core.custom.splashSkip

import android.content.Context
import android.view.View
import android.view.ViewGroup

/* 
 * 自定义跳过按钮的基类
 *
 * Created by Matthew Chen on 2020-04-17.
 */
abstract class BaseSplashSkipView {

    /**
     * 创建跳过按钮的布局
     */
    abstract fun onCreateSkipView(context: Context): View

    /**
     * 处理倒计时的展示，单位：秒
     */
    open fun handleTime(second: Int) {
    }

    /**
     * 获取布局参数，控制跳过按钮的位置
     */
    abstract fun getLayoutParams(): ViewGroup.LayoutParams


}