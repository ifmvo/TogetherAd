package com.ifmvo.togetherad.core.listener

import org.jetbrains.annotations.NotNull

/**
 *
 * Created by Matthew Chen on 2020/12/2.
 */
interface AllAdListener {

    /**
     * 开始请求前回调
     */
    fun onAdStartRequest(@NotNull providerType: String, @NotNull alias: String) {}

    /**
     * 单个提供商请求失败
     */
    fun onAdFailed(@NotNull providerType: String, @NotNull alias: String, failedMsg: String?) {}

    /**
     * 请求到了广告
     */
    fun onAdLoaded(@NotNull providerType: String, @NotNull alias: String) {}

}