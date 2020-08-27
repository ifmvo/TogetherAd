package com.ifmvo.togetherad.core.listener

import org.jetbrains.annotations.NotNull


/**
 * Created by Matthew Chen on 2020/5/25.
 */
interface BannerListener : BaseListener {

    fun onAdLoaded(@NotNull providerType: String) {}

    fun onAdClicked(@NotNull providerType: String) {}

    fun onAdExpose(@NotNull providerType: String) {}

    fun onAdClose(@NotNull providerType: String) {}

}