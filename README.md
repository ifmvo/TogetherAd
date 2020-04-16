![Logo](doc/img/TogetherAd-Logo.png)

TogetherAd 封装了多种广告的 SDK，可以自行控制请求各种广告次数的比例，可以自定义广告位。

### 文档收集
- 广点通的文档
1. https://developers.adnet.qq.com/backend/error_code.html  （ 错误码对照 ）
2. https://developers.adnet.qq.com/doc/android/union/union_debug#sdk%20%E9%94%99%E8%AF%AF%E7%A0%81

- 穿山甲的文档
1. http://partner.toutiao.com/doc?id=5de4cc6d78c8690012a90aa5   （ 错误码对照 ）

### 目前支持的广告
百度Mob、腾讯GDT、穿山甲

### 功能介绍

控制个平台广告的展示比例
> 因为各个平台分发广告的量是不一样的，比如广点通分配1000次，百度只有100次，那么展示广点通和百度广告的次数必然是10：1才能使收益最大化

广告失败切换
> 如果某个平台的广告请求失败或没有量，会自动在其他广告中随机出一种再次请求，这样可以尽可能多的展示广告，使收益最大化

### Gradle集成方法
项目根目录下 build.gradle 中
```
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

Module 目录下 build.gradle 中
```
dependencies {
    implementation 'com.github.ifmvo:TogetherAd:lastVersion'
}
```
### 使用方法
[Java](doc/java.md)  
[Kotlin](doc/kotlin.md)

### 关于 Log
如果你看不到广告或者广告展示异常，你可以在 Logcat 中过滤 ``TogetherAd`` 你就会看到类似下面这样的日志：
```
2019-03-12 14:18:40.238 8724-8724/xxx D/TogetherAd: AdRandomUtil: 广告的配置：baidu:0,gdt:9,xunfei:1
2019-03-12 14:18:40.239 8724-8724/xxx D/TogetherAd: AdRandomUtil: 随机到的广告: gdt
2019-03-12 14:18:40.909 8724-8724/xxx D/TogetherAd: TogetherAdSplash: gdt: 准备好了
2019-03-12 14:18:45.970 8724-8724/xxx D/TogetherAd: TogetherAdSplash: gdt: 曝光成功了
2019-03-12 14:18:45.972 8724-8724/xxx D/TogetherAd: TogetherAdSplash: gdt: 消失了
```

### 随机广告配置的规则
假如有 BAIDU，GDT，CSJ 这三种广告，实际的配置字符串应该是这样的："baidu:3,gdt:3,csj:4"   

1. 随机广告配置必须符合这样的格式  
"xxx:m,yyy:n,zzz:i"

2. AdRandomUtil 这个类只会识别特定的 key （ 例：baidu、gdt、adview ）  
"baidu:2,gdt:8" <==>  "baidu:2,gdt:8,abc:3" （  abc 会被忽略 ）  

3. key 区分大小写  
"BAIDU:2,GDT:8"  ≠  "baidu:2,gdt:8"  
"Baidu:2,Gdt:8"  ≠  "baidu:2,gdt:8"  

### 广告切源的逻辑以及实际实现的方式
假如有 BAIDU，GDT，CSJ 这三种广告 （ 实际的配置字符串："baidu:3,gdt:3,csj:4" ）   

第一次随机到了 GDT，如果 GDT 请求失败，将 GDT 的 key 使用一个通用的字符串替换，再从其他的广告中再随机   
此时的配置字符串："baidu:3,HIDE:3,csj:4"  

第二次随机到了 BAIDU，如果 BAIDU 也请求失败了，将 BAIDU 的 key 使用一个通用的字符串替换，再从其他的广告中再随机    
此时的配置字符串："HIDE:3,HIDE:3,csj:4"  
......  
直到请求某个广告成功后停止   
如果所有的广告全部失败，此时的配置字符串："HIDE:3,HIDE:3,HIDE:4"  

### 计划功能
目前虽然可以满足公司现有项目的需求，但是还有很多方面还需要改善和优化，例：  

1. 提供自定义前贴广告的布局功能
2. 可选择性添加广点通、百度、穿山甲的广告

接下来一一实现

### 特殊处理
1. 前贴其实就是使用信息流+倒计时进行处理
2. 广点通和穿山甲的插屏广告也是使用 信息流 + 关闭按钮 的方式伪装出来的

### 有疑问？VX 联系我!
![WeChat](img/Wechat.jpeg)

### 更新日志
v2.0.9
1. 前贴视频广告播放完成之后自动消失

v2.0.8
1. 前贴视频广告倒计时5秒之后出现关闭按钮

v2.0.7 
1. 添加了一种在界面中间显示的类型，TogetherAdMid

v2.0.5 
1. 添加了一个自定义 AdLogoView 方便管理各个平台广告的标示

v2.0.3
1. 广告标示：百度是用SDK && 穿山甲添加"广告"文字标示

v2.0.0
1. TogetherAdBanner2 中广点通目前使用的就是 Banner2.0
2. 广点通自渲染1.0即将关停。信息流、插屏、前贴，这三种广告的广点通改为 自渲染2.0

v1.6.8
1. 更新项目的 gradle 版本，以解决找不到百度 FileProvider 的问题
2. 更新穿山甲到 v2.8.0.3
3. 更新广点通到 v4.176.1046

v1.6.7
1. 更新穿山甲到 v2.5.3.2

v1.6.6
1. 修复百度的开屏广告

v1.6.5
1. 更新三种广告的SDK到最新版本

v1.6.4
1. 修复 广点通 Banner 广告，刷新失败后一直回调 onError，导致请求错乱的问题

v1.6.2
1. 开屏广告的跳过按钮可null，并且为可选参数

v1.5.1
1. 修复一个 bug

v1.5.0
1. TogetherAdPreMovie 请求到的csj广告无论有没有倒计时都可能展示视频广告
2. TogetherAdPreMovie 请求到的所有广告点击不消失

v1.4.9
1. 前贴广告没有倒计时的时候，也可以展示视频，点击不消失

v1.4.8
1. 穿山甲广告初始化可选 .useTextureView(useTextureView)

v1.4.4
1. 穿山甲异常捕获，捕获后切广告

v1.4.3
1. 只有前贴广告才能展示视频广告 （ 前贴会根据 needTimer 参数判断是否展示视频 ）
2. 信息流也不展示视频

v1.4.2
1. TogetherAdBanner    csj 用的 信息流;  TogetherAdBanner2   csj 用的 Banner原生

v1.4.0
1. 修复前贴广告穿山甲没有点击回调bug

v1.3.9
1. 视频前贴广告支持无倒计时模式 （ 可以用来实现再一次退出、搜索界面等. 只展示一个原生的情况 ）
2. 修复其他bug  