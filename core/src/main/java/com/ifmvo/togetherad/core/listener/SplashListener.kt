package com.ifmvo.togetherad.core.listener

import org.jetbrains.annotations.NotNull


/**
 *  通用的监听器
 * 
 * Created by Matthew Chen on 2020-04-03.
 */
interface SplashListener : BaseListener {

    /**
     * 请求到了广告
     */
    fun onAdLoaded(@NotNull providerType: String) {}

    /**
     * 广告被点击了
     */
    fun onAdClicked(@NotNull providerType: String) {}

    /**
     * 广告曝光了
     */
    fun onAdExposure(@NotNull providerType: String) {}

    /**
     * 广告消失了（ 点击跳过或者倒计时结束 ）
     */
    fun onAdDismissed(@NotNull providerType: String) {}

}