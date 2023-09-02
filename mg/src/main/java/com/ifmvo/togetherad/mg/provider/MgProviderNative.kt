package com.ifmvo.togetherad.mg.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.mg.R

abstract class MgProviderNative : MgProviderInter() {

    override fun getNativeAdList(activity: Activity, adProviderType: String, alias: String, maxCount: Int, listener: NativeListener) {
        callbackNativeStartRequest(adProviderType, alias, listener)
        callbackNativeFailed(adProviderType, alias, listener, null, activity.getString(R.string.mg_can_not))
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