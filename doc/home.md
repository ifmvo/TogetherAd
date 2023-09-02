# 一、配置项目

### 1. Gradle 添加依赖

项目根目录下的 build.gradle 文件中添加 ``JitPack`` 仓库

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

``core``是必选，其他5个根据自身需求``任选``1 ~ 5个组合搭配

```gradle
dependencies {

    //核心库（ 必要 ）
    implementation 'com.github.ifmvo.TogetherAd:core:5.2.5'

    //芒果 （ 可选 ）
    implementation 'com.github.ifmvo.TogetherAd:mg:5.2.5'

    //腾讯优量汇 （ 可选 ）
    implementation 'com.github.ifmvo.TogetherAd:gdt:5.2.5'

    //穿山甲 （ 可选 ）
    implementation 'com.github.ifmvo.TogetherAd:csj:5.2.5'

    //快手联盟 （ 可选 ）
    implementation 'com.github.ifmvo.TogetherAd:ks:5.2.5'

    //百度百青藤 （ 可选 ）
    implementation 'com.github.ifmvo.TogetherAd:baidu:5.2.5'

}
```

> 对应版本：[芒果1.0.10](https://www.mangolm.com/?ch=1)；穿山甲5.6.0.2；优量汇4.510.1380；快手3.3.25；百度5.91

### 2. 混淆 proguard-rules.pro

资源库中已自带混淆规则，通常情况下无需手动配置。

当然你也可以点击这里查看每个资源库的混淆规则： [core](core/proguard-rules.pro)、[gdt](gdt/proguard-rules.pro)、[csj](csj/proguard-rules.pro)、[baidu](baidu/proguard-rules.pro)

>库中已自带各个广告SDK所需的配置，通常情况下不再需要手动添加。
可以在各个模块的 AndroidManifest.xml 文件中自行查看

# 二、初始化

### 1. 新建一个枚举类 AdProviderType，列举你接入的广告商

```kotlin
/*
 * 广告提供商枚举
 * 不需要的就删除，只保留需要的即可
 */
enum class AdProviderType(val type: String) {

    //芒果移动广告
    MG("mg"),

    //腾讯优量汇 也叫广点通
    GDT("gdt"),

    //穿山甲
    CSJ("csj"),

    //快手
    KS("ks"),

    //百度百青藤
    BAIDU("baidu")

}
```

### 2. 新建一个 静态类，并列举你项目中的所有广告位

```kotlin
/**
 * 所有广告位的别名
 *
 * 列举你项目中的所有广告位，并给每个广告位起个名字
 *
 * 用于初始化广告位ID 以及 广告的请求
 */
object TogetherAdAlias {

    //开屏
    const val AD_SPLASH = "ad_splash"

    //激励广告
    const val AD_REWARD = "ad_reward"

    //.....更多其他
}
```

### 3. 在 Applicatioin 的 onCreate() 方法里面调用初始化方法

同时需要配置所有 ``广告ID`` 和 ``广告位ID``

**注意：示例代码中的 ``ID`` 是各个平台中 Demo 的测试 ``ID``，可用于测试。正式环境下各位开发者请自行更换。**

```kotlin
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        /**
         * 尽量放在 Application 的 onCreate 方法中初始化，否则可能会影响填充率
         */
        //初始化穿山甲
        TogetherAdCsj.init(context = this, adProviderType = AdProviderType.CSJ.type, csjAdAppId = "5001121", appName = this.getString(R.string.app_name))
        //初始化广点通
        TogetherAdGdt.init(context = this, adProviderType = AdProviderType.GDT.type, gdtAdAppId = "1101152570")
        //初始化百青藤
        TogetherAdBaidu.init(context = this, adProviderType = AdProviderType.BAIDU.type, baiduAdAppId = "e866cfb0")
        //初始化快手
        TogetherAdKs.init(context = this, adProviderType = AdProviderType.KS.type, ksAdAppId = "90009")

        /**
         * 配置所有广告位ID
         * 如果你的ID是服务器下发，也可以把配置ID放在其他位置，但是必须要在请求广告之前完成配置，否则无法加载广告
         */
        TogetherAdCsj.idMapCsj = mutableMapOf(
            TogetherAdAlias.AD_SPLASH to "801121648",
            TogetherAdAlias.AD_REWARD to "901121365"
            //......其他更多
        )

        TogetherAdGdt.idMapGDT = mutableMapOf(
            TogetherAdAlias.AD_SPLASH to "8863364436303842593",
            TogetherAdAlias.AD_REWARD to "2090845242931421"
            //......其他更多
        )

        TogetherAdKs.idMapKs = mutableMapOf(
            TogetherAdAlias.AD_SPLASH to 4000000042L,
            TogetherAdAlias.AD_REWARD to 90009001L
            //......其他更多
        )

        TogetherAdBaidu.idMapBaidu = mutableMapOf(
            TogetherAdAlias.AD_SPLASH to "2058622",
            TogetherAdAlias.AD_REWARD to "5925490"
            //......其他更多
        )

    }
}
```

更多自定义配置查看[示例代码](../demo/src/main/java/com/ifmvo/togetherad/demo/app/App.kt)

# 三、请求和展示广告

[开屏广告（请求成功后立即自动展示）](../demo/src/main/java/com/ifmvo/togetherad/demo/splash/SplashActivity.kt)（推荐）

[开屏广告（请求成功后可手动控制展示时机）](../demo/src/main/java/com/ifmvo/togetherad/demo/splash/SplashProActivity.kt)

[激励广告（请求成功后立即自动展示）](../demo/src/main/java/com/ifmvo/togetherad/demo/reward/RewardProActivity.kt)（推荐）

[激励广告（请求成功后可手动控制展示时机）](../demo/src/main/java/com/ifmvo/togetherad/demo/reward/RewardActivity.kt)

[全屏视频广告](../demo/src/main/java/com/ifmvo/togetherad/demo/fullvideo/FullVideoActivity.kt)

>全屏视频广告，该广告的效果播放全屏的视频，类似于激励视频广告，区别在于视频一定时间后可跳过，无需全程观看完。

[Banner横幅广告](../demo/src/main/java/com/ifmvo/togetherad/demo/banner/BannerActivity.kt)

[Interstitial插屏广告](../demo/src/main/java/com/ifmvo/togetherad/demo/inter/InterActivity.kt)

[原生模板广告的简单用法](../demo/src/main/java/com/ifmvo/togetherad/demo/express/NativeExpressSimpleActivity.kt)

[原生模板广告在RecyclerView中使用](../demo/src/main/java/com/ifmvo/togetherad/demo/express/NativeExpressRecyclerViewActivity.kt)

[原生自渲染广告的简单用法](../demo/src/main/java/com/ifmvo/togetherad/demo/native_/NativeSimpleActivity.kt)

[原生自渲染广告在RecyclerView中使用](../demo/src/main/java/com/ifmvo/togetherad/demo/native_/NativeRecyclerViewActivity.kt)

>原生广告自渲染方式支持开发者自由拼合素材，最大程度的满足开发需求；与原生模板方式相比，自渲染方式更加自由灵活，开发者可以使用其打造自定义的布局样式
