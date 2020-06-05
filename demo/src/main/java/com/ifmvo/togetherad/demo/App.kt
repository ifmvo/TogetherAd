package com.ifmvo.togetherad.demo

import android.app.Application
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.ifmvo.togetherad.baidu.TogetherAdBaidu
import com.ifmvo.togetherad.csj.TogetherAdCsj
import com.ifmvo.togetherad.gdt.TogetherAdGdt

/* 
 * Created by Matthew Chen on 2020-04-16.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        /**
         * 可自行查看穿山甲的文档，自定义穿山甲的初始化配置
         * ttAdConfig 为可选参数。不传或传空的情况会使用默认的配置
         */
        //初始化穿山甲
        TogetherAdCsj.useTextureView = false
        TogetherAdCsj.titleBarTheme = TTAdConstant.TITLE_BAR_THEME_DARK
        TogetherAdCsj.allowShowNotify = true
        TogetherAdCsj.allowShowPageWhenScreenLock = true
        TogetherAdCsj.debug = true
        TogetherAdCsj.directDownloadNetworkType = TTAdConstant.NETWORK_STATE_WIFI or TTAdConstant.NETWORK_STATE_3G
        TogetherAdCsj.supportMultiProcess = false
//        TogetherAdCsj.httpStack = MyOkStack3()
        TogetherAdCsj.init(context = this, adProviderType = AdProviderType.CSJ.type, csjAdAppId = "5001121", appName = this.getString(R.string.app_name), csjIdMap = mapOf(
                TogetherAdAlias.AD_SPLASH to "801121648",
                TogetherAdAlias.AD_NATIVE_SIMPLE to "901121737",
                TogetherAdAlias.AD_NATIVE_RECYCLERVIEW to "901121737",
                TogetherAdAlias.AD_BANNER to "901121246",
                TogetherAdAlias.AD_REWARD to "901121365"
        ))

        //初始化广点通
        TogetherAdGdt.init(context = this, adProviderType = AdProviderType.GDT.type, gdtAdAppId = "1101152570", gdtIdMap = mapOf(
                TogetherAdAlias.AD_SPLASH to "8863364436303842593",
                TogetherAdAlias.AD_NATIVE_SIMPLE to "6040749702835933",
                TogetherAdAlias.AD_NATIVE_RECYCLERVIEW to "6040749702835933",
                TogetherAdAlias.AD_BANNER to "4080052898050840",
                TogetherAdAlias.AD_REWARD to "2090845242931421"
        ))

        //初始化百青藤
        TogetherAdBaidu.init(context = this, adProviderType = AdProviderType.BAIDU.type, baiduAdAppId = "e866cfb0", baiduIdMap = mapOf(
                TogetherAdAlias.AD_SPLASH to "2058622",
                TogetherAdAlias.AD_NATIVE_SIMPLE to "2058628",
                TogetherAdAlias.AD_NATIVE_RECYCLERVIEW to "2058628",
                TogetherAdAlias.AD_BANNER to "2015351",
                TogetherAdAlias.AD_REWARD to "5925490"
        ))

        /**
         * 配置全局的广告商权重。
         * 如果在调用具体广告API时没有配置单次请求的权重，就会默认使用这个全局的权重
         * 如果不配置，TogetherAd会默认所有初始化的广告商权重相同
         *
         * 也可以在请求广告前设置，实时生效
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