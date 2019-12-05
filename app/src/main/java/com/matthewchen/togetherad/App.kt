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


        /**
         * 请查看/app/build.gradle
         *
         * TogetherAd/local.properties 中配置你的广告ID
         *
         */
//        #广点通
//        gdt_ad_id=xxxxxxxx
//        gdt_ad_splash=xxxxxxxx
//        gdt_ad_inter=xxxxxxxx
//        gdt_ad_flow_index=xxxxxxxx
//        gdt_ad_flow_tiepian_live=xxxxxxxx
//        gdt_ad_webview_banner=xxxxxxxx
//        #百度
//        baidu_ad_id=xxxxxxxx
//        baidu_ad_splash=xxxxxxxx
//        baidu_ad_inter=xxxxxxxx
//        baidu_ad_flow_index=xxxxxxxx
//        baidu_ad_flow_tiepian_live=xxxxxxxx
//        baidu_ad_webview_banner=xxxxxxxx
//        # 穿山甲
//        csj_ad_id=xxxxxxxx
//        csj_ad_splash=xxxxxxxx
//        csj_ad_inter=xxxxxxxx
//        csj_ad_flow_index=xxxxxxxx
//        csj_ad_flow_tiepian_live=xxxxxxxx
//        csj_ad_webview_banner=xxxxxxxx
        val baiduIdMap = HashMap<String, String>()
        baiduIdMap[TogetherAdConst.AD_SPLASH] = getString(R.string.baidu_ad_splash)
        baiduIdMap[TogetherAdConst.AD_INTER] = getString(R.string.baidu_ad_inter)
        baiduIdMap[TogetherAdConst.AD_FLOW_INDEX] = getString(R.string.baidu_ad_flow_index)
        baiduIdMap[TogetherAdConst.AD_TIEPIAN_LIVE] = getString(R.string.baidu_ad_flow_tiepian_live)
        baiduIdMap[TogetherAdConst.AD_WEBVIEW_BANNER] = getString(R.string.baidu_ad_webview_banner)
        TogetherAd.initBaiduAd(this, getString(R.string.baidu_ad_id), baiduIdMap)

        val gdtIdMap = HashMap<String, String>()
        gdtIdMap[TogetherAdConst.AD_SPLASH] = getString(R.string.gdt_ad_splash)
        gdtIdMap[TogetherAdConst.AD_INTER] = getString(R.string.gdt_ad_inter)
        gdtIdMap[TogetherAdConst.AD_FLOW_INDEX] = getString(R.string.gdt_ad_flow_index)
        gdtIdMap[TogetherAdConst.AD_TIEPIAN_LIVE] = getString(R.string.gdt_ad_flow_tiepian_live)
        gdtIdMap[TogetherAdConst.AD_WEBVIEW_BANNER] = getString(R.string.gdt_ad_webview_banner)
        TogetherAd.initGDTAd(this, getString(R.string.gdt_ad_id), gdtIdMap)

        val csjIdMap = HashMap<String, String>()
        csjIdMap[TogetherAdConst.AD_SPLASH] = getString(R.string.csj_ad_splash)
        csjIdMap[TogetherAdConst.AD_INTER] = getString(R.string.csj_ad_inter)
        csjIdMap[TogetherAdConst.AD_FLOW_INDEX] = getString(R.string.csj_ad_flow_index)
        csjIdMap[TogetherAdConst.AD_TIEPIAN_LIVE] = getString(R.string.csj_ad_flow_tiepian_live)
        csjIdMap[TogetherAdConst.AD_WEBVIEW_BANNER] = getString(R.string.csj_ad_webview_banner)
        TogetherAd.initCsjAd(this, getString(R.string.csj_ad_id), this.getString(R.string.app_name), csjIdMap, useTextureView = true)

        TogetherAd.setAdTimeOutMillis(5000)
    }
}