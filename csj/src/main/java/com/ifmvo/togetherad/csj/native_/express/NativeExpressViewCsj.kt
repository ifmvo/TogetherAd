package com.ifmvo.togetherad.csj.native_.express

import android.view.ViewGroup
import com.bytedance.sdk.openadsdk.TTNativeExpressAd
import com.ifmvo.togetherad.core.custom.flow.BaseNativeView
import com.ifmvo.togetherad.core.listener.NativeViewListener

/**
 *
 * Created by Matthew Chen on 2020/11/25.
 */
class NativeExpressViewCsj : BaseNativeView() {

    override fun showNative(adProviderType: String, adObject: Any, container: ViewGroup, listener: NativeViewListener?) {
        if (adObject !is TTNativeExpressAd) return

    }

}