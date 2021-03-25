# TogetherAd

TogetherAd 是由 Kotlin 编写的 Android 广告聚合开源项目。

能够帮助 Android 开发者``快速``、``便捷``、``灵活``的 ``接入并使用`` 国内多家主流广告 SDK。

### 安装 Demo

微信扫描底部二维码，回复 ``apk`` 可下载 Demo Apk 尝鲜

### Gradle 添加依赖

根据自身需求``任选``以下 1 至 3 个依赖，随意组合搭配

```gradle
dependencies {

    //穿山甲（ 头条 ）
    implementation 'com.matthewchen.togetherad:csj:4.1.8-3.5.0.3'
    
    //优量汇（ 腾讯广点通 ）
    implementation 'com.matthewchen.togetherad:gdt:4.1.8-4.333.1203'
    
    //百青藤 ( 百度 Mob )
    implementation 'com.matthewchen.togetherad:baidu:4.1.8-5.91'
    
}
```

> 版本号的规则：TogetherAd版本-对应广告商的SDK版本号

### 使用方法

- [特色功能](doc/feature.md)

- [准备工作及初始化](doc/prepare.md)

- [开屏广告](doc/splash.md)

- [Banner横幅广告](doc/banner.md)

- [Interstitial插屏广告](doc/inter.md)

- [原生模板](doc/express.md)

- [原生自渲染](doc/native.md)

- [激励广告](doc/reward.md)

- [全屏视频广告](doc/full_video.md)

- [混合使用](doc/hybrid.md)

- [扩展](doc/extend.md)

- [常见问题](doc/question.md)

- [版本更新日志](doc/update_log.md)

### 混淆 proguard-rules.pro

资源库中已自带混淆规则，通常情况下无需手动配置。

当然你也可以点击这里查看每个资源库的混淆规则： [core](core/proguard-rules.pro)、[gdt](gdt/proguard-rules.pro)、[csj](csj/proguard-rules.pro)、[baidu](baidu/proguard-rules.pro)

### 相关文档收集

- [优量汇接入文档](https://developers.adnet.qq.com/doc/android/access_doc)

- [优量汇常见问题](https://e.qq.com/dev/help_detail.html?cid=668&pid=2208)

- [优量汇SDK修订历史](https://developers.adnet.qq.com/doc/android/union/union_version)

- [优量汇错误码对照](https://developers.adnet.qq.com/backend/error_code.html)

- [穿山甲文档](http://partner.toutiao.com/doc?id=5dd0fe756b181e00112e3ec5)

- [百青藤v5.88接入文档](https://baidu-ssp.gz.bcebos.com/mssp/sdk/BaiduMobAds_MSSP_bd_SDK_android_v5.88.pdf)

### 有疑问？

微信扫描下面二维码, **关注后点击联系我** 可邀请进微信交流群，更多大佬为你答疑。

<img src="img/qrcode_for_gh_e66be0cfb1f0_258.jpeg"  height="200" width="200">

### License

```
MIT License

Copyright (c) 2021 陈铭卓

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```