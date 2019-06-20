package com.matthewchen.togetherad

import android.app.Application
import com.matthewchen.togetherad.config.TogetherAdConst
import com.rumtel.ad.TogetherAd

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew_Chen on 2018/12/26.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val baiduIdMap = mutableMapOf(
            TogetherAdConst.AD_SPLASH to "5873732",
            TogetherAdConst.AD_INTER to "5873733",
            TogetherAdConst.AD_FLOW_INDEX to "5873735",
            TogetherAdConst.AD_TIEPIAN_LIVE to "5873734",
            TogetherAdConst.AD_WEBVIEW_BANNER to "6293556"
        )
        TogetherAd.initBaiduAd(applicationContext, "cd3d8b16", baiduIdMap)

        val gdtIdMap = mutableMapOf(
            TogetherAdConst.AD_SPLASH to "5070550501041614",
            TogetherAdConst.AD_INTER to "7000758820077828",
            TogetherAdConst.AD_FLOW_INDEX to "5060356551943607",
            TogetherAdConst.AD_TIEPIAN_LIVE to "6040255571748678",
            TogetherAdConst.AD_WEBVIEW_BANNER to "3050767842595815"
        )
        TogetherAd.initGDTAd(applicationContext, "1105965856", gdtIdMap)

        val csjIdMap = mutableMapOf(
            TogetherAdConst.AD_SPLASH to "820413685",
            TogetherAdConst.AD_INTER to "920413056",
            TogetherAdConst.AD_FLOW_INDEX to "920413297",
            TogetherAdConst.AD_TIEPIAN_LIVE to "920413238",
            TogetherAdConst.AD_WEBVIEW_BANNER to "920413685"
        )
        TogetherAd.initCsjAd(applicationContext, "5020413", getString(R.string.app_name), csjIdMap)

        TogetherAd.setAdTimeOutMillis(5000)
    }
}