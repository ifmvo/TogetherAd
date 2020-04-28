package com.ifmvo.togetherad.core.listener

import androidx.annotation.NonNull
import com.ifmvo.togetherad.core._enum.AdProviderType

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-22.
 */
interface RewardListener {

    fun onAdStartRequest(@NonNull providerType: AdProviderType) {}

    fun onAdFailed(@NonNull providerType: AdProviderType, failedMsg: String?) {}

    fun onAdFailedAll(@NonNull failedMsg: String?) {}

    fun onAdClicked(@NonNull providerType: AdProviderType) {}

    fun onAdShow(@NonNull providerType: AdProviderType) {}

    fun onAdLoaded(@NonNull providerType: AdProviderType) {}

}