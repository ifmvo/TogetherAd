package com.ifmvo.togetherad.mg.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.NativeExpressListener
import com.ifmvo.togetherad.mg.R

abstract class MgProviderNativeExpress : MgProviderNative() {

    override fun getNativeExpressAdList(activity: Activity, adProviderType: String, alias: String, adCount: Int, listener: NativeExpressListener) {
        callbackNativeExpressStartRequest(adProviderType, alias, listener)
        callbackNativeExpressFailed(adProviderType, alias, listener, null, activity.getString(R.string.mg_can_not))
    }

    override fun destroyNativeExpressAd(adObject: Any) {
        
    }

    override fun nativeExpressAdIsBelongTheProvider(adObject: Any): Boolean {
        return false
    }
}