# 准备工作

### 1. 列举广告商类型

新建一个枚举类 AdProviderType，列举你接入的广告商，任选1至3个，随意搭配

```kotlin
/*
 * 广告提供商枚举
 * 不需要的就删除，只保留需要的即可
 */
enum class AdProviderType(val type: String) {

    //腾讯优量汇 也叫广点通
    GDT("gdt"),

    //穿山甲
    CSJ("csj"),

    //快手
    KS("ks"),

    //百度百青藤
    //BAIDU("baidu")

}
```

### 2. 列举所有广告位

新建一个 静态类，并列举你项目中的所有广告位

```kotlin
/**
 * 所有广告位的别名
 *
 * 列举你项目中的所有广告位，并给每个广告位起个名字
 *
 * 用于初始化广告位ID 以及 广告的请求
 *
 * Created by Matthew Chen on 2020-04-16.
 */
object TogetherAdAlias {

    //开屏
    const val AD_SPLASH = "ad_splash"

    //激励广告
    const val AD_REWARD = "ad_reward"


    //.....更多其他
}
```

### 4. 初始化

在 Applicatioin 的 onCreate() 方法里面调用初始化方法

同时需要配置所有 ``广告ID`` 和 ``广告位ID``

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

> 示例代码中的 ``ID`` 是各个平台中 Demo 的测试 ``ID``。正式环境下各位开发者请自行更换。

库中已自带各个广告SDK所需的配置，通常情况下不再需要手动添加。
可以在各个模块的 AndroidManifest.xml 文件中自行查看