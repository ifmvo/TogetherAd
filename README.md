# TogetherAd 
[![](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-%E9%99%88%E9%93%AD%E5%8D%93-yellowgreen.svg?style=popout-square&logo=appveyor)](https://blog.csdn.net/ifmvo)

TogetherAd 是一个对所有广告和随机展示逻辑进行封装的一个 Lib

# 效果图
<div align="center">
<img src="https://img-blog.csdnimg.cn/20190102131814805.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2lmbXZv,size_16,color_FFFFFF,t_70" height="330" width="190" >
<img src="https://img-blog.csdnimg.cn/20190102131608648.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2lmbXZv,size_16,color_FFFFFF,t_70" height="330" width="190" >
<img src="https://img-blog.csdnimg.cn/20190102131901955.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2lmbXZv,size_16,color_FFFFFF,t_70" height="330" width="190" >
<img src="https://img-blog.csdnimg.cn/20190102131931566.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2lmbXZv,size_16,color_FFFFFF,t_70" height="330" width="190" >
</div>

# 使用方法
```
implementation 'com.github.ifmvo:TogetherAd:1.0.3'
```

### Java 调用姿势
参数以及作用见相关类的注释即可

1. Lib 初始化操作
```
Map<String, String> baiduIdMap = new HashMap<>();
baiduIdMap.put(TogetherAdConst.AD_SPLASH, "xxxxxx");
baiduIdMap.put(TogetherAdConst.AD_INTER, "xxxxxx");
baiduIdMap.put(TogetherAdConst.AD_FLOW_INDEX, "xxxxxx");
baiduIdMap.put(TogetherAdConst.AD_TIEPIAN_LIVE, "xxxxxx");
TogetherAd.INSTANCE.initBaiduAd(getApplicationContext(), "xxxxxx", baiduIdMap);

Map<String, String> gdtIdMap = new HashMap<>();
baiduIdMap.put(TogetherAdConst.AD_SPLASH, "xxxxxxxxxxxx");
baiduIdMap.put(TogetherAdConst.AD_INTER, "xxxxxxxxxxxx");
baiduIdMap.put(TogetherAdConst.AD_FLOW_INDEX, "xxxxxxxxxxxx");
baiduIdMap.put(TogetherAdConst.AD_TIEPIAN_LIVE, "xxxxxxxxxxxx");
TogetherAd.INSTANCE.initGDTAd(getApplicationContext(), "xxxxxxxxxxxx", gdtIdMap);

Map<String, String> iFlyIdMap = new HashMap<>();
baiduIdMap.put(TogetherAdConst.AD_SPLASH, "xxxxxxxxxxxx");
baiduIdMap.put(TogetherAdConst.AD_INTER, "xxxxxxxxxxxx");
baiduIdMap.put(TogetherAdConst.AD_FLOW_INDEX, "xxxxxxxxxxxx");
baiduIdMap.put(TogetherAdConst.AD_TIEPIAN_LIVE, "xxxxxxxxxxxx");
TogetherAd.INSTANCE.initXunFeiAd(getApplicationContext(), iFlyIdMap);
```

### Kotlin 的调用姿势

1. 初始化操作
```
val baiduIdMap = mutableMapOf<String, String?>(
    TogetherAdConst.AD_SPLASH to "xxxxxx",
    TogetherAdConst.AD_INTER to "xxxxxx",
    TogetherAdConst.AD_FLOW_INDEX to "xxxxxx",
    TogetherAdConst.AD_TIEPIAN_LIVE to "xxxxxx"
)
TogetherAd.initBaiduAd(applicationContext, "xxxxxx", baiduIdMap)

val gdtIdMap = mutableMapOf<String, String?>(
    TogetherAdConst.AD_SPLASH to "xxxxxx",
    TogetherAdConst.AD_INTER to "xxxxxx",
    TogetherAdConst.AD_FLOW_INDEX to "xxxxxx",
    TogetherAdConst.AD_TIEPIAN_LIVE to "xxxxxx"
)
TogetherAd.initGDTAd(applicationContext, "xxxxxx", gdtIdMap)

val xunFeiIdMap = mutableMapOf<String, String?>(
    TogetherAdConst.AD_SPLASH to "xxxxxx",
    TogetherAdConst.AD_INTER to "xxxxxx",
    TogetherAdConst.AD_FLOW_INDEX to "xxxxxx",
    TogetherAdConst.AD_TIEPIAN_LIVE to "xxxxxx"
)
TogetherAd.initXunFeiAd(applicationContext, xunFeiIdMap)
```

2. 开屏广告的调用方法
```
TogetherAdSplash.showAdFull(this, splashConfigAd, TogetherAdConst.AD_SPLASH, mFlAdContainer, object : TogetherAdSplash.AdListenerSplashFull {
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
        //广告倒计时消失
    }

    override fun onAdPrepared(channel: String) {
        //广告请求成功，准备展示，channel：gdt、baidu、xunfei
    }
})
```


# 目前支持的广告
百度Mob、腾讯GDT、科大讯飞

# 随机广告配置的规则
假如有 BAIDU，GDT，ADVIEW 这三种广告，实际的配置字符串应该是这样的："baidu:3,gdt:3,adview:4"   

1. 随机广告配置必须符合这样的格式  
"xxx:m,yyy:n,zzz:i"

2. AdRandomUtil 这个类只会识别特定的 key （ 例：baidu、gdt、adview ）  
"baidu:2,gdt:8" <==>  "baidu:2,gdt:8,abc:3" （  abc 会被忽略 ）  

3. key 区分大小写  
"BAIDU:2,GDT:8"  ≠  "baidu:2,gdt:8"  
"Baidu:2,Gdt:8"  ≠  "baidu:2,gdt:8"  

# 广告切源的逻辑以及实际实现的方式
假如有 BAIDU，GDT，ADVIEW 这三种广告 （ 实际的配置字符串："baidu:3,gdt:3,adview:4" ）   

第一次随机到了 GDT，如果 GDT 请求失败，将 GDT 的 key 使用一个通用的字符串替换，再从其他的广告中再随机   
此时的配置字符串："baidu:3,HIDE:3,adview:4"  

第二次随机到了 BAIDU，如果 BAIDU 也请求失败了，将 BAIDU 的 key 使用一个通用的字符串替换，再从其他的广告中再随机    
此时的配置字符串："HIDE:3,HIDE:3,adview:4"  
......  
直到请求某个广告成功后停止   
如果所有的广告全部失败，此时的配置字符串："HIDE:3,HIDE:3,HIDE:4"  

# License
```
Copyright 2018 陈铭卓

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
