package com.ifmvo.togetherad.core.listener

import org.jetbrains.annotations.NotNull

/**
 *
 * Created by Matthew Chen on 2020/12/2.
 */
interface FullVideoListener : BaseListener {

    /**
     * 请求到了广告
     */
    fun onAdLoaded(@NotNull providerType: String) {}

    /**
     * 广告被点击了
     */
    fun onAdClicked(@NotNull providerType: String) {}

    /**
     * 广告展示了
     */
    fun onAdShow(@NotNull providerType: String) {}

    /**
     * 视频缓存完成
     */
    fun onAdVideoCached(@NotNull providerType: String) {}

    /**
     * 广告被关闭了
     */
    fun onAdClose(@NotNull providerType: String) {}

}