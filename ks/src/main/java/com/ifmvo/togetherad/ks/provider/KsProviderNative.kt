package com.ifmvo.togetherad.ks.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.NativeListener

abstract class KsProviderNative : KsProviderInter() {

    override fun getNativeAdList(activity: Activity, adProviderType: String, alias: String, maxCount: Int, listener: NativeListener) {
        
    }

    override fun nativeAdIsBelongTheProvider(adObject: Any): Boolean {
        return false
    }

    override fun resumeNativeAd(adObject: Any) {
        
    }

    override fun pauseNativeAd(adObject: Any) {
        
    }

    override fun destroyNativeAd(adObject: Any) {
        
    }
}