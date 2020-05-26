package com.ifmvo.togetherad.core.listener

import androidx.annotation.NonNull

/**
 * Created by Matthew Chen on 2020-04-20.
 */
interface NativeListener : BaseListener {

    fun onAdStartRequest(@NonNull providerType: String) {}

    fun onAdLoaded(@NonNull providerType: String, @NonNull adList: List<Any>) {}

    fun onAdFailed(@NonNull providerType: String, failedMsg: String?) {}

    fun onAdFailedAll() {}

}