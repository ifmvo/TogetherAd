package com.ifmvo.togetherad.core.listener

import android.support.annotation.NonNull

/**
 * 插屏的回调
 *
 * Created by Matthew Chen on 2020/7/3.
 */
interface InterListener {

    fun onAdStartRequest(@NonNull providerType: String) {}

    fun onAdLoaded(@NonNull providerType: String) {}

    fun onAdFailed(@NonNull providerType: String, failedMsg: String?) {}

    fun onAdFailedAll() {}

    fun onAdClicked(@NonNull providerType: String) {}

    fun onAdExpose(@NonNull providerType: String) {}

    fun onAdClose(@NonNull providerType: String) {}

}