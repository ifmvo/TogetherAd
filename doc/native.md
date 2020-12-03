# 原生自渲染广告

原生广告自渲染方式支持开发者自由拼合素材，最大程度的满足开发需求；与原生广告（模板方式）相比，自渲染方式更加自由灵活，开发者可以使用其为开发者的应用打造自定义的布局样式

—— 引自广点通文档

### 声明并初始化

```kotlin
class NativeSimpleActivity : AppCompatActivity() {
    //声明
    private var adHelperNative: AdHelperNativePro? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_simple)

        //使用 Map<String, Int> 配置广告商 权重，通俗的讲就是 随机请求的概率占比
        val ratioMapNativeSimple = linkedMapOf(
                AdProviderType.GDT.type to 3,
                AdProviderType.CSJ.type to 1,
                AdProviderType.BAIDU.type to 1
        )

        //初始化  maxCount: 返回广告的最大个数 （ 由于各个广告提供商返回的广告数量不等，所以只能控制返回广告的最大数量。例：maxCount = 4，返回的 1 ≤ List.size ≤ 4 ）
        adHelperNative = AdHelperNativePro(activity = this, alias = TogetherAdAlias.AD_NATIVE_SIMPLE, maxCount = 1/*, ratioMap = ratioMapNativeSimple*/)
    }
}
```

### 请求

```kotlin
/**
 * 请求广告
 */
private fun requestAd() {
    //--------------------------------------------------------------------------------------
    //  （ 必须设置 ）如果需要使用穿山甲的原生广告，必须在请求之前设置类型。（ 没用到穿山甲的请忽略 ）
    //  设置方式：CsjProvider.Native.nativeAdType = AdSlot.TYPE_XXX（类型和你的广告位ID一致）。
    //  CsjProvider.Native.nativeAdType = AdSlot.TYPE_FEED
    //  CsjProvider.Native.nativeAdType = AdSlot.TYPE_INTERACTION_AD
    //  CsjProvider.Native.nativeAdType = AdSlot.TYPE_BANNER
    //  CsjProvider.Native.nativeAdType = AdSlot.TYPE_CACHED_SPLASH
    //  CsjProvider.Native.nativeAdType = AdSlot.TYPE_DRAW_FEED
    //  CsjProvider.Native.nativeAdType = AdSlot.TYPE_FULL_SCREEN_VIDEO
    //  CsjProvider.Native.nativeAdType = AdSlot.TYPE_REWARD_VIDEO
    //  CsjProvider.Native.nativeAdType = AdSlot.TYPE_SPLASH
    //--------------------------------------------------------------------------------------
    CsjProvider.Native.nativeAdType = AdSlot.TYPE_FEED

    //设置 穿山甲 图片可接受的尺寸 ( 建议设置，默认是 1080，607.5 )
    //CsjProvider.Native.setImageAcceptedSize(ScreenUtil.getDisplayMetricsWidth(this), ScreenUtil.getDisplayMetricsWidth(this) * 9 / 16)

    //指定普链广告点击后用于展示落地页的浏览器类型，可选项包括：InnerBrowser（APP 内置浏览器），Sys（系统浏览器），Default（默认，SDK 按照默认逻辑选择)
    // ( 非必须设置，默认是：BrowserType.Default )
    //GdtProvider.Native.browserType = BrowserType.Default

    //指定点击 APP 广告后是否展示二次确认，可选项包括 Default（wifi 不展示，非 wifi 展示），NoConfirm（所有情况不展示）
    // ( 非必须设置，默认是：DownAPPConfirmPolicy.Default )
    //GdtProvider.Native.downAPPConfirmPolicy = DownAPPConfirmPolicy.Default

    //设置返回视频广告的最大视频时长（闭区间，可单独设置），单位:秒，合法输入为：5<=maxVideoDuration<=60. 此设置会影响广告填充，请谨慎设置
    // ( 非必须设置，默认是：60 )
    //GdtProvider.Native.maxVideoDuration = 60

    //设置返回视频广告的最小视频时长（闭区间，可单独设置），单位:秒 此设置会影响广告填充，请谨慎设置
    // ( 非必须设置，默认是：5 )
    //GdtProvider.Native.minVideoDuration = 5

    //设置本次拉取的视频广告，从用户角度看到的视频播放策略；
    // 可选项包括自VideoOption.VideoPlayPolicy.AUTO(在用户看来，视频广告是自动播放的)和VideoOption.VideoPlayPolicy.MANUAL(在用户看来，视频广告是手动播放的)；
    // 如果广告位支持视频，强烈建议调用此接口设置视频广告的播放策略，有助于提高eCPM值；如果广告位不支持视频，忽略本接口
    // ( 默认是：VideoOption.VideoPlayPolicy.AUTO )
    //GdtProvider.Native.videoPlayPolicy = VideoOption.VideoPlayPolicy.AUTO

    adHelperNative?.getList(listener = object : NativeListener {
        override fun onAdStartRequest(providerType: String) {
            //在开始请求之前会回调此方法，失败切换的情况会回调多次
        }

        override fun onAdLoaded(providerType: String, adList: List<Any>) {
            //广告请求成功的回调，每次请求只回调一次
        }

        override fun onAdFailed(providerType: String, failedMsg: String?) {
            //请求失败的回调，失败切换的情况会回调多次
        }

        override fun onAdFailedAll() {
            //所有配置的广告商都请求失败了，只有在全部失败之后会回调一次
        }
    })
}
```

### 展示

```kotlin
/**
 * 展示广告
 */
private fun showAd(adObject: Any) {
    AdHelperNativePro.show(adObject = adObject, container = adContainer, nativeTemplate = NativeTemplateSimple1(), listener = object : NativeViewListener {
        override fun onAdExposed(providerType: String) {
            //每次曝光就会回调这里一次
        }

        override fun onAdClicked(providerType: String) {
            //每次点击就会回调这里一次
        }
    })
}
```

### 处理广告的生命周期

```kotlin
override fun onResume() {
    super.onResume()
    AdHelperNativePro.resumeAd(adObject)
}

override fun onPause() {
    super.onPause()
    AdHelperNativePro.pauseAd(adObject)
}

override fun onDestroy() {
    super.onDestroy()
    AdHelperNativePro.destroyAd(adObject)
}
```

详情可查看 Demo 中 [原生自渲染广告的简单用法示例代码](../demo/src/main/java/com/ifmvo/togetherad/demo/native_/NativeSimpleActivity.kt)

详情可查看 Demo 中 [原生自渲染广告在RecyclerView中使用示例代码](../demo/src/main/java/com/ifmvo/togetherad/demo/native_/NativeRecyclerViewActivity.kt)