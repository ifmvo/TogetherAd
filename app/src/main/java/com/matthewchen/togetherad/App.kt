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
            TogetherAdConst.AD_SPLASH to "2543740",
            TogetherAdConst.AD_INTER to "2543741",
            TogetherAdConst.AD_FLOW_INDEX to "3105303",
            TogetherAdConst.AD_TIEPIAN_LIVE to "5985131",
            TogetherAdConst.AD_WEBVIEW_BANNER to "6293234"
        )
        TogetherAd.initBaiduAd(this, "ee93e58e", baiduIdMap)

        val gdtIdMap = mutableMapOf(
            TogetherAdConst.AD_SPLASH to "4020810160602330",
            TogetherAdConst.AD_INTER to "1000916140309269",
            TogetherAdConst.AD_FLOW_INDEX to "9070650580643423",
            TogetherAdConst.AD_TIEPIAN_LIVE to "9070955510849785",
            TogetherAdConst.AD_WEBVIEW_BANNER to "3050168862780763"
        )
        TogetherAd.initGDTAd(this, "1105302187", gdtIdMap)

        val csjIdMap = mutableMapOf(
            TogetherAdConst.AD_SPLASH to "819667807",
            TogetherAdConst.AD_INTER to "919667304",
            TogetherAdConst.AD_FLOW_INDEX to "919667579",
            TogetherAdConst.AD_TIEPIAN_LIVE to "919667787",
            TogetherAdConst.AD_WEBVIEW_BANNER to "919667619"
        )
        TogetherAd.initCsjAd(this, "5019667", getString(R.string.app_name), csjIdMap)

        TogetherAd.setAdTimeOutMillis(5000)
    }
}