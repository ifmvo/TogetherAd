package com.ifmvo.togetherad.demo

import android.app.Application
import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ifmvo.togetherad.baidu.TogetherAdBaidu
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.custom.flow.AdImageLoader
import com.ifmvo.togetherad.csj.TogetherAdCsj
import com.ifmvo.togetherad.gdt.TogetherAdGdt

/* 
 * Created by Matthew Chen on 2020-04-16.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        TogetherAdCsj.init(this, AdProviderType.CSJ.type, "5020413", this.getString(R.string.app_name), mapOf(
                TogetherAdAlias.AD_SPLASH to "820413685",
                TogetherAdAlias.AD_NATIVE to "920413297",
                TogetherAdAlias.AD_BANNER to "920413358",
                TogetherAdAlias.AD_REWARD to "920413358"
        ), isDebug = BuildConfig.DEBUG)

        TogetherAdGdt.init(this, AdProviderType.GDT.type, "1105965856", mapOf(
                TogetherAdAlias.AD_SPLASH to "5070550501041614",
                TogetherAdAlias.AD_NATIVE to "6041707449579237",
                TogetherAdAlias.AD_BANNER to "3050767842595815",
                TogetherAdAlias.AD_REWARD to "3050767842595815"
        ))

        TogetherAdBaidu.init(this, AdProviderType.BAIDU.type, "c4d4e71f", mapOf(
                TogetherAdAlias.AD_SPLASH to "6697024",
                TogetherAdAlias.AD_NATIVE to "6697101",
                TogetherAdAlias.AD_BANNER to "6697141",
                TogetherAdAlias.AD_REWARD to "6697141"
        ))

        TogetherAd.setPublicProviderRadio(mapOf(
                AdProviderType.GDT.type to 1,
                AdProviderType.BAIDU.type to 1,
                AdProviderType.CSJ.type to 1
        ))

        TogetherAd.setCustomImageLoader(object : AdImageLoader {
            override fun loadImage(context: Context, imageView: ImageView, imgUrl: String) {
                Glide.with(context).load(imgUrl).into(imageView)
            }
        })
    }
}