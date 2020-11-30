package com.ifmvo.togetherad.core.listener

import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable


/**
 * Created by Matthew Chen on 2020-04-20.
 */
interface NativeExpressListener : NativeExpress2Listener {

    /**
     * 广告被点击了
     */
    fun onAdClicked(@NotNull providerType: String, @Nullable adObject: Any?) {}

    /**
     * 广告展示了
     */
    fun onAdShow(@NotNull providerType: String, @Nullable adObject: Any?) {}

    /**
     * 广告模板渲染成功
     */
    fun onAdRenderSuccess(@NotNull providerType: String, @Nullable adObject: Any?) {}

    /**
     * 广告模板渲染失败了
     */
    fun onAdRenderFail(@NotNull providerType: String, @Nullable adObject: Any?) {}

    /**
     * 广告模板被关闭了
     */
    fun onAdClosed(@NotNull providerType: String, @Nullable adObject: Any?) {}
}