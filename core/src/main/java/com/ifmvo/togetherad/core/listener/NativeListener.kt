package com.ifmvo.togetherad.core.listener

import org.jetbrains.annotations.NotNull


/**
 * Created by Matthew Chen on 2020-04-20.
 */
interface NativeListener : BaseListener {

    fun onAdLoaded(@NotNull providerType: String, @NotNull adList: List<Any>) {}

}