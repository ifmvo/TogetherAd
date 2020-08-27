package com.ifmvo.togetherad.core.listener

import org.jetbrains.annotations.NotNull


/**
 *  原生自渲染广告曝光和点击的监听
 *
 * Created by Matthew Chen on 2020/5/27.
 */
interface NativeViewListener {

    fun onAdExposed(@NotNull providerType: String) {}

    fun onAdClicked(@NotNull providerType: String) {}

}