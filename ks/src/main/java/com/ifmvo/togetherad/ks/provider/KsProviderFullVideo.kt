package com.ifmvo.togetherad.ks.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.FullVideoListener

abstract class KsProviderFullVideo : KsProviderBanner() {

    override fun requestFullVideoAd(activity: Activity, adProviderType: String, alias: String, listener: FullVideoListener) {
        
    }

    override fun showFullVideoAd(activity: Activity): Boolean {
        return false
    }
}