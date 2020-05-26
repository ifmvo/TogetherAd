package com.ifmvo.togetherad.core.listener

import androidx.annotation.NonNull

/**
 * Created by Matthew Chen on 2020/5/25.
 */
interface BannerListener : BaseListener {

    fun onAdStartRequest(@NonNull providerType: String) {}

    fun onAdLoaded(@NonNull providerType: String) {}

    fun onAdFailed(@NonNull providerType: String, failedMsg: String?) {}

    fun onAdFailedAll(@NonNull failedMsg: String?) {}

    fun onAdClicked(@NonNull providerType: String) {}

    fun onAdShow(@NonNull providerType: String) {}

    fun onAdExpose(@NonNull providerType: String) {}

    fun onAdVideoComplete(@NonNull providerType: String) {}

    fun onAdVideoCached(@NonNull providerType: String) {}

    fun onAdRewardVerify(@NonNull providerType: String) {}

    fun onAdClose(@NonNull providerType: String) {}

}