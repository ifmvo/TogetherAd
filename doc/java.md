### Java 调用姿势
1. 自定义广告位
新建一个名字叫做 TogetherAdConst 的类，这个类用来定义所有广告的位置，例：开屏广告、列表信息流 ... 等等
```
public class TogetherAdConst {
    //开屏
    public static final String AD_SPLASH = "ad_splash";
    
    //插屏
    public static final String AD_TIEPIAN_LIVE = "ad_flow_tiepian_live";
    
    //首页信息流
    public static final String AD_FLOW_INDEX = "ad_flow_index";
    
    //直播播放器前贴
    public static final String AD_INTER = "ad_inter";
    
    ......
}
```
> 自定义的广告位用于初始化广告位ID，也被当做请求广告时的参数

2. 初始化
```
//初始化百度Mob广告
Map<String, String> baiduIdMap = new HashMap<>();
baiduIdMap.put(TogetherAdConst.AD_SPLASH, "相应的广告位ID");
baiduIdMap.put(TogetherAdConst.AD_INTER, "xxxxxx");
baiduIdMap.put(TogetherAdConst.AD_FLOW_INDEX, "xxxxxx");
baiduIdMap.put(TogetherAdConst.AD_VIDEO_PRE, "xxxxxx");
TogetherAd.INSTANCE.initBaiduAd(getApplicationContext(), "你的Baidu_MobAds的APPID", baiduIdMap);

//初始化腾讯广点通广告
Map<String, String> gdtIdMap = new HashMap<>();
gdtIdMap.put(TogetherAdConst.AD_SPLASH, "相应的广告位ID");
gdtIdMap.put(TogetherAdConst.AD_INTER, "xxxxxxxxxxxx");
gdtIdMap.put(TogetherAdConst.AD_FLOW_INDEX, "xxxxxxxxxxxx");
gdtIdMap.put(TogetherAdConst.AD_VIDEO_PRE, "xxxxxxxxxxxx");
TogetherAd.INSTANCE.initGDTAd(getApplicationContext(), "你的GDT的APPID", gdtIdMap);

//初始化科大讯飞广告
Map<String, String> iFlyIdMap = new HashMap<>();
iFlyIdMap.put(TogetherAdConst.AD_SPLASH, "相应的广告位ID");
iFlyIdMap.put(TogetherAdConst.AD_INTER, "xxxxxxxxxxxx");
iFlyIdMap.put(TogetherAdConst.AD_FLOW_INDEX, "xxxxxxxxxxxx");
iFlyIdMap.put(TogetherAdConst.AD_VIDEO_PRE, "xxxxxxxxxxxx");
TogetherAd.INSTANCE.initXunFeiAd(getApplicationContext(), iFlyIdMap);
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
TogetherAdSplash.INSTANCE.showAdFull(mContext, splashConfigStr, adConstStr, adsParentLayout, new TogetherAdSplash.AdListenerSplashFull() {
    @Override
    public void onStartRequest(@NotNull String s) {
        //开始请求广告之前，channel：gdt、baidu、xunfei
    }

    @Override
    public void onAdClick(@NotNull String s) {
        //广告被点击之后，channel：gdt、baidu、xunfei
    }

    @Override
    public void onAdFailed(@Nullable String s) {
        //广告加载失败
    }

    @Override
    public void onAdDismissed() {
        //广告倒计时结束
    }

    @Override
    public void onAdPrepared(@NotNull String s) {
        //广告请求成功，准备展示，channel：gdt、baidu、xunfei
    }
});
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
TogetherAdFlow.INSTANCE.getAdList(mContext, "baidu:1,gdt:1,xunfei:1", adConstStr, new TogetherAdFlow.AdListenerList() {
    @Override
    public void onAdFailed(@Nullable String s) {
        //广告加载失败
    }

    @Override
    public void onAdLoaded(@NotNull String s, @NotNull List<?> list) {
        //广告请求成功，准备展示
        //channel：gdt、baidu、xunfei
        //adList：某个平台的广告List，list里面的object：NativeMediaADData 是广点通，NativeResponse 是百度，NativeADDataRef 是科大讯飞
        //可以通过多类型列表进行展示，详细可借鉴 Demo 中的 IndexFragment 
    }

    @Override
    public void onStartRequest(@NotNull String s) {
        //开始请求广告之前，channel：gdt、baidu、xunfei
    }
    // onClick 点击事件需要自行根据各平台的文档处理
});
```
5. 前贴广告的调用姿势
```
TogetherAdPreMovie.INSTANCE.showAdPreMovie(this, "baidu:1,gdt:1,xunfei:1", TogetherAdConst.AD_TIEPIAN_LIVE, adsParentLayout, new TogetherAdPreMovie.AdListenerPreMovie() {
    @Override
    public void onAdClick(@NotNull String s) {
        //广告被点击之后，channel：gdt、baidu、xunfei
    }

    @Override
    public void onAdFailed(@Nullable String s) {
        //广告加载失败
    }

    @Override
    public void onAdDismissed() {
        //广告倒计时结束
    }

    @Override
    public void onAdPrepared(@NotNull String s) {
        //广告请求成功，准备展示，channel：gdt、baidu、xunfei
    }

    @Override
    public void onStartRequest(@NotNull String s) {
        //开始请求广告之前，channel：gdt、baidu、xunfei
    }
});
```

6. 插屏广告调用姿势
```
TogetherAdInter.INSTANCE.showAdInter(this, Config.interAdConfig(), TogetherAdConst.AD_INTER, false, mRlInterAd, new TogetherAdInter.AdListenerInter() {
    @Override
    public void onStartRequest(@NotNull String s) {
        //开始请求广告之前，channel：gdt、baidu、xunfei
    }

    @Override
    public void onAdClick(@NotNull String s) {
        //广告被点击之后，channel：gdt、baidu、xunfei
    }

    @Override
    public void onAdFailed(@Nullable String s) {
        //广告加载失败
    }

    @Override
    public void onAdDismissed() {
        //广告倒计时结束
    }

    @Override
    public void onAdPrepared(@NotNull String s) {
        //广告请求成功，准备展示，channel：gdt、baidu、xunfei
    }
});
```
