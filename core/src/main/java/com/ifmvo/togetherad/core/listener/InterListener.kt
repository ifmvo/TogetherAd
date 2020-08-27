package com.ifmvo.togetherad.core.listener

import org.jetbrains.annotations.NotNull

/**
 * 插屏的回调
 *
 * Created by Matthew Chen on 2020/7/3.
 */
interface InterListener : BaseListener {

    fun onAdLoaded(@NotNull providerType: String) {}

    fun onAdClicked(@NotNull providerType: String) {}

    fun onAdExpose(@NotNull providerType: String) {}

    fun onAdClose(@NotNull providerType: String) {}

}