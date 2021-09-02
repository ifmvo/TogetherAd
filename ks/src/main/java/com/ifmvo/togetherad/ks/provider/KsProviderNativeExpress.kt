package com.ifmvo.togetherad.ks.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.NativeExpressListener
import com.ifmvo.togetherad.ks.R

abstract class KsProviderNativeExpress : KsProviderNative() {

    override fun getNativeExpressAdList(activity: Activity, adProviderType: String, alias: String, adCount: Int, listener: NativeExpressListener) {
        callbackNativeExpressStartRequest(adProviderType, alias, listener)
        callbackNativeExpressFailed(adProviderType, alias, listener, null, activity.getString(R.string.ks_can_not))
    }

    override fun destroyNativeExpressAd(adObject: Any) {
        
    }

    override fun nativeExpressAdIsBelongTheProvider(adObject: Any): Boolean {
        return false
    }
}