package com.ifmvo.togetherad.core.listener

import androidx.annotation.NonNull
import com.ifmvo.togetherad.core._enum.AdProviderType

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-20.
 */
interface FlowListener : BaseListener {

    fun onAdStartRequest(@NonNull providerType: AdProviderType) {}

    fun onAdLoaded(@NonNull providerType: AdProviderType, @NonNull adList: List<*>) {}

    fun onAdFailed(@NonNull providerType: AdProviderType, failedMsg: String?) {}

    fun onAdFailedAll(@NonNull failedMsg: String?) {}

}