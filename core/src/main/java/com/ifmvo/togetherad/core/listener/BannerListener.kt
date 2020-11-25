package com.ifmvo.togetherad.core.listener

import org.jetbrains.annotations.NotNull


/**
 * Created by Matthew Chen on 2020/5/25.
 */
interface BannerListener : BaseListener {

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