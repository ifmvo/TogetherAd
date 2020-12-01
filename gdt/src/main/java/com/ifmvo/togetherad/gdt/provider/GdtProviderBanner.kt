package com.ifmvo.togetherad.gdt.provider

import android.app.Activity
import android.view.ViewGroup
import com.ifmvo.togetherad.core.listener.BannerListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.gdt.TogetherAdGdt
import com.qq.e.ads.banner2.UnifiedBannerADListener
import com.qq.e.ads.banner2.UnifiedBannerView
import com.qq.e.comm.util.AdError

/**
 *
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class GdtProviderBanner : BaseAdProvider() {

    private var banner: UnifiedBannerView? = null
    override fun showBannerAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: BannerListener) {
        callbackBannerStartRequest(adProviderType, listener)
        destroyBannerAd()
        banner = UnifiedBannerView(activity, TogetherAdGdt.idMapGDT[alias], object : UnifiedBannerADListener {
            override fun onADCloseOverlay() {
            }

            override fun onADExposure() {
                callbackBannerExpose(adProviderType, listener)
            }

            override fun onADClosed() {
                destroyBannerAd()
                callbackBannerClosed(adProviderType, listener)
            }

            override fun onADLeftApplication() {
            }

            override fun onADOpenOverlay() {
            }

            override fun onNoAD(adError: AdError?) {
                destroyBannerAd()
                callbackBannerFailed(adProviderType, listener, adError?.errorCode, adError?.errorMsg)
            }

            override fun onADReceive() {
                callbackBannerLoaded(adProviderType, listener)
            }

            override fun onADClicked() {
                callbackBannerClicked(adProviderType, listener)
            }
        })
        container.addView(banner)
        banner?.setRefresh(GdtProvider.Banner.slideIntervalTime)
        banner?.loadAD()
    }

    override fun destroyBannerAd() {
        banner?.destroy()
        banner = null
    }

}