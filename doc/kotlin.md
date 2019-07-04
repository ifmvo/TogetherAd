### Kotlin 调用姿势
1. 自定义广告位
新建一个名字叫做 TogetherAdConst 的类，这个类用来定义所有广告的位置，例：开屏广告、列表信息流 ... 等等
```
class TogetherAdConst {
    companion object {
        //开屏
        const val AD_SPLASH = "ad_splash"

        //插屏
        const val AD_INTER = "ad_inter"

        //首页信息流
        const val AD_FLOW_INDEX = "ad_flow_index"
        
        //视频播放前贴
        const val AD_VIDEO_PRE = "ad_video_pre"

        //xxxxxxxx
        const val XXX_XXX_XXX = "xxx_xxx_xxx"
        
        ......
    }
}
```
> 自定义的广告位用于初始化广告位ID，也被当做请求广告时的参数

2. 初始化
```
//初始化百度Mob广告
val baiduIdMap = mutableMapOf<String, String?>(
    TogetherAdConst.AD_SPLASH to "2543740",
    TogetherAdConst.AD_INTER to "2543741",
    TogetherAdConst.AD_FLOW_INDEX to "2715031",
    TogetherAdConst.AD_TIEPIAN_LIVE to "5985131"
)
TogetherAd.initBaiduAd(applicationContext, "ee93e58e", baiduIdMap)

//初始化腾讯广点通广告
val gdtIdMap = mutableMapOf<String, String?>(
    TogetherAdConst.AD_SPLASH to "8030228893573270",
    TogetherAdConst.AD_INTER to "4090620883979242",
    TogetherAdConst.AD_FLOW_INDEX to "4010231735332811",
    TogetherAdConst.AD_TIEPIAN_LIVE to "4060449650093530"
)
TogetherAd.initGDTAd(applicationContext, "1106572734", gdtIdMap)

//初始化科大讯飞广告
val xunFeiIdMap = mutableMapOf<String, String?>(
    TogetherAdConst.AD_SPLASH to "FD0AC8FDE5CE0B317A6C4077E68D34CC",
    TogetherAdConst.AD_INTER to "6FD44C667D5EFD97730CC1E3F174D965",
    TogetherAdConst.AD_FLOW_INDEX to "EE2009111A1DF0BCA9DAD3723A95602F",
    TogetherAdConst.AD_TIEPIAN_LIVE to "93D157AAFA8B7EF64165B1F0ECEE2623"
)
TogetherAd.initXunFeiAd(applicationContext, xunFeiIdMap)
```

3. 开屏广告的调用姿势
```
/**
 * mContext 上下文
 * splashConfigStr: 各平台的展示比例，会根据这个比例随机请求某个平台的广告，比例越大展示的概率就越大
 * 例如: "baidu:1,gdt:1,xunfei:1" (baidu、gdt、xunfei 等key区分大小写)
 * adConstStr: 广告位 例如：TogetherAdConst.AD_SPLASH
 * adsParentLayout: 展示开屏广告的容器 （ 广告平台要求：容器大小不低于屏幕的75% ）
 * adListener: 监听器广告状态回调
 */
TogetherAdSplash.showAdFull(mContext, splashConfigStr, adConstStr, adsParentLayout, object : TogetherAdSplash.AdListenerSplashFull {
    override fun onStartRequest(channel: String) {
        //开始请求广告之前，channel：gdt、baidu、xunfei
    }

    override fun onAdClick(channel: String) {
        //广告被点击之后，channel：gdt、baidu、xunfei
    }

    override fun onAdFailed(failedMsg: String?) {
        //广告加载失败
    }

    override fun onAdDismissed() {
        //广告倒计时结束
    }

    override fun onAdPrepared(channel: String) {
        //广告请求成功，准备展示，channel：gdt、baidu、xunfei
    }
})
```

4. 信息流广告的调用姿势
```
/**
 * 信息流广告会随机返回其中一种平台的广告集合，一般用于列表中广告的展示
 * mContext 上下文
 * listConfigStr: 各平台的展示比例，会根据这个比例随机请求某个平台的广告，比例越大展示的概率就越大
 * 例如: "baidu:1,gdt:1,xunfei:1" (baidu、gdt、xunfei 等key区分大小写)
 * adConstStr: 广告位 例如：TogetherAdConst.AD_FLOW_INDEX
 * adListener: 监听器广告状态回调
 */
TogetherAdFlow.getAdList(mContext, "baidu:1,gdt:1,xunfei:1", adConstStr, object : TogetherAdFlow.AdListenerList {
    override fun onAdFailed(failedMsg: String?) {
        //广告加载失败
    }

    override fun onAdLoaded(channel: String, adList: List<*>) {
        //广告请求成功，准备展示
        //channel：gdt、baidu、xunfei
        //adList：某个平台的广告List，list里面的object：NativeMediaADData 是广点通，NativeResponse 是百度，NativeADDataRef 是科大讯飞
        //可以通过多类型列表进行展示，详细可借鉴 Demo 中的 IndexFragment 
    }

    override fun onStartRequest(channel: String) {
        //开始请求广告之前，channel：gdt、baidu、xunfei
    }
    
    // onClick 点击事件需要自行根据各平台的文档处理
})

```
5. 前贴广告的调用姿势
```
TogetherAdPreMovie.showAdPreMovie(this, "baidu:1,gdt:1,xunfei:1", TogetherAdConst.AD_TIEPIAN_LIVE, adsParentLayout, object : TogetherAdPreMovie.AdListenerPreMovie {
    override fun onAdClick(channel: String) {
        //广告被点击之后，channel：gdt、baidu、xunfei
    }

    override fun onAdFailed(failedMsg: String?) {
        //广告加载失败
    }

    override fun onAdDismissed() {
        //广告倒计时结束
    }

    override fun onAdPrepared(channel: String) {
        //广告请求成功，准备展示，channel：gdt、baidu、xunfei
    }

    override fun onStartRequest(channel: String) {
        //开始请求广告之前，channel：gdt、baidu、xunfei
    }
}, needTime)//是否需要倒计时
```

6. 插屏广告调用姿势
```
TogetherAdInter.showAdInter(this, Config.interAdConfig(), TogetherAdConst.AD_INTER, false, mRlInterAd, object : TogetherAdInter.AdListenerInter {
    override fun onStartRequest(channel: String) {
        //开始请求广告之前，channel：gdt、baidu、xunfei
    }

    override fun onAdClick(channel: String) {
        //广告被点击之后，channel：gdt、baidu、xunfei
    }

    override fun onAdFailed(failedMsg: String?) {
        //广告加载失败
    }

    override fun onAdDismissed() {
        //广告倒计时结束
    }

    override fun onAdPrepared(channel: String) {
        //广告请求成功，准备展示，channel：gdt、baidu、xunfei
    }
})
```
