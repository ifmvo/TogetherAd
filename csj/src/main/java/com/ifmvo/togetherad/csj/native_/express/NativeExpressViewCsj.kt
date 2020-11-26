package com.ifmvo.togetherad.csj.native_.express

import android.view.ViewGroup
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAppDownloadListener
import com.bytedance.sdk.openadsdk.TTNativeExpressAd
import com.ifmvo.togetherad.core.custom.flow.BaseNativeView
import com.ifmvo.togetherad.core.listener.NativeViewListener

/**
 * Created by Matthew Chen on 2020/11/25.
 */
class NativeExpressViewCsj : BaseNativeView() {

    override fun showNative(adProviderType: String, adObject: Any, container: ViewGroup, listener: NativeViewListener?) {
        if (adObject !is TTNativeExpressAd) return

        if (adObject.interactionType == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            adObject.setDownloadListener(object : TTAppDownloadListener {
                override fun onIdle() {}
                override fun onDownloadPaused(p0: Long, p1: Long, p2: String?, p3: String?) {}
                override fun onDownloadFailed(p0: Long, p1: Long, p2: String?, p3: String?) {}
                override fun onDownloadActive(p0: Long, p1: Long, p2: String?, p3: String?) {}
                override fun onDownloadFinished(p0: Long, p1: String?, p2: String?) {}
                override fun onInstalled(p0: String?, p1: String?) {}
            })
        }
        adObject.render()
    }
}