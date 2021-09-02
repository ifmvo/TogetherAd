package com.ifmvo.togetherad.ks.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.InterListener
import com.ifmvo.togetherad.ks.R
import com.ifmvo.togetherad.ks.TogetherAdKs
import com.kwad.sdk.api.*

abstract class KsProviderInter : KsProviderFullVideo() {

    private var interstitialAd: KsInterstitialAd? = null

    override fun requestInterAd(activity: Activity, adProviderType: String, alias: String, listener: InterListener) {
        callbackInterStartRequest(adProviderType, alias, listener)

        if (TogetherAdKs.adRequestManager == null) {
            callbackInterFailed(adProviderType, alias, listener, null, activity.getString(R.string.ks_init_failed))
            return
        }

        val ksScene = KsScene.Builder(TogetherAdKs.idMapKs[alias] ?: 0).build()

        TogetherAdKs.adRequestManager!!.loadInterstitialAd(ksScene, object : KsLoadManager.InterstitialAdListener {
            override fun onRequestResult(adNumber: Int) {}

            override fun onError(errorCode: Int, errorMsg: String?) {
                callbackInterFailed(adProviderType, alias, listener, errorCode, errorMsg)
            }

            override fun onInterstitialAdLoad(adList: MutableList<KsInterstitialAd>?) {
                if (adList.isNullOrEmpty()) {
                    callbackInterFailed(adProviderType, alias, listener, null, activity.getString(R.string.ks_ad_null))
                    return
                }

                interstitialAd = adList[0]

                if (interstitialAd == null) {
                    callbackInterFailed(adProviderType, alias, listener, null, activity.getString(R.string.ks_ad_null))
                    return
                }

                callbackInterLoaded(adProviderType, alias, listener)

                interstitialAd?.setAdInteractionListener(object : KsInterstitialAd.AdInteractionListener {

                    override fun onAdClicked() {
                        callbackInterClicked(adProviderType, listener)
                    }

                    override fun onPageDismiss() {
                        interstitialAd = null
                        callbackInterClosed(adProviderType, listener)
                    }

                    override fun onVideoPlayError(errorCode: Int, extra: Int) {
                        callbackInterFailed(adProviderType, alias, listener, errorCode, extra.toString())
                    }

                    override fun onSkippedAd() {}

                    override fun onAdClosed() {}

                    override fun onVideoPlayEnd() {}

                    override fun onVideoPlayStart() {}

                    override fun onAdShow() {
                        callbackInterExpose(adProviderType, listener)
                    }
                })
            }
        })
    }

    override fun showInterAd(activity: Activity) {
        val config = KsVideoPlayConfig.Builder()
                .showLandscape(KsProvider.FullVideo.isShowLandscape)
                .build()
        interstitialAd!!.showInterstitialAd(activity, config)
    }

    override fun destroyInterAd() {
        interstitialAd = null
    }
}