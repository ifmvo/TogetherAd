package com.ifmvo.togetherad.core.listener

import org.jetbrains.annotations.NotNull


/**
 *  通用的监听器
 * 
 * Created by Matthew Chen on 2020-04-03.
 */
interface SplashListener : BaseListener {

    fun onAdLoaded(@NotNull providerType: String) {}

    fun onAdClicked(@NotNull providerType: String) {}

    fun onAdExposure(@NotNull providerType: String) {}

    fun onAdDismissed(@NotNull providerType: String) {}

}