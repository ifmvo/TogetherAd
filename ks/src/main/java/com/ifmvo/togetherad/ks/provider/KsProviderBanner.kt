package com.ifmvo.togetherad.ks.provider

import android.app.Activity
import android.view.ViewGroup
import com.ifmvo.togetherad.core.listener.BannerListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider

abstract class KsProviderBanner : BaseAdProvider() {

    override fun showBannerAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: BannerListener) {
        
    }

    override fun destroyBannerAd() {
        
    }
}