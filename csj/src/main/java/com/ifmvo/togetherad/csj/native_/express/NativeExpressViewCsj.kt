package com.ifmvo.togetherad.csj.native_.express

import android.view.ViewGroup
import com.bytedance.sdk.openadsdk.TTNativeExpressAd
import com.ifmvo.togetherad.core.custom.express.BaseNativeExpressView

/**
 * Created by Matthew Chen on 2020/11/25.
 */
class NativeExpressViewCsj : BaseNativeExpressView() {

    override fun showNativeExpress(adProviderType: String, adObject: Any, container: ViewGroup) {
        if (adObject !is TTNativeExpressAd) return
        adObject.render()
        val parent = adObject.expressAdView?.parent
        if (parent is ViewGroup) {
            parent.removeAllViews()
        }
        container.addView(adObject.expressAdView)
    }

}