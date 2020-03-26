package com.matthewchen.togetherad

import android.app.Application
import com.matthewchen.togetherad.config.TogetherAdConst
import com.rumtel.ad.TogetherAd
import java.util.*

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew_Chen on 2018/12/26.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val baiduIdMap = HashMap<String, String>()
        baiduIdMap[TogetherAdConst.AD_SPLASH] = "6697024"
        baiduIdMap[TogetherAdConst.AD_INTER] = "6697054"
        baiduIdMap[TogetherAdConst.AD_FLOW_INDEX] = "6697101"
        baiduIdMap[TogetherAdConst.AD_TIEPIAN_LIVE] = "6697116"
        baiduIdMap[TogetherAdConst.AD_WEBVIEW_BANNER] = "6697141"
        baiduIdMap[TogetherAdConst.AD_BACK] = "6697152"
        TogetherAd.initBaiduAd(this, "c4d4e71f", baiduIdMap)

        val gdtIdMap = HashMap<String, String>()
        gdtIdMap[TogetherAdConst.AD_SPLASH] = "5070550501041614"
        gdtIdMap[TogetherAdConst.AD_INTER] = "4061006419774284"
        gdtIdMap[TogetherAdConst.AD_FLOW_INDEX] = "6041707449579237"
        gdtIdMap[TogetherAdConst.AD_TIEPIAN_LIVE] = "3031506499071361"
        gdtIdMap[TogetherAdConst.AD_BACK] = "8021700419077347"
        gdtIdMap[TogetherAdConst.AD_WEBVIEW_BANNER] = "3050767842595815"
        TogetherAd.initGDTAd(this, "1105965856", gdtIdMap)

        val csjIdMap = HashMap<String, String>()
        csjIdMap[TogetherAdConst.AD_SPLASH] = "820413685"
        csjIdMap[TogetherAdConst.AD_INTER] = "920413056"
        csjIdMap[TogetherAdConst.AD_FLOW_INDEX] = "920413297"
        csjIdMap[TogetherAdConst.AD_TIEPIAN_LIVE] = "920413238"
        csjIdMap[TogetherAdConst.AD_WEBVIEW_BANNER] = "920413358"
        csjIdMap[TogetherAdConst.AD_BACK] = "920413512"
        TogetherAd.initCsjAd(this, "5020413", this.getString(R.string.app_name), csjIdMap, useTextureView = true)

        TogetherAd.setAdTimeOutMillis(5000)
    }
}