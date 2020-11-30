package com.ifmvo.togetherad.csj.native_.express

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAdDislike
import com.bytedance.sdk.openadsdk.TTAppDownloadListener
import com.bytedance.sdk.openadsdk.TTNativeExpressAd
import com.ifmvo.togetherad.core.custom.express2.BaseNativeExpress2View
import com.ifmvo.togetherad.core.listener.NativeExpress2ViewListener

/**
 * Created by Matthew Chen on 2020/11/25.
 */
class NativeExpress2ViewCsj : BaseNativeExpress2View() {

    override fun showNativeExpress2(activity: Activity, adProviderType: String, adObject: Any, container: ViewGroup, listener: NativeExpress2ViewListener?) {
        if (adObject !is TTNativeExpressAd) return

        container.removeAllViews()

        adObject.setExpressInteractionListener(object : TTNativeExpressAd.ExpressAdInteractionListener {
            override fun onAdClicked(view: View?, type: Int) {
                listener?.onAdClicked(adProviderType)
            }

            override fun onAdShow(view: View?, type: Int) {
                listener?.onAdExposed(adProviderType)
            }

            override fun onRenderSuccess(view: View?, width: Float, height: Float) {
                listener?.onAdRenderSuccess(adProviderType)
            }

            override fun onRenderFail(view: View?, errorMsg: String?, errorCode: Int) {
                listener?.onAdRenderFailed(adProviderType)
            }
        })

        adObject.setDislikeCallback(activity, object : TTAdDislike.DislikeInteractionCallback {
            override fun onSelected(position: Int, value: String) {
                container.removeAllViews()
                listener?.onAdClose(adProviderType)
            }

            override fun onCancel() {}
            override fun onRefuse() {}
        })

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
        container.addView(adObject.expressAdView)
    }

}