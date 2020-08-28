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
        val radioMapNativeSimple = mapOf(
                AdProviderType.GDT.type to 3,
                AdProviderType.CSJ.type to 1,
                AdProviderType.BAIDU.type to 1
        )

        //初始化  maxCount: 返回广告的最大个数 （ 由于各个广告提供商返回的广告数量不等，所以只能控制返回广告的最大数量。例：maxCount = 4，返回的 1 ≤ List.size ≤ 4 ）
        adHelperNative = AdHelperNativePro(activity = this, alias = TogetherAdAlias.AD_NATIVE_SIMPLE, maxCount = 1/*, radioMap = radioMapNativeSimple*/)
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
    //  必须在每次请求穿山甲的原生广告之前设置类型。( 如果没用到穿山甲可忽略 )
    //  设置方式：AdHelperNativePro.csjNativeAdType = AdSlot.TYPE_XXX（类型和你的广告位ID一致）。
    //  AdHelperNativePro.csjNativeAdType = AdSlot.TYPE_FEED
    //  AdHelperNativePro.csjNativeAdType = AdSlot.TYPE_INTERACTION_AD
    //  AdHelperNativePro.csjNativeAdType = AdSlot.TYPE_BANNER
    //  AdHelperNativePro.csjNativeAdType = AdSlot.TYPE_CACHED_SPLASH
    //  AdHelperNativePro.csjNativeAdType = AdSlot.TYPE_DRAW_FEED
    //  AdHelperNativePro.csjNativeAdType = AdSlot.TYPE_FULL_SCREEN_VIDEO
    //  AdHelperNativePro.csjNativeAdType = AdSlot.TYPE_REWARD_VIDEO
    //  AdHelperNativePro.csjNativeAdType = AdSlot.TYPE_SPLASH
    //--------------------------------------------------------------------------------------
    AdHelperNativePro.csjNativeAdType = AdSlot.TYPE_FEED

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
    AdHelperNativePro.show(adObject = adObject, container = adContainer, nativeTemplate = NativeTemplateCommon(), listener = object : NativeViewListener {
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

可查看 Demo 中 [原生自渲染广告的简单用法示例代码](../demo/src/main/java/com/ifmvo/togetherad/demo/native_/NativeSimpleActivity.kt)

可查看 Demo 中 [原生自渲染广告在RecyclerView中使用示例代码](../demo/src/main/java/com/ifmvo/togetherad/demo/native_/NativeRecyclerViewFragment.kt)