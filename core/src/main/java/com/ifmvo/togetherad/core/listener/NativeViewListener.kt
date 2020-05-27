package com.ifmvo.togetherad.core.listener

import androidx.annotation.NonNull

/**
 *  原生自渲染广告曝光和点击的监听
 *
 * Created by Matthew Chen on 2020/5/27.
 */
interface NativeViewListener : BaseListener {

    fun onAdExposed(@NonNull providerType: String) {}

    fun onAdClicked(@NonNull providerType: String) {}

}