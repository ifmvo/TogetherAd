package com.ifmvo.togetherad.demo.app

import com.ifmvo.togetherad.baidu.TogetherAdBaidu
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.csj.TogetherAdCsj
import com.ifmvo.togetherad.demo.BuildConfig
import com.ifmvo.togetherad.demo.R
import com.ifmvo.togetherad.gdt.TogetherAdGdt

/*
 * Created by Matthew Chen on 2020-04-16.
 */
class App : ActLifecycleAppBase() {

    override fun onCreate() {
        super.onCreate()

        /**
         * 自定义穿山甲的初始化配置
         * 可自行选择自定义穿山甲的配置，不配置就会使用穿山甲的默认值
         */
//        // 可选参数，需在初始化之前，使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
//        TogetherAdCsj.useTextureView = false
//        // 可选参数，需在初始化之前，标题栏的主题色
//        TogetherAdCsj.titleBarTheme = TTAdConstant.TITLE_BAR_THEME_DARK
//        // 可选参数，需在初始化之前，是否允许sdk展示通知栏提示
//        TogetherAdCsj.allowShowNotify = true
//        // 可选参数，需在初始化之前，测试阶段打开，可以通过日志排查问题，上线时去除该调用
//        TogetherAdCsj.debug = true
//        // 可选参数，需在初始化之前，允许直接下载的网络状态集合
//        TogetherAdCsj.directDownloadNetworkType = TTAdConstant.NETWORK_STATE_WIFI or TTAdConstant.NETWORK_STATE_4G
//        // 可选参数，需在初始化之前，是否支持多进程，true支持
//        TogetherAdCsj.supportMultiProcess = false
//        // 可选参数，需在初始化之前，自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
//        TogetherAdCsj.httpStack = object : IHttpStack {  }
//        // 可选参数，需在初始化之前，设置是否为计费用户：true计费用户、false非计费用户。默认为false非计费用户。须征得用户同意才可传入该参数
//        TogetherAdCsj.isPaid = false
//        // 可选参数，需在初始化之前，是否一步初始化
//        TogetherAdCsj.isAsyncInit = false
//        // 可选参数，需在初始化之前，设置用户画像的关键词列表 **不能超过为1000个字符**。须征得用户同意才可传入该参数
//        TogetherAdCsj.keywords = ""
//        // 可选参数，需在初始化之前，设置额外的用户信息 **不能超过为1000个字符**
//        TogetherAdCsj.data = ""
//        // 可选参数，需在初始化之前，可以设置隐私信息控制开关，需要重写其方法
//        TogetherAdCsj.customController = object : TTCustomController() {}
//        // 可选参数，需在初始化之前，穿山甲初始化状态回调
//        TogetherAdCsj.initCallback = object : TTAdSdk.InitCallback {}

        /**
         * 自定义优量汇的初始化配置
         */
//        //可参照 DownloadConfirmHelper 自定义下载确认的回调
//        TogetherAdGdt.downloadConfirmListener = DownloadConfirmHelper.DOWNLOAD_CONFIRM_LISTENER

        //初始化穿山甲
        TogetherAdCsj.init(context = this, adProviderType = AdProviderType.CSJ.type, csjAdAppId = "5001121", appName = this.getString(R.string.app_name))

        //初始化广点通
        TogetherAdGdt.init(context = this, adProviderType = AdProviderType.GDT.type, gdtAdAppId = "1101152570")
        //初始化百青藤
        TogetherAdBaidu.init(context = this, adProviderType = AdProviderType.BAIDU.type, baiduAdAppId = "e866cfb0")

        /**
         * 配置所有广告位ID
         * 如果你的ID是服务器下发，也可以把配置ID放在其他位置，但是必须要在请求广告之前完成配置，否则无法加载广告
         */
        TogetherAdCsj.idMapCsj = mutableMapOf(
                TogetherAdAlias.AD_SPLASH to "801121648",
                TogetherAdAlias.AD_SPLASH_HOT to "801121648",
                TogetherAdAlias.AD_NATIVE_EXPRESS_2_SIMPLE to "901121134",
                TogetherAdAlias.AD_NATIVE_EXPRESS_2_RECYCLERVIEW to "901121125",
                TogetherAdAlias.AD_NATIVE_EXPRESS_SIMPLE to "",//不支持
                TogetherAdAlias.AD_NATIVE_EXPRESS_RECYCLERVIEW to "",//不支持
                TogetherAdAlias.AD_NATIVE_SIMPLE to "901121737",
                TogetherAdAlias.AD_NATIVE_RECYCLERVIEW to "901121737",
                TogetherAdAlias.AD_BANNER to "901121246",
                TogetherAdAlias.AD_INTER to "945509693",
                TogetherAdAlias.AD_REWARD to "901121365",
                TogetherAdAlias.AD_FULL_VIDEO to "901121073",
                TogetherAdAlias.AD_HYBRID_SPLASH to "901121737",//id是原生类型
                TogetherAdAlias.AD_HYBRID_EXPRESS to "901121134",//id是原生模板2.0
                TogetherAdAlias.AD_HYBRID_VERTICAL_PREMOVIE to "901121073"//id是全屏视频
        )

        TogetherAdGdt.idMapGDT = mutableMapOf(
                TogetherAdAlias.AD_SPLASH to "8863364436303842593",
                TogetherAdAlias.AD_SPLASH_HOT to "8863364436303842593",
                TogetherAdAlias.AD_NATIVE_EXPRESS_2_SIMPLE to "9061615683013706",
                TogetherAdAlias.AD_NATIVE_EXPRESS_2_RECYCLERVIEW to "9061615683013706",
                TogetherAdAlias.AD_NATIVE_EXPRESS_SIMPLE to "5060295460765937",
                TogetherAdAlias.AD_NATIVE_EXPRESS_RECYCLERVIEW to "5060295460765937",
                TogetherAdAlias.AD_NATIVE_SIMPLE to "6040749702835933",
                TogetherAdAlias.AD_NATIVE_RECYCLERVIEW to "6040749702835933",
                TogetherAdAlias.AD_BANNER to "4080052898050840",
                TogetherAdAlias.AD_INTER to "1050691202717808",
                TogetherAdAlias.AD_REWARD to "2090845242931421",
                TogetherAdAlias.AD_FULL_VIDEO to "9051949928507973",
                TogetherAdAlias.AD_HYBRID_SPLASH to "8863364436303842593",//id是开屏类型
                TogetherAdAlias.AD_HYBRID_EXPRESS to "5060295460765937",//id是原生模板1.0
                TogetherAdAlias.AD_HYBRID_VERTICAL_PREMOVIE to "6040749702835933"
        )

        TogetherAdBaidu.idMapBaidu = mutableMapOf(
                TogetherAdAlias.AD_SPLASH to "2058622",
                TogetherAdAlias.AD_SPLASH_HOT to "2058622",
                TogetherAdAlias.AD_NATIVE_EXPRESS_2_SIMPLE to "",//不支持
                TogetherAdAlias.AD_NATIVE_EXPRESS_2_RECYCLERVIEW to "",//不支持
                TogetherAdAlias.AD_NATIVE_EXPRESS_SIMPLE to "",//不支持
                TogetherAdAlias.AD_NATIVE_EXPRESS_RECYCLERVIEW to "",//不支持
                TogetherAdAlias.AD_NATIVE_SIMPLE to "2058628",
                TogetherAdAlias.AD_NATIVE_RECYCLERVIEW to "2058628",
                TogetherAdAlias.AD_BANNER to "2015351",
                TogetherAdAlias.AD_INTER to "2403633",
                TogetherAdAlias.AD_REWARD to "5925490",
                TogetherAdAlias.AD_FULL_VIDEO to "",
                TogetherAdAlias.AD_HYBRID_SPLASH to "2058628",//id是原生类型
                TogetherAdAlias.AD_HYBRID_EXPRESS to "",//不支持
                TogetherAdAlias.AD_HYBRID_VERTICAL_PREMOVIE to ""//不支持
        )

        /**
         * 配置全局的广告商权重。
         * 如果在调用具体广告API时没有配置单次请求的权重，就会默认使用这个全局的权重
         * 如果不配置，TogetherAd会默认所有初始化的广告商权重相同
         *
         * 也可以在请求广告前设置，实时生效
         */
        TogetherAd.setPublicProviderRatio(linkedMapOf(
                AdProviderType.GDT.type to 1,
                AdProviderType.CSJ.type to 1,
                AdProviderType.BAIDU.type to 0
        ))

        /**
         * 自定义图片加载方式
         * 用于自渲染类型的广告图片加载
         * 如果不配置，TogetherAd 会使用默认的图片加载方式
         * 不建议使用默认的 DefaultImageLoader 兼容性较差
         * 主要考虑到：开发者可以自定义实现图片加载：渐变、占位图、错误图等
         */
//        TogetherAd.setCustomImageLoader(object : AdImageLoader {
//            override fun loadImage(context: Context, imageView: ImageView, imgUrl: String) {
//                Glide.with(context).load(imgUrl).into(imageView)
//            }
//        })

        /**
         * 日志的开关
         * 全局实时生效
         */
        TogetherAd.printLogEnable = BuildConfig.DEBUG

        /**
         * 是否失败切换 （ 当请求广告失败时，是否允许切换到其他广告提供商再次请求 ）
         * 全局实时生效
         */
//        TogetherAd.failedSwitchEnable = true

        /**
         * 最大拉取延时时间 ms（ 请求广告的超时时间 ）
         * 3000 ≤ value ≥ 10000（ 小于3000时按3000计算，大于10000时按10000计算 ）
         * 全局实时生效
         * 不设置或设置为0 -> 超时时间无限长
         */
//        TogetherAd.maxFetchDelay = 8000

        /**
         * 所有广告商所有广告类型的广告都会回调这个监听器
         * 主要是方便做统计：请求成功率、请求失败信息等
         */
//        TogetherAd.allAdListener = object : AllAdListener {
//            override fun onAdStartRequest(providerType: String, alias: String) {
//                "开始请求: 提供商: $providerType, 广告位: $alias".logi("TogetherAd.allAdListener")
//            }
//
//            override fun onAdFailed(providerType: String, alias: String, failedMsg: String?) {
//                "请求失败: 提供商: $providerType, 广告位: $alias, 错误信息: $failedMsg".loge("TogetherAd.allAdListener")
//            }
//
//            override fun onAdLoaded(providerType: String, alias: String) {
//                "请求成功: 提供商: $providerType, 广告位: $alias".logi("TogetherAd.allAdListener")
//            }
//        }
        /**
         * 设置广告分发模式
         * DispatchType.Priority    优先权重最高分发模式
         * DispatchType.Random  按照权重随机分发模式
         */
//        TogetherAd.dispatchType = DispatchType.Random
    }
}