package com.ifmvo.togetherad.core.listener

import org.jetbrains.annotations.NotNull


/**
 *  原生自渲染广告曝光和点击的监听
 *
 * Created by Matthew Chen on 2020/5/27.
 */
interface NativeExpress2ViewListener {

    /**
     * 广告曝光了
     */
    fun onAdExposed(@NotNull providerType: String) {}

    /**
     * 广告被点击了
     */
    fun onAdClicked(@NotNull providerType: String) {}

    /**
     * 广告模板渲染成功了
     */
    fun onAdRenderSuccess(@NotNull providerType: String) {}

    /**
     * 广告模板渲染失败了
     */
    fun onAdRenderFailed(@NotNull providerType: String) {}

    /**
     * 广告模板被关闭了
     */
    fun onAdClose(@NotNull providerType: String) {}
}