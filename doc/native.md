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

        //初始化
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