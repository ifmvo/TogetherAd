package com.ifmvo.togetherad.gdt.native_.express2

import android.app.Activity
import android.view.ViewGroup
import com.ifmvo.togetherad.core.custom.express2.BaseNativeExpress2View
import com.ifmvo.togetherad.core.listener.NativeExpress2ViewListener
import com.qq.e.ads.nativ.express2.AdEventListener
import com.qq.e.ads.nativ.express2.MediaEventListener
import com.qq.e.ads.nativ.express2.NativeExpressADData2

/**
 * Created by Matthew Chen on 2020/11/25.
 */
class NativeExpress2ViewGdt : BaseNativeExpress2View() {

    override fun showNativeExpress2(activity: Activity, adProviderType: String, adObject: Any, container: ViewGroup, listener: NativeExpress2ViewListener?) {

        if (adObject !is NativeExpressADData2) return

        container.removeAllViews()

        adObject.setAdEventListener(object : AdEventListener {
            override fun onClick() {
                listener?.onAdClicked(adProviderType)
            }

            override fun onExposed() {
                listener?.onAdExposed(adProviderType)
            }

            override fun onRenderSuccess() {
                if (adObject.adView != null) {
                    val parent = adObject.adView?.parent
                    if (parent is ViewGroup) {
                        parent.removeAllViews()
                    }
                    container.addView(adObject.adView)
                    listener?.onAdRenderSuccess(adProviderType)
                } else {
                    listener?.onAdRenderFailed(adProviderType)
                }
            }

            override fun onAdClosed() {
                container.removeAllViews()
                adObject.destroy()
                listener?.onAdClose(adProviderType)
            }

            override fun onRenderFail() {
                listener?.onAdRenderFailed(adProviderType)
            }
        })

        adObject.setMediaListener(object : MediaEventListener {
            override fun onVideoCache() {}
            override fun onVideoPause() {}
            override fun onVideoStart() {}
            override fun onVideoComplete() {}
            override fun onVideoError() {}
            override fun onVideoResume() {}
        })

        adObject.render()

    }
}