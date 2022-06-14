package com.ifmvo.togetherad.ks.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.FullVideoListener
import com.ifmvo.togetherad.ks.R
import com.ifmvo.togetherad.ks.TogetherAdKs
import com.kwad.sdk.api.*

abstract class KsProviderFullVideo : KsProviderBanner() {

    private var fullScreenVideoAd: KsFullScreenVideoAd? = null

    override fun requestFullVideoAd(activity: Activity, adProviderType: String, alias: String, listener: FullVideoListener) {
        callbackFullVideoStartRequest(adProviderType, alias, listener)

        if (TogetherAdKs.adRequestManager == null) {
            callbackFullVideoFailed(adProviderType, alias, listener, null, activity.getString(R.string.ks_init_failed))
            return
        }

        val ksScene = KsScene.Builder(TogetherAdKs.idMapKs[alias] ?: 0).build()

        TogetherAdKs.adRequestManager!!.loadFullScreenVideoAd(ksScene, object : KsLoadManager.FullScreenVideoAdListener {

            override fun onFullScreenVideoResult(adList: MutableList<KsFullScreenVideoAd>?) {}

            override fun onFullScreenVideoAdLoad(adList: MutableList<KsFullScreenVideoAd>?) {
                if (adList.isNullOrEmpty()) {
                    callbackFullVideoFailed(adProviderType, alias, listener, null, activity.getString(R.string.ks_ad_null))
                    return
                }

                fullScreenVideoAd = adList[0]

                if (fullScreenVideoAd == null) {
                    callbackFullVideoFailed(adProviderType, alias, listener, null, activity.getString(R.string.ks_ad_null))
                    return
                }

                callbackFullVideoLoaded(adProviderType, alias, listener)

                fullScreenVideoAd?.setFullScreenVideoAdInteractionListener(object : KsFullScreenVideoAd.FullScreenVideoAdInteractionListener {
                    override fun onSkippedVideo() {}

                    override fun onAdClicked() {
                        callbackFullVideoClicked(adProviderType, listener)
                    }

                    override fun onPageDismiss() {
                        fullScreenVideoAd = null
                        callbackFullVideoClosed(adProviderType, listener)
                    }

                    override fun onVideoPlayError(errorCode: Int, extra: Int) {
                        callbackFullVideoFailed(adProviderType, alias, listener, errorCode, extra.toString())
                    }

                    override fun onVideoPlayEnd() {
                        callbackFullVideoComplete(adProviderType, listener)
                    }

                    override fun onVideoPlayStart() {
                        callbackFullVideoShow(adProviderType, listener)
                    }
                })
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                callbackFullVideoFailed(adProviderType, alias, listener, errorCode, errorMsg)
            }
        })
    }

    override fun showFullVideoAd(activity: Activity): Boolean {
        if (fullScreenVideoAd?.isAdEnable == true) {
            val config = KsVideoPlayConfig.Builder()
                    .showLandscape(KsProvider.FullVideo.isShowLandscape)
                    .build()
            fullScreenVideoAd!!.showFullScreenVideoAd(activity, config)
        }
        return false
    }
}