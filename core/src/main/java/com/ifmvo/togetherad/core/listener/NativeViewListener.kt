package com.ifmvo.togetherad.core.listener

import org.jetbrains.annotations.NotNull


/**
 *  原生自渲染广告曝光和点击的监听
 *
 * Created by Matthew Chen on 2020/5/27.
 */
interface NativeViewListener {

    /**
     * 广告曝光了
     */
    fun onAdExposed(@NotNull providerType: String) {}

    /**
     * 广告被点击了
     */
    fun onAdClicked(@NotNull providerType: String) {}

}