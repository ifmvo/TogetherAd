package com.ifmvo.togetherad.demo

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.ifmvo.togetherad.baidu.TogetherAdBaidu
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.csj.TogetherAdCsj
import com.ifmvo.togetherad.gdt.TogetherAdGdt
import com.ifmvo.togetherad.mango.TogetherAdMango
import java.util.*

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-16.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val gdtIdMap = HashMap<String, String>()
        gdtIdMap[TogetherAdAlias.AD_SPLASH] = "5070550501041614"
        gdtIdMap[TogetherAdAlias.AD_INTER] = "4061006419774284"
        gdtIdMap[TogetherAdAlias.AD_FLOW_INDEX] = "6041707449579237"
        gdtIdMap[TogetherAdAlias.AD_TIEPIAN_LIVE] = "3031506499071361"
        gdtIdMap[TogetherAdAlias.AD_WEBVIEW_BANNER] = "3050767842595815"
        gdtIdMap[TogetherAdAlias.AD_BACK] = "8021700419077347"
        gdtIdMap[TogetherAdAlias.AD_MID] = "8021700419077347"
        gdtIdMap[TogetherAdAlias.AD_REWARD] = "6040295592058680"
        TogetherAdGdt.init(this, "1105965856", gdtIdMap)

        val csjIdMap = HashMap<String, String>()
        csjIdMap[TogetherAdAlias.AD_SPLASH] = "820413685"
        csjIdMap[TogetherAdAlias.AD_INTER] = "920413056"
        csjIdMap[TogetherAdAlias.AD_FLOW_INDEX] = "920413297"
        csjIdMap[TogetherAdAlias.AD_TIEPIAN_LIVE] = "920413238"
        csjIdMap[TogetherAdAlias.AD_WEBVIEW_BANNER] = "920413358"
        csjIdMap[TogetherAdAlias.AD_BACK] = "920413512"
        csjIdMap[TogetherAdAlias.AD_MID] = "920413056"
        csjIdMap[TogetherAdAlias.AD_REWARD] = "901121365"
        TogetherAdCsj.init(this, "5020413", this.getString(R.string.app_name), csjIdMap, useTextureView = true, isDebug = BuildConfig.DEBUG)

        val baiduIdMap = HashMap<String, String>()
        baiduIdMap[TogetherAdAlias.AD_SPLASH] = "6697024"
        baiduIdMap[TogetherAdAlias.AD_INTER] = "6697054"
        baiduIdMap[TogetherAdAlias.AD_FLOW_INDEX] = "6697101"
        baiduIdMap[TogetherAdAlias.AD_TIEPIAN_LIVE] = "6697116"
        baiduIdMap[TogetherAdAlias.AD_WEBVIEW_BANNER] = "6697141"
        baiduIdMap[TogetherAdAlias.AD_BACK] = "6697152"
        baiduIdMap[TogetherAdAlias.AD_MID] = "6697152"
        baiduIdMap[TogetherAdAlias.AD_REWARD] = "5925490"
        TogetherAdBaidu.init(this, "c4d4e71f", baiduIdMap)

        TogetherAdMango.init(this)

        TogetherAd.setDefaultProviderRadio("gdt:1,csj:1,baidu:1,mango:1")

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}