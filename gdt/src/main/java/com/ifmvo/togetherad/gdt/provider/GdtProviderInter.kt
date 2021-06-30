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
abstract class GdtProviderInter : GdtProviderFullVideo() {

    private var interAd: UnifiedInterstitialAD? = null
    override fun requestInterAd(activity: Activity, adProviderType: String, alias: String, listener: InterListener) {

        callbackInterStartRequest(adProviderType, alias, listener)

        destroyInterAd()

        interAd = UnifiedInterstitialAD(activity, TogetherAdGdt.idMapGDT[alias], object : UnifiedInterstitialADListener {
            override fun onADExposure() {
                callbackInterExpose(adProviderType, listener)
            }

            override fun onVideoCached() {
                "onVideoCached".logi(tag)
            }

            override fun onADOpened() {
                "onADOpened".logi(tag)
            }

            override fun onRenderFail() {

            }

            override fun onADClosed() {
                callbackInterClosed(adProviderType, listener)
            }

            override fun onADLeftApplication() {
                "onADLeftApplication".logi(tag)
            }

            override fun onADReceive() {
                TogetherAdGdt.downloadConfirmListener?.let {
                    interAd?.setDownloadConfirmListener(it)
                }
                callbackInterLoaded(adProviderType, alias, listener)
            }

            override fun onNoAD(adError: AdError?) {
                callbackInterFailed(adProviderType, alias, listener, adError?.errorCode, adError?.errorMsg)
            }

            override fun onADClicked() {
                callbackInterClicked(adProviderType, listener)
            }

            override fun onRenderSuccess() {
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