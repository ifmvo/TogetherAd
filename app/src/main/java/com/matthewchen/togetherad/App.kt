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
        baiduIdMap[TogetherAdConst.AD_SPLASH] = "5873732"
        baiduIdMap[TogetherAdConst.AD_INTER] = "5873733"
        baiduIdMap[TogetherAdConst.AD_FLOW_INDEX] = "5873735"
        baiduIdMap[TogetherAdConst.AD_TIEPIAN_LIVE] = "5873734"
        TogetherAd.initBaiduAd(applicationContext, "cd3d8b16", baiduIdMap)

        val gdtIdMap = HashMap<String, String>()
        gdtIdMap[TogetherAdConst.AD_SPLASH] = "5070550501041614"
        gdtIdMap[TogetherAdConst.AD_INTER] = "7000758820077828"
        gdtIdMap[TogetherAdConst.AD_FLOW_INDEX] = "5060356551943607"
        gdtIdMap[TogetherAdConst.AD_TIEPIAN_LIVE] = "6040255571748678"
        TogetherAd.initGDTAd(applicationContext, "1105965856", gdtIdMap)

        val csjIdMap = mutableMapOf(
            TogetherAdConst.AD_SPLASH to "820413685"
        )
        TogetherAd.initCsjAd(applicationContext, "5020413", getString(R.string.app_name), csjIdMap)

        TogetherAd.setAdTimeOutMillis(5000)
    }
}