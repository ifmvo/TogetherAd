package com.ifmvo.togetherad.ks.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.InterListener

abstract class KsProviderInter : KsProviderFullVideo() {

    override fun requestInterAd(activity: Activity, adProviderType: String, alias: String, listener: InterListener) {
        
    }

    override fun showInterAd(activity: Activity) {
        
    }

    override fun destroyInterAd() {
        
    }
}