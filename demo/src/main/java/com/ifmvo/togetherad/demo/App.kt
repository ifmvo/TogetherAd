package com.ifmvo.togetherad.demo

import android.app.Application
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.gdt.TogetherAdGdt

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-16.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

//        TogetherAdCsj.init(this, AdProviderType.CSJ.type, "5062695", this.getString(R.string.app_name), mapOf(
//                TogetherAdAlias.AD_REWARD to "945152437"
//        ), isDebug = BuildConfig.DEBUG)
//
        TogetherAdGdt.init(this, AdProviderType.GDT.type, "1107469470", mapOf(
                TogetherAdAlias.AD_REWARD to "6051815092224372"
        ))
//
//        TogetherAdBaidu.init(this, AdProviderType.BAIDU.type, "d39ba09f", mapOf(
//                TogetherAdAlias.AD_REWARD to "7045323"
//        ))

        TogetherAd.setPublicProviderRadio(mapOf(
                AdProviderType.GDT.type to 1,
                AdProviderType.BAIDU.type to 1,
                AdProviderType.CSJ.type to 1
        ))
    }
}