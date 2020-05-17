# TogetherAd

TogetherAd 能够帮助 Android 开发者``快速``、``便捷``、``灵活``的接入国内主流广告 SDK。

## 特色功能

###1. 主流SDK随意搭配组合

实际项目中，往往会接入多家广告SDK，以实现收益最大化的目的。

``TogetherAd``帮助开发者将其集成在一起，开发者可以任选组合进行搭配使用

###2. 支持权重配置

因为各个平台分发广告的量以及价格都是不一样的，所以需要动态配置请求的比例。

例如：有三家广告平台 A、B、C，你认为 A 的单价和收入都是最高的，想要多展示一点。

那么可以配置他们的权重：A：B：C = 2：1：1

``TogetherAd`` 会根据配置的权重随机请求一家平台的广告，如果请求广告的总数是 40000 次。

那么每家平台请求的次数就会趋近于：A: 20000, B:10000, C:10000

###3. 支持失败切换

如果某个平台的广告请求失败或没有量，会自动在其他广告中随机出一种再次请求，这样可以尽可能多的展示广告，使收益最大化

## 接入姿势

###1.根据自身需求``任选``以下 1 至 3 个依赖随意组合搭配

```
dependencies {

    //穿山甲（ 头条 ）
    implementation 'com.matthewchen.togetherad:csj:3.0.0'
    
    //优量汇（ 腾讯广点通 ）
    implementation 'com.matthewchen.togetherad:gdt:3.0.0'
    
    //百青藤 ( 百度 Mob )
    implementation 'com.matthewchen.togetherad:baidu:3.0.0'
    
}
```

###2.下载对应广告平台的aar文件

请到各个平台官网下载SDK，或者[点击这里获取aar文件](demo/libs)

将对应的aar文件添加到项目的libs文件夹中

<img src="img/aars-to-libs.png"  height="200" width="400">

###3. 引入aar文件
```
dependencies {

	//这句代码的意思是：将libs文件夹中的 所有aar和jar文件 依赖到项目中
	implementation fileTree(dir: 'libs', include: ['*.jar', '*.aar'])
	
	...
}
```
> 征集：寻找将aar打包到依赖里面的方法，以优化接入的步骤。有方案者请联系我，将获取精美咖啡一杯。
> 感激！！

## 支持的广告类型以及使用方法

[开屏广告](doc/splash.md)

[原生自渲染](doc/native.md)

[激励广告](doc/reward.md)

## 扩展
``TogetherAd``目前支持的广告平台有：穿山甲、广点通、百青藤。

如果你想接入其他广告平台，或者自己有API组装。可参考[这里进行自定义扩展](doc/expend.md)。

## 相关文档收集

[广点通接入文档](https://developers.adnet.qq.com/doc/android/access_doc)

[广点通错误码对照](https://developers.adnet.qq.com/backend/error_code.html)

[穿山甲错误码对照](http://partner.toutiao.com/doc?id=5de4cc6d78c8690012a90aa5)

[百青藤v5.85接入文档](https://baidu-ssp.gz.bcebos.com/mssp/sdk/BaiduMobAds_MSSP_bd_SDK_android_v5.85.pdf)

>会尽量保持最新。如果发现有新版本也可联系我，我会及时更新，感激！！

## 有疑问？欢迎提交 Issue，或者VX 联系我，也可加入 QQ 交流群

<img src="img/Wechat.jpeg"  height="200" width="200">
</br>
<img src="img/QQ.png"  height="265" width="200">