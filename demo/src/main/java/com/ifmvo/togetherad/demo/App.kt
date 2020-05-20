package com.ifmvo.togetherad.demo

import android.app.Application
import com.ifmvo.togetherad.baidu.TogetherAdBaidu
import com.ifmvo.togetherad.csj.TogetherAdCsj
import com.ifmvo.togetherad.gdt.TogetherAdGdt

/* 
 * Created by Matthew Chen on 2020-04-16.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        //初始化穿山甲
        TogetherAdCsj.init(context = this, adProviderType = AdProviderType.CSJ.type, csjAdAppId = "你的穿山甲广告的应用ID", appName = this.getString(R.string.app_name), csjIdMap = mapOf(
                TogetherAdAlias.AD_SPLASH to "你的对应广告位的ID",
                TogetherAdAlias.AD_NATIVE_SIMPLE to "你的对应广告位的ID",
                TogetherAdAlias.AD_NATIVE_RECYCLERVIEW to "你的对应广告位的ID",
                TogetherAdAlias.AD_BANNER to "你的对应广告位的ID",
                TogetherAdAlias.AD_REWARD to "你的对应广告位的ID"
        ), isDebug = BuildConfig.DEBUG)

        //初始化广点通
        TogetherAdGdt.init(context = this, adProviderType = AdProviderType.GDT.type, gdtAdAppId = "你的广点通广告的应用ID", gdtIdMap = mapOf(
                TogetherAdAlias.AD_SPLASH to "你的对应广告位的ID",
                TogetherAdAlias.AD_NATIVE_SIMPLE to "你的对应广告位的ID",
                TogetherAdAlias.AD_NATIVE_RECYCLERVIEW to "你的对应广告位的ID",
                TogetherAdAlias.AD_BANNER to "你的对应广告位的ID",
                TogetherAdAlias.AD_REWARD to "你的对应广告位的ID"
        ))

        //初始化百青藤
        TogetherAdBaidu.init(context = this, adProviderType = AdProviderType.BAIDU.type, baiduAdAppId = "你的百青藤广告的应用ID", baiduIdMap = mapOf(
                TogetherAdAlias.AD_SPLASH to "你的对应广告位的ID",
                TogetherAdAlias.AD_NATIVE_SIMPLE to "你的对应广告位的ID",
                TogetherAdAlias.AD_NATIVE_RECYCLERVIEW to "你的对应广告位的ID",
                TogetherAdAlias.AD_BANNER to "你的对应广告位的ID",
                TogetherAdAlias.AD_REWARD to "你的对应广告位的ID"
        ))

        /**
         * 配置全局的广告商权重。
         * 如果在调用具体广告API时没有配置单次请求的权重，就会默认使用这个全局的权重
         * 如果不配置，TogetherAd会默认所有初始化的广告商权重相同
         */
//        TogetherAd.setPublicProviderRadio(mapOf(
//                AdProviderType.GDT.type to 1,
//                AdProviderType.BAIDU.type to 1,
//                AdProviderType.CSJ.type to 1
//        ))

        /**
         * 自定义图片加载方式
         * 用于自渲染类型的广告图片加载
         * 如果不配置，TogetherAd 会使用默认的图片加载方式
         * 主要考虑到：开发者可以自定义实现图片加载：渐变、占位图、错误图等
         */
//        TogetherAd.setCustomImageLoader(object : AdImageLoader {
//            override fun loadImage(context: Context, imageView: ImageView, imgUrl: String) {
//                Glide.with(context).load(imgUrl).into(imageView)
//            }
//        })
    }
}