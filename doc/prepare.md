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
 *
 * Created by Matthew Chen on 2020-04-16.
 */
object TogetherAdAlias {

    //开屏
    const val AD_SPLASH = "ad_splash"

    //原生模板2.0 简单使用
    const val AD_NATIVE_EXPRESS_2_SIMPLE = "ad_native_express2_simple"

    //原生模板2.0 在 RecyclerView 中使用
    const val AD_NATIVE_EXPRESS_2_RECYCLERVIEW = "ad_native_express2_recyclerview"

    //原生模板 简单使用
    const val AD_NATIVE_EXPRESS_SIMPLE = "ad_native_express_simple"

    //原生模板 在 RecyclerView 中使用
    const val AD_NATIVE_EXPRESS_RECYCLERVIEW = "ad_native_express_recyclerview"

    //原生 简单使用
    const val AD_NATIVE_SIMPLE = "ad_native_simple"

    //原生 在 RecyclerView 中使用
    const val AD_NATIVE_RECYCLERVIEW = "ad_native_recyclerview"

    //Banner
    const val AD_BANNER = "ad_banner"

    //插屏广告
    const val AD_INTER = "ad_inter"

    //激励广告
    const val AD_REWARD = "ad_reward"

    //全屏视频广告
    const val AD_FULL_VIDEO = "ad_full_video"

    //开屏混合使用
    const val AD_SPLASH_HYBRID = "ad_splash_and_native"

    //原生模板混合
    const val AD_EXPRESS_HYBRID = "ad_express_hybrid"

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
//        //可选参数，需在初始化之前，可以设置隐私信息控制开关，需要重写其方法
//        TogetherAdCsj.customController = object : TTCustomController() {}

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
        TogetherAdCsj.idMapCsj = mapOf(
                TogetherAdAlias.AD_SPLASH to "801121648",
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
                TogetherAdAlias.AD_SPLASH_HYBRID to "901121737",//id是原生类型
                TogetherAdAlias.AD_EXPRESS_HYBRID to "901121134"//id是原生模板2.0
        )

        TogetherAdGdt.idMapGDT = mapOf(
                TogetherAdAlias.AD_SPLASH to "8863364436303842593",
                TogetherAdAlias.AD_NATIVE_EXPRESS_2_SIMPLE to "9061615683013706",
                TogetherAdAlias.AD_NATIVE_EXPRESS_2_RECYCLERVIEW to "9061615683013706",
                TogetherAdAlias.AD_NATIVE_EXPRESS_SIMPLE to "5060295460765937",
                TogetherAdAlias.AD_NATIVE_EXPRESS_RECYCLERVIEW to "5060295460765937",
                TogetherAdAlias.AD_NATIVE_SIMPLE to "6040749702835933",
                TogetherAdAlias.AD_NATIVE_RECYCLERVIEW to "6040749702835933",
                TogetherAdAlias.AD_BANNER to "4080052898050840",
                TogetherAdAlias.AD_INTER to "1050691202717808",
                TogetherAdAlias.AD_REWARD to "2090845242931421",
                TogetherAdAlias.AD_FULL_VIDEO to "",//不支持
                TogetherAdAlias.AD_SPLASH_HYBRID to "8863364436303842593",//id是开屏类型
                TogetherAdAlias.AD_EXPRESS_HYBRID to "5060295460765937"//id是原生模板1.0
        )

        TogetherAdBaidu.idMapBaidu = mapOf(
                TogetherAdAlias.AD_SPLASH to "2058622",
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
                TogetherAdAlias.AD_SPLASH_HYBRID to "2058628",//id是原生类型
                TogetherAdAlias.AD_EXPRESS_HYBRID to ""//不支持
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
                AdProviderType.BAIDU.type to 1,
                AdProviderType.CSJ.type to 3
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
         * 不设置代表没有超时时间
         */
//        TogetherAd.maxFetchDelay = 5000

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
        TogetherAd.dispatchType = DispatchType.Random
    }
}
```