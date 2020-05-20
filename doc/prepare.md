# 准备工作

### 1. 列举广告商类型

新建一个枚举类 AdProviderType，列举你接入的广告商，任选1至3个，随意搭配

```
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

### 2. 列举所有广告位

新建一个 静态类，并列举你项目中的所有广告位，例如下面: 列举Demo 中所有的广告位别名

```
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

### 3. 初始化

在 Applicatioin 的 onCreate() 方法里面调用初始化方法

同时需要配置所有 ``广告ID`` 和 ``广告位ID``

```
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        //初始化穿山甲
        TogetherAdCsj.init(context = this, adProviderType = AdProviderType.CSJ.type, csjAdAppId = "你的穿山甲广告的应用ID", appName = this.getString(R.string.app_name), csjIdMap = mapOf(
                TogetherAdAlias.AD_SPLASH to "你的对应广告位的ID",
                TogetherAdAlias.AD_NATIVE_SIMPLE to "你的对应广告位的ID",
                TogetherAdAlias.AD_NATIVE_RECYCLERVIEW to "你的对应广告位的ID",
                TogetherAdAlias.AD_BANNER to "你的对应广告位的ID",
                TogetherAdAlias.AD_REWARD to "你的对应广告位的ID"
        ), isDebug = BuildConfig.DEBUG)

        //初始化广点通
        TogetherAdGdt.init(context = this, adProviderType = AdProviderType.GDT.type, gdtAdAppId = "你的广点通广告的应用ID", gdtIdMap = mapOf(
                TogetherAdAlias.AD_SPLASH to "你的对应广告位的ID",
                TogetherAdAlias.AD_NATIVE_SIMPLE to "你的对应广告位的ID",
                TogetherAdAlias.AD_NATIVE_RECYCLERVIEW to "你的对应广告位的ID",
                TogetherAdAlias.AD_BANNER to "你的对应广告位的ID",
                TogetherAdAlias.AD_REWARD to "你的对应广告位的ID"
        ))

        //初始化百青藤
        TogetherAdBaidu.init(context = this, adProviderType = AdProviderType.BAIDU.type, baiduAdAppId = "你的百青藤广告的应用ID", baiduIdMap = mapOf(
                TogetherAdAlias.AD_SPLASH to "你的对应广告位的ID",
                TogetherAdAlias.AD_NATIVE_SIMPLE to "你的对应广告位的ID",
                TogetherAdAlias.AD_NATIVE_RECYCLERVIEW to "你的对应广告位的ID",
                TogetherAdAlias.AD_BANNER to "你的对应广告位的ID",
                TogetherAdAlias.AD_REWARD to "你的对应广告位的ID"
        ))

        /**
         * 配置全局的广告商权重。
         * 如果在调用具体广告API时没有配置单次请求的权重，就会默认使用这个全局的权重
         * 如果不配置，TogetherAd会默认所有初始化的广告商权重相同
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