package com.ifmvo.togetherad.gdt.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.FullVideoListener
import com.ifmvo.togetherad.core.utils.logd
import com.ifmvo.togetherad.gdt.TogetherAdGdt
import com.qq.e.ads.cfg.VideoOption
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener
import com.qq.e.ads.interstitial2.UnifiedInterstitialMediaListener
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
                "onADExposure".logd(tag)
                callbackFullVideoShow(adProviderType, listener)
            }

            override fun onVideoCached() {
                "onVideoCached".logd(tag)
                callbackFullVideoCached(adProviderType, listener)
            }

            override fun onADOpened() {
                "onADOpened".logd(tag)
            }
            override fun onRenderFail() {
                "onRenderFail".logd(tag)
            }

            override fun onADClosed() {
                "onADClosed".logd(tag)
                callbackFullVideoClosed(adProviderType, listener)
            }

            override fun onADLeftApplication() {}

            override fun onADReceive() {
                "onADReceive".logd(tag)
                TogetherAdGdt.downloadConfirmListener?.let {
                    fullVideoAd?.setDownloadConfirmListener(it)
                }
                callbackFullVideoLoaded(adProviderType, alias, listener)
            }

            override fun onNoAD(adError: AdError?) {
                "onNoAD".logd(tag)
                callbackFullVideoFailed(adProviderType, alias, listener, adError?.errorCode, adError?.errorMsg)
            }

            override fun onADClicked() {
                "onADClicked".logd(tag)
                callbackFullVideoClicked(adProviderType, listener)
            }

            override fun onRenderSuccess() {
                "onRenderSuccess".logd(tag)
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
        fullVideoAd?.setMediaListener(object :UnifiedInterstitialMediaListener {
            override fun onVideoPageOpen() {}
            override fun onVideoLoading() {}
            override fun onVideoReady(p0: Long) {}
            override fun onVideoInit() {}
            override fun onVideoPause() {}
            override fun onVideoPageClose() {}
            override fun onVideoStart() {}
            override fun onVideoComplete() {
                callbackFullVideoComplete(adProviderType, listener)
            }
            override fun onVideoError(adError: AdError?) {}
        })
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