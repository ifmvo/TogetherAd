# 开屏广告

开屏广告以App启动作为曝光时机，提供5s的可感知广告展示。用户可以点击广告跳转到目标页面，或者点击右上角的“跳过”按钮，跳转到app内容首页。

—— 引自广点通文档

### 调用姿势

```kotlin
/**
 * 设置 gdt 开屏广告 自定义跳过按钮
 * TogetherAd 提供了两个简单的实例模板，同时只能设置一个,如果设置多个后面的生效
 * 目前只有 优量汇(广点通) 支持自定义跳过按钮的样式，所以只会对 广点通 生效
 */
GdtProvider.Splash.customSkipView = SplashSkipViewSimple2()
//GdtProvider.Splash.customSkipView = SplashSkipViewSimple1()
/**
 * 设置 gdt 开屏广告超时时间
 * fetchDelay 参数，设置开屏广告从请求到展示所花的最大时长（并不是指广告曝光时长），
 * 取值范围为[3000, 5000]ms。
 * 如果需要使用默认值，可以给 fetchDelay 设为0或者不设置。
 */
//GdtProvider.Splash.maxFetchDelay = 0

/**
 * 给 csj 设置可接受的图片尺寸，避免图片变形
 * 一般设置容器的宽高即可
 */
CsjProvider.Splash.setImageAcceptedSize(ScreenUtil.getDisplayMetricsWidth(this), ScreenUtil.getDisplayMetricsHeight(this) - 100f.dp.toInt())

/**
 * 设置 csj 开屏广告超时时间
 * fetchDelay 参数，设置开屏广告从请求到展示所花的最大时长（并不是指广告曝光时长），
 * 如果不设置，默认值为 3000ms
 */
//CsjProvider.Splash.maxFetchDelay = 3000

//使用 Map<String, Int> 配置广告商 权重，通俗的讲就是 随机请求的概率占比
val ratioMapSplash = linkedMapOf(
        AdProviderType.GDT.type to 1,
        AdProviderType.CSJ.type to 1,
        AdProviderType.BAIDU.type to 1
)

/**
 * activity: 必传。这里不是 Context，因为广点通必须传 Activity，所以统一传 Activity。
 * alias: 必传。广告位的别名。初始化的时候是根据别名设置的广告ID，所以这里TogetherAd会根据别名查找对应的广告位ID。
 * ratioMap: 非必传。广告商的权重。可以不传或传null，空的情况 TogetherAd 会自动使用初始化时 TogetherAd.setPublicProviderRatio 设置的全局通用权重。
 * container: 必传。请求到广告之后会自动添加到 container 这个布局中展示。
 * listener: 非必传。如果你不需要监听结果可以不传或传空。各个回调方法也可以选择性添加
 */
AdHelperSplash.show(activity = this, alias = TogetherAdAlias.AD_SPLASH, ratioMap = ratioMapSplash, container = adContainer, listener = object : SplashListener {

    override fun onAdStartRequest(providerType: String) {
        //在开始请求之前会回调此方法，失败切换的情况会回调多次
    }

    override fun onAdLoaded(providerType: String) {
        //广告请求成功的回调，每次请求只回调一次
    }

    override fun onAdClicked(providerType: String) {
        //点击广告的回调
    }

    override fun onAdExposure(providerType: String) {
        //广告展示曝光的回调
    }

    override fun onAdFailed(providerType: String, failedMsg: String?) {
        //请求失败的回调，失败切换的情况会回调多次
    }

    override fun onAdFailedAll() {
        //所有配置的广告商都请求失败了，只有在全部失败之后会回调一次
        actionHome(1000)//跳转主页并finish开屏页
    }

    override fun onAdDismissed(providerType: String) {
        //开屏广告消失了，点了跳过按钮或者倒计时结束之后会回调一次
        //在这里跳转主界面，并关闭 Splash
        actionHome(0)//跳转主页并finish开屏页
    }
})
```

> 回调中 ``providerType: String `` 是广告商的类型

### 自定义广点通的跳过按钮

TogetherAd 提供了两个简单的实例模板，只需要在请求广告之前进行如下配置即可：

```kotlin
/*
 * 设置 gdt 开屏广告 自定义跳过按钮
 * TogetherAd 提供了两个简单的实例模板，同时只能设置一个,如果设置多个后面的生效
 * 目前只有 优量汇(广点通) 支持自定义跳过按钮的样式，所以只会对 广点通 生效
 */
GdtProvider.Splash.customSkipView = SplashSkipViewSimple2()
//GdtProvider.Splash.customSkipView = SplashSkipViewSimple1()

AdHelperSplash.show(activity = this, alias = TogetherAdAlias.AD_SPLASH, ratioMap = ratioMapSplash, container = adContainer, listener = object : SplashListener {
	...
})
```

如果你觉得提供的两个模板都不满足你的需求，可以自定义样式：

1. 首先需要创建一个类并继承 BaseSplashSkipView

2. 然后模仿 ``SplashSkipViewSimple1`` 和 ``SplashSkipViewSimple2`` 来自定义想要的样式

```kotlin
class SplashSkipViewCustom : BaseSplashSkipView() {

    private lateinit var tvTime: TextView

    /**
     * 创建跳过按钮的布局
     */
    override fun onCreateSkipView(context: Context): View {
        val skipView = View.inflate(context, R.layout.layout_splash_skip_view_simple2, null)
        tvTime = skipView.findViewById(R.id.time)
        return skipView
    }

    /**
     * 处理倒计时的展示，单位：秒
     */
    override fun handleTime(second: Int) {
        tvTime.text = second.toString()
    }

    /**
     * 获取布局参数，控制跳过按钮的位置
     *
     * 注意：LayoutParams 的类型取决于 请求开屏广告时 container 参数的类型。
     * Demo 中是使用的 FrameLayout，所以这里就是 FrameLayout.LayoutParams；
     * LayoutParams类型必须要一致，否则会崩溃
     */
    override fun getLayoutParams(): ViewGroup.LayoutParams {
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.END or Gravity.TOP
        layoutParams.topMargin = 50
        layoutParams.rightMargin = 30
        return layoutParams
    }

}
```
最后不要忘记修改配置：

```kotlin
GdtProvider.Splash.customSkipView = SplashSkipViewCustom()

AdHelperSplash.show(activity = this, alias = TogetherAdAlias.AD_SPLASH, ratioMap = ratioMapSplash, container = adContainer, listener = object : SplashListener {
	...
})
```

详情可查看 Demo 中 [开屏广告请求并展示的示例代码](../demo/src/main/java/com/ifmvo/togetherad/demo/splash/SplashActivity.kt)
详情可查看 Demo 中 [开屏广告请求和展示分开的示例代码](../demo/src/main/java/com/ifmvo/togetherad/demo/splash/SplashProActivity.kt)