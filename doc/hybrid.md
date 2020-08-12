# 混合使用

实际项目中我们可能遇到这样的情况：

广点通的开屏广告ecpm很低，但是它的原生自渲染ecpm很高，那我们是不是可以使用原生自渲染伪装成开屏来代替广点通自己的开屏广告呢？这样收益岂不是更高！

所以 TogetherAd 提供了混合使用的方案：

调用开屏广告时，如果随机到csj和baidu就调用其开屏进行展示，如果随机到gdt就使用原生信息流伪装成开屏再进行展示。

具体可查看 Demo 中 [开屏广告混合使用的示例代码](../demo/src/main/java/com/ifmvo/togetherad/demo/hybrid/SplashHybridActivity.kt)

这里只举了一个开屏的例子，其他广告类型同理。