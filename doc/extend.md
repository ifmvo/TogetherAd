# 扩展

### 扩展现有的 Provider
[CsjProvider.kt](../csj/src/main/java/com/ifmvo/togetherad/csj/CsjProvider.kt)、[GdtProvider.kt](../gdt/src/main/java/com/ifmvo/togetherad/gdt/GdtProvider.kt)、[BaiduProvider.kt](../baidu/src/main/java/com/ifmvo/togetherad/baidu/BaiduProvider.kt) 

这三个文件分别是穿山甲、广点通、百青藤真正请求广告的代码，如果现有的Provider不符合你的需求，你可以通过``继承``的方式，按照自己的需求，对其他相应的方法进行``重写``。

以 Csj 的 Banner 广告为例，目前穿山甲只允许创建模板类型的 Banner 广告，CsjProvider.kt 中使用的就是通过 loadBannerExpressAd 方法请求模板类型Banner。如果你想用原生类型Banner，可以通过以下方式进行自定义：

```kotlin
class CustomCsjProvider : CsjProvider() {

    private val tag = "CustomCsjProvider"

    object Banner {

        var supportDeepLink: Boolean = true

        //图片的宽高
        internal var imageAcceptedSizeWidth = 600

        internal var imageAcceptedSizeHeight = 257

        fun setImageAcceptedSize(width: Int, height: Int) {
            imageAcceptedSizeWidth = width
            imageAcceptedSizeHeight = height
        }

        //Banner 刷新间隔时间
        var slideIntervalTime = 30 * 1000
    }

    override fun showBannerAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: BannerListener) {
        callbackBannerStartRequest(adProviderType, alias, listener)

        destroyBannerAd()

        val adSlot = AdSlot.Builder()
                .setCodeId(TogetherAdCsj.idMapCsj[alias]) //广告位id
                .setSupportDeepLink(Banner.supportDeepLink)
                .setImageAcceptedSize(Banner.imageAcceptedSizeWidth, Banner.imageAcceptedSizeHeight)
                .build()

        TTAdSdk.getAdManager().createAdNative(activity).loadBannerAd(adSlot, object : TTAdNative.BannerAdListener {
            override fun onBannerAdLoad(bannerAd: TTBannerAd?) {
                if (bannerAd == null) {
                    callbackBannerFailed(adProviderType, alias, listener, "请求成功，但是返回的 bannerAd 为空")
                    return
                }

                val bannerView = bannerAd.bannerView
                if (bannerView == null) {
                    callbackBannerFailed(adProviderType, alias, listener, "请求成功，但是返回的 bannerView 为空")
                    return
                }

                callbackBannerLoaded(adProviderType, alias, listener)

                bannerAd.setSlideIntervalTime(Banner.slideIntervalTime)
                container.removeAllViews()
                container.addView(bannerView)

                bannerAd.setBannerInteractionListener(object : TTBannerAd.AdInteractionListener {
                    override fun onAdClicked(view: View?, type: Int) {
                        callbackBannerClicked(adProviderType, listener)
                    }

                    override fun onAdShow(view: View?, type: Int) {
                        callbackBannerExpose(adProviderType, listener)
                    }
                })

                bannerAd.setShowDislikeIcon(object : TTAdDislike.DislikeInteractionCallback {
                    override fun onSelected(position: Int, value: String?) {
                        container.removeAllViews()
                        callbackBannerClosed(adProviderType, listener)
                    }

                    override fun onCancel() {}
                    override fun onRefuse() {}
                })
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                "onError".loge(tag)
                callbackBannerFailed(adProviderType, alias, listener, "错误码：$errorCode, 错误信息：$errorMsg")
            }
        })
    }

}
```

然后 Application 中初始化时的代码修改：

```kotlin
TogetherAdCsj.init(context = this, adProviderType = AdProviderType.CSJ.type, csjAdAppId = "5001121", appName = this.getString(R.string.app_name), providerClassPath = CustomCsjProvider::class.java.name)
```

### 新增自定义 Provider

同样的原理，如果穿山甲、广点通、百青藤这三个平台已经满足不了你，你还想再加一个其他的广告平台，那你就可以这样，自定义一个Provider：

```Kotlin
/**
 * 自定义小米广告提供商
 *
 * 根据其他 Provider 可自行实现广告请求逻辑
 *
 * Created by Matthew Chen on 2020/10/23.
 */
class XiaomiProvider: BaseAdProvider() {

    override fun showSplashAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: SplashListener) {}

    override fun showBannerAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: BannerListener) {}

    override fun destroyBannerAd() {}

    override fun requestInterAd(activity: Activity, adProviderType: String, alias: String, listener: InterListener) {}

    override fun showInterAd(activity: Activity) {}

    override fun destroyInterAd() {}

    override fun getNativeAdList(activity: Activity, adProviderType: String, alias: String, maxCount: Int, listener: NativeListener) {}

    override fun nativeAdIsBelongTheProvider(adObject: Any): Boolean { return false }

    override fun resumeNativeAd(adObject: Any) {}

    override fun pauseNativeAd(adObject: Any) {}

    override fun destroyNativeAd(adObject: Any) {}

    override fun requestRewardAd(activity: Activity, adProviderType: String, alias: String, listener: RewardListener) {}

    override fun showRewardAd(activity: Activity) {}
}
```

再创建一个文件模仿 TogetherAdGdt 自定义小米的初始化

```
/**
 * 自定义小米初始化
 *
 * Created by Matthew Chen on 2020/10/23.
 */
object TogetherAdXiaomi {

    var idMapBaidu = mapOf<String, String>()

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull xiaomiAdAppId: String) {
        init(context, adProviderType, xiaomiAdAppId, null, null)
    }

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull xiaomiAdAppId: String, providerClassPath: String? = null) {
        init(context, adProviderType, xiaomiAdAppId, null, providerClassPath)
    }

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull xiaomiAdAppId: String, xiaomiIdMap: Map<String, String>? = null) {
        init(context, adProviderType, xiaomiAdAppId, xiaomiIdMap, null)
    }

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull xiaomiAdAppId: String, xiaomiIdMap: Map<String, String>? = null, providerClassPath: String? = null) {
        TogetherAd.addProvider(AdProviderEntity(adProviderType, if (providerClassPath?.isEmpty() != false) BaiduProvider::class.java.name else providerClassPath))
        xiaomiIdMap?.let { idMapBaidu = it }

        //小米SDK初始化
    }

}
```

枚举里面添加``XIAOMI``

```kotlin
/*
 * 广告提供商枚举
 *
 * Created by Matthew_Chen on 2018/8/23.
 */
enum class AdProviderType(val type: String) {

    //百度 Mob （ 百青藤 ）
    BAIDU("baidu"),

    //腾讯的广点通（ 优量汇 ）
    GDT("gdt"),

    //穿山甲
    CSJ("csj"),
    
    //小米
    XIAOMI("xiaomi")

}
```

最后不要忘记再Application中初始化：

```kotlin
TogetherAdXiaomi.init(context = this, adProviderType = AdProviderType.XIAOMI.type, xiaomiAdAppId = "xxxxxxxx")
```

以上只是举例子，更多扩展同理。