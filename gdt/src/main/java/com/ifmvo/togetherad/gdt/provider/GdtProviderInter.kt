package com.ifmvo.togetherad.gdt.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.InterListener
import com.ifmvo.togetherad.core.utils.logi
import com.ifmvo.togetherad.gdt.TogetherAdGdt
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener
import com.qq.e.comm.util.AdError

/**
 *
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class GdtProviderInter : GdtProviderBanner() {

    private var interAd: UnifiedInterstitialAD? = null
    override fun requestInterAd(activity: Activity, adProviderType: String, alias: String, listener: InterListener) {

        callbackInterStartRequest(adProviderType, listener)

        destroyInterAd()

        interAd = UnifiedInterstitialAD(activity, TogetherAdGdt.idMapGDT[alias], object : UnifiedInterstitialADListener {
            override fun onADExposure() {
                callbackInterExpose(adProviderType, listener)
            }

            override fun onVideoCached() {
                "onVideoCached".logi(TAG)
            }

            override fun onADOpened() {
                "onADOpened".logi(TAG)
            }

            override fun onADClosed() {
                callbackInterClosed(adProviderType, listener)
            }

            override fun onADLeftApplication() {
                "onADLeftApplication".logi(TAG)
            }

            override fun onADReceive() {
                callbackInterLoaded(adProviderType, listener)
            }

            override fun onNoAD(adError: AdError?) {
                callbackInterFailed(adProviderType, listener, "错误码: ${adError?.errorCode}, 错误信息：${adError?.errorMsg}")
            }

            override fun onADClicked() {
                callbackInterClicked(adProviderType, listener)
            }
        })
        interAd?.loadAD()
    }

    override fun showInterAd(activity: Activity) {
        interAd?.show()
    }

    override fun destroyInterAd() {
        interAd?.close()
        interAd?.destroy()
        interAd = null
    }

}