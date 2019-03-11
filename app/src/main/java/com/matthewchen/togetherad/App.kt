package com.matthewchen.togetherad

import android.app.Application
import com.matthewchen.togetherad.config.TogetherAdConst
import com.matthewchen.togetherad.utils.Kits
import com.rumtel.ad.TogetherAd

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew_Chen on 2018/12/26.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val baiduIdMap = mutableMapOf<String, String?>(
            TogetherAdConst.AD_SPLASH to "2543740",
            TogetherAdConst.AD_INTER to "2543741",
            TogetherAdConst.AD_FLOW_INDEX to "2715031",
            TogetherAdConst.AD_TIEPIAN_LIVE to "5985131"
        )
        TogetherAd.initBaiduAd(applicationContext, "ee93e58e", baiduIdMap)

        val gdtIdMap = mutableMapOf<String, String?>(
            TogetherAdConst.AD_SPLASH to "8030228893573270",
            TogetherAdConst.AD_INTER to "4090620883979242",
            TogetherAdConst.AD_FLOW_INDEX to "4010231735332811",
            TogetherAdConst.AD_TIEPIAN_LIVE to "4060449650093530"
        )
        TogetherAd.initGDTAd(applicationContext, "1106572734", gdtIdMap)

        val xunFeiIdMap = mutableMapOf<String, String?>(
            TogetherAdConst.AD_SPLASH to "FD0AC8FDE5CE0B317A6C4077E68D34CC",
            TogetherAdConst.AD_INTER to "6FD44C667D5EFD97730CC1E3F174D965",
            TogetherAdConst.AD_FLOW_INDEX to "EE2009111A1DF0BCA9DAD3723A95602F",
            TogetherAdConst.AD_TIEPIAN_LIVE to "93D157AAFA8B7EF64165B1F0ECEE2623"
        )
        TogetherAd.initXunFeiAd(applicationContext, xunFeiIdMap)
        TogetherAd.setPreMoiveMarginTopSize(Kits.StatuBar.getStatusBarHeight(this))
    }
}