package com.ifmvo.togetherad.core.listener

import org.jetbrains.annotations.NotNull


/**
 * Created by Matthew Chen on 2020-04-22.
 */
interface RewardListener : BaseListener {

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
     * 广告曝光了（ 和 onAdShow 的区别是展示不一定曝光，曝光一定展示，需要展示一定的时间才会曝光，曝光的条件是提供商规定的 ）
     */
    fun onAdExpose(@NotNull providerType: String) {}

    /**
     * 视频广告播放完成
     */
    fun onAdVideoComplete(@NotNull providerType: String) {}

    /**
     * 视频缓存完成
     */
    fun onAdVideoCached(@NotNull providerType: String) {}

    /**
     * 奖励被验证
     */
    fun onAdRewardVerify(@NotNull providerType: String) {}

    /**
     * 广告被关闭了
     */
    fun onAdClose(@NotNull providerType: String) {}

}