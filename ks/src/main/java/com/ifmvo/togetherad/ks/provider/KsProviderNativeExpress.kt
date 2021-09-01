package com.ifmvo.togetherad.ks.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.NativeExpressListener

abstract class KsProviderNativeExpress : KsProviderNative() {

    override fun getNativeExpressAdList(activity: Activity, adProviderType: String, alias: String, adCount: Int, listener: NativeExpressListener) {
        
    }

    override fun destroyNativeExpressAd(adObject: Any) {
        
    }

    override fun nativeExpressAdIsBelongTheProvider(adObject: Any): Boolean {
        return false
    }
}