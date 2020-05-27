# 准备工作
### 1. 自行选择性添加权限到 AndroidManifest.xml 文件中

**穿山甲的权限**  

```
<!--必要权限-->
<uses-permission android:name="android.permission.INTERNET" />

<!--可选权限-->
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES"/>
<uses-permission android:name="android.permission.GET_TASKS"/>

<!--可选，穿山甲提供“获取地理位置权限”和“不给予地理位置权限，开发者传入地理位置参数”两种方式上报用户位置，两种方式均可不选，添加位置权限或参数将帮助投放定位广告-->
<!--请注意：无论通过何种方式提供给穿山甲用户地理位置，均需向用户声明地理位置权限将应用于穿山甲广告投放，穿山甲不强制获取地理位置信息-->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />

<!-- 如果有视频相关的广告且使用textureView播放，请务必添加，否则黑屏 -->
<uses-permission android:name="android.permission.WAKE_LOCK" />
```

```
//建议在广告请求前，合适的时机调用SDK提供的方法
//TTAdManager接口中的方法，context可以是Activity或Application
void requestPermissionIfNecessary(Context context);
```

**优量汇的权限**

优量汇 SDK 建议您在AndroidManifest.xml添加以下权限声明，若您的targetSDKVersion >= 23您还需要在运行时进行动态权限申请（可参考示例工程）

```
<!--必要权限-->
<uses-permission android:name="android.permission.INTERNET" />

<!--可选权限-->
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />    <!-- 可选，如果需要精确定位的话请加上此权限 -->
```

注意：可选权限SDK不强制校验上述权限（即:无上述权限sdk也可正常工作），但建议您申请上述权限。针对单媒体的用户，允许获取权限的，投放定向广告；不允许获取权限的用户，投放通投广告。媒体可以选择是否把上述权限提供给优量汇，并承担相应广告填充和eCPM单价下降损失的结果。

**百度的权限**

```
<!--必要权限-->
<uses-permission android:name="android.permission.INTERNET" />

<!--可选权限-->
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permissio.WRITE_EXTERNAL_STORAGE" />
```

### 2. 列举广告商类型

新建一个枚举类 AdProviderType，列举你接入的广告商，任选1至3个，随意搭配

```kotlin
/*
 * 广告提供商枚举
 * 不需要的就删除，只保留需要的即可
 */
enum class AdProviderType(val type: String) {

    //腾讯的广点通
    GDT("gdt"),

    //穿山甲
    CSJ("csj"),
    
    //百度 Mob
    //BAIDU("baidu")

}
```

### 3. 列举所有广告位

新建一个 静态类，并列举你项目中的所有广告位，例如下面: 列举Demo 中所有的广告位别名

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

    //原生_简单使用
    const val AD_NATIVE_SIMPLE = "ad_native_simple"

    //原生_在 RecyclerView 中使用
    const val AD_NATIVE_RECYCLERVIEW = "ad_native_recyclerview"

    //Banner
    const val AD_BANNER = "ad_banner"

    //激励广告
    const val AD_REWARD = "ad_reward"

}
```

### 4. 初始化

在 Applicatioin 的 onCreate() 方法里面调用初始化方法

同时需要配置所有 ``广告ID`` 和 ``广告位ID``

示例代码中的 ``ID`` 是各个平台中 Demo 的测试 ``ID``

```kotlin
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        /**
         * 可自行查看穿山甲的文档，自定义穿山甲的初始化配置
         * ttAdConfig 为可选参数。不传或传空的情况会使用默认的配置
         */
        //初始化穿山甲
        TogetherAdCsj.init(context = this, adProviderType = AdProviderType.CSJ.type, csjAdAppId = "5001121", appName = this.getString(R.string.app_name), csjIdMap = mapOf(
                TogetherAdAlias.AD_SPLASH to "801121648",
                TogetherAdAlias.AD_NATIVE_SIMPLE to "901121737",
                TogetherAdAlias.AD_NATIVE_RECYCLERVIEW to "901121737",
                TogetherAdAlias.AD_BANNER to "901121246",
                TogetherAdAlias.AD_REWARD to "901121365"
        )/*, ttAdConfig = customTTAdConfig*/)

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
```