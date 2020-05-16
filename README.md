``TogetherAd`` 能够帮助 ``Android`` 开发者``快速``、``便捷``、``灵活``的接入国内主流广告 SDK。

# 特色功能
- **主流SDK随意搭配组合**  
实际项目中，往往会接入多家广告SDK，以实现收益最大化的目的。``TogetherAd``帮助开发者将其集成在一起，开发者可以任选组合进行搭配使用

- ###支持权重配置###  
例如：你认为穿山甲的单价和收入都是最高的，想要多展示一点。可以配置他们的权重。穿山甲：广点通：百度Mob = 2：1：1
``TogetherAd`` 会根据配置的权重随机请求一家平台的广告，如果请求广告的次数是 40000 次。那么实际请求广告的次数就会接近于：20000：10000：10000

- ###支持失败切换###    
如果按照权重随机一家广告平台的广告请求失败了，那么 ``TogetherAd`` 会在其他家平台继续按照权重随机请求

# 接入方法
可以根据自身需求``任选``以下``1至3``个依赖

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
# 有疑问？VX 联系我! 或者加入 QQ 交流群
<img src="img/Wechat.jpeg"  height="200" width="200">
</br>
<img src="img/QQ.png"  height="265" width="200">