package com.ifmvo.togetherad.core.listener

import android.support.annotation.NonNull


/**
 * Created by Matthew Chen on 2020/5/25.
 */
interface BannerListener : BaseListener {

    fun onAdLoaded(@NonNull providerType: String) {}

    fun onAdFailed(@NonNull providerType: String, failedMsg: String?) {}

    fun onAdClicked(@NonNull providerType: String) {}

    fun onAdExpose(@NonNull providerType: String) {}

    fun onAdClose(@NonNull providerType: String) {}

}