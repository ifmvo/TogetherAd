package com.ifmvo.togetherad.gdt.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.NativeExpressListener

/**
 *
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class GdtProviderNativeExpress : GdtProviderNative() {

    override fun getNativeExpressAdList(activity: Activity, adProviderType: String, alias: String, maxCount: Int, listener: NativeExpressListener) {

    }

    override fun resumeNativeExpressAd(adObject: Any) {

    }

    override fun pauseNativeExpressAd(adObject: Any) {

    }

    override fun destroyNativeExpressAd(adObject: Any) {

    }

    override fun nativeExpressAdIsBelongTheProvider(adObject: Any): Boolean {
        return false
    }
}