package com.ifmvo.togetherad.core.listener

import org.jetbrains.annotations.NotNull

/**
 * 插屏的回调
 *
 * Created by Matthew Chen on 2020/7/3.
 */
interface InterListener : BaseListener {

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
    fun onAdExpose(@NotNull providerType: String) {}

    /**
     * 广告被关闭了
     */
    fun onAdClose(@NotNull providerType: String) {}

}