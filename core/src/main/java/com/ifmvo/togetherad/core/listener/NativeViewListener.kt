package com.ifmvo.togetherad.core.listener

import android.support.annotation.NonNull


/**
 *  原生自渲染广告曝光和点击的监听
 *
 * Created by Matthew Chen on 2020/5/27.
 */
interface NativeViewListener {

    fun onAdExposed(@NonNull providerType: String) {}

    fun onAdClicked(@NonNull providerType: String) {}

}