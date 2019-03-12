# TogetherAd 
[![](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-%E9%99%88%E9%93%AD%E5%8D%93-yellowgreen.svg?style=popout-square&logo=appveyor)](https://blog.csdn.net/ifmvo)

TogetherAd 封装了多种广告的 SDK，可以自行控制请求各种广告次数的比例

### 目前支持的广告
百度Mob、腾讯GDT、科大讯飞

### 功能介绍

多种平台随机展示
> 因为各个平台分发广告的量实际上有可能不够用，所以多种广告根据一定比例随机展示会使收益最大化

控制个平台广告的展示比例
> 因为各个平台分发广告的量是不一样的，比如广点通分配1000次，百度只有100次，那么展示广点通和百度广告的次数必然是10：1才能使收益最大化

广告失败切换
> 如果某个平台的广告请求失败或没有量，会自动在其他广告中随机出一种再次请求，这样可以尽可能多的展示广告，使收益最大化

### 效果图
<div align="center">
<img src="/img/img_splash.jpeg" height="330" width="190" >
<img src="/img/img_flow.jpeg" height="330" width="190" >
<img src="/img/img_premovie.jpeg" height="330" width="190" >
<img src="/img/img_inter.jpeg" height="330" width="190" >
</div>

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
    implementation 'com.github.ifmvo:TogetherAd:1.1.8'
}
```
### 使用方法
[Java](doc/java.md)  
[Kotlin](doc/kotlin.md)

### 随机广告配置的规则
假如有 BAIDU，GDT，ADVIEW 这三种广告，实际的配置字符串应该是这样的："baidu:3,gdt:3,adview:4"   

1. 随机广告配置必须符合这样的格式  
"xxx:m,yyy:n,zzz:i"

2. AdRandomUtil 这个类只会识别特定的 key （ 例：baidu、gdt、adview ）  
"baidu:2,gdt:8" <==>  "baidu:2,gdt:8,abc:3" （  abc 会被忽略 ）  

3. key 区分大小写  
"BAIDU:2,GDT:8"  ≠  "baidu:2,gdt:8"  
"Baidu:2,Gdt:8"  ≠  "baidu:2,gdt:8"  

### 广告切源的逻辑以及实际实现的方式
假如有 BAIDU，GDT，ADVIEW 这三种广告 （ 实际的配置字符串："baidu:3,gdt:3,adview:4" ）   

第一次随机到了 GDT，如果 GDT 请求失败，将 GDT 的 key 使用一个通用的字符串替换，再从其他的广告中再随机   
此时的配置字符串："baidu:3,HIDE:3,adview:4"  

第二次随机到了 BAIDU，如果 BAIDU 也请求失败了，将 BAIDU 的 key 使用一个通用的字符串替换，再从其他的广告中再随机    
此时的配置字符串："HIDE:3,HIDE:3,adview:4"  
......  
直到请求某个广告成功后停止   
如果所有的广告全部失败，此时的配置字符串："HIDE:3,HIDE:3,HIDE:4"  

### 计划功能
1. 提供自定义前贴广告的布局功能
2. 可选择性添加广点通、百度、科大讯飞的广告

### 对 TogetherAd 有疑问？
欢迎 Issues 和 Star，或添加 TogetherAd 交流群  
<img src="doc/img/TogetherAd-QQ.png" >

### License
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
