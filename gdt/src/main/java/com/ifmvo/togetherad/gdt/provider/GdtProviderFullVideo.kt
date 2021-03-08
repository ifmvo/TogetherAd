package com.ifmvo.togetherad.gdt.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.FullVideoListener
import com.ifmvo.togetherad.gdt.TogetherAdGdt
import com.qq.e.ads.cfg.VideoOption
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener
import com.qq.e.comm.util.AdError


/**
 * 全屏视频广告
 *
 * Created by Matthew Chen on 2020/12/2.
 */
abstract class GdtProviderFullVideo : GdtProviderBanner() {

    private var fullVideoAd: UnifiedInterstitialAD? = null

    override fun requestFullVideoAd(activity: Activity, adProviderType: String, alias: String, listener: FullVideoListener) {

        callbackFullVideoStartRequest(adProviderType, alias, listener)

        fullVideoAd = UnifiedInterstitialAD(activity, TogetherAdGdt.idMapGDT[alias], object : UnifiedInterstitialADListener {
            override fun onADExposure() {
                callbackFullVideoShow(adProviderType, listener)
            }

            override fun onVideoCached() {
                callbackFullVideoCached(adProviderType, listener)
            }

            override fun onADOpened() {}

            override fun onADClosed() {
                callbackFullVideoClosed(adProviderType, listener)
            }

            override fun onADLeftApplication() {}

            override fun onADReceive() {
                callbackFullVideoLoaded(adProviderType, alias, listener)
            }

            override fun onNoAD(adError: AdError?) {
                callbackFullVideoFailed(adProviderType, alias, listener, adError?.errorCode, adError?.errorMsg)
            }

            override fun onADClicked() {
                callbackFullVideoClicked(adProviderType, listener)
            }
        })
        val option = VideoOption.Builder()
                .setAutoPlayMuted(GdtProvider.FullVideo.autoPlayMuted)
                .setAutoPlayPolicy(GdtProvider.FullVideo.autoPlayPolicy)
                .build()
        fullVideoAd?.setVideoOption(option)
        fullVideoAd?.setVideoPlayPolicy(GdtProvider.FullVideo.videoPlayPolicy)
        fullVideoAd?.setMaxVideoDuration(GdtProvider.FullVideo.maxVideoDuration)
        fullVideoAd?.setMaxVideoDuration(GdtProvider.FullVideo.minVideoDuration)
        fullVideoAd?.loadFullScreenAD()
    }

    override fun showFullVideoAd(activity: Activity): Boolean {
        if (fullVideoAd?.isValid != true) {
            return false
        }
        fullVideoAd?.showFullScreenAD(activity)
        return true
    }
}