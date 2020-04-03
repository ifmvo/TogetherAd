package com.ifmvo.togetherad.core.listener

import androidx.annotation.NonNull
import com.ifmvo.togetherad.core._enum.AdProviderType

/* 
 * (●ﾟωﾟ●) 通用的监听器
 * 
 * Created by Matthew Chen on 2020-04-03.
 */
interface CommonListener {

    fun onStartRequest(@NonNull providerType: AdProviderType)

    fun onAdPrepared(@NonNull providerType: AdProviderType)

    fun onAdClick(@NonNull providerType: AdProviderType)

    fun onAdFailed(@NonNull failedMsg: String?)

    fun onAdDismissed(@NonNull providerType: AdProviderType)

}