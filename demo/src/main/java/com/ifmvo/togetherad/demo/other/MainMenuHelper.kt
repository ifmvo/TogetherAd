package com.ifmvo.togetherad.demo.other

import com.ifmvo.togetherad.demo.MainActivity
import com.ifmvo.togetherad.demo.banner.BannerActivity
import com.ifmvo.togetherad.demo.express.NativeExpressRecyclerViewActivity
import com.ifmvo.togetherad.demo.express.NativeExpressSimpleActivity
import com.ifmvo.togetherad.demo.express2.NativeExpress2RecyclerViewActivity
import com.ifmvo.togetherad.demo.express2.NativeExpress2SimpleActivity
import com.ifmvo.togetherad.demo.fullvideo.FullVideoActivity
import com.ifmvo.togetherad.demo.hybrid.NativeExpressHybridActivity
import com.ifmvo.togetherad.demo.hybrid.SplashHybridActivity
import com.ifmvo.togetherad.demo.hybrid.VerticalPreMovieHybridActivity
import com.ifmvo.togetherad.demo.inter.InterActivity
import com.ifmvo.togetherad.demo.native_.NativeRecyclerViewActivity
import com.ifmvo.togetherad.demo.native_.NativeSimpleActivity
import com.ifmvo.togetherad.demo.reward.RewardActivity
import com.ifmvo.togetherad.demo.splash.SplashActivity
import com.ifmvo.togetherad.demo.splash.SplashHotActivity
import com.ifmvo.togetherad.demo.splash.SplashProActivity

/**
 *
 * Created by Matthew Chen on 2020/11/30.
 */
object MainMenuHelper {

    const val menuMain = "menuMain"
    private const val menuSplash = "menuSplash"
    private const val menuNative = "menuNative"
    private const val menuNativeExpress = "menuNativeExpress"
    private const val menuHybrid = "menuHybrid"

    private val menuMainList = arrayListOf(
            mapOf(
                    "title" to "广告聚合 4.1.4",
                    "desc" to "穿山甲3.5.0.3；优量汇4.333.1203；百度5.91"
            ),
            mapOf(
                    "title" to "开屏",
                    "desc" to "开屏广告以App启动作为曝光时机，提供5s的可感知广告展示。用户可以点击广告跳转到目标页面；或者点击右上角的“跳过”按钮，跳转到app内容首页",
                    "class" to MainActivity::class.java,
                    "clickMenu" to menuSplash
            ),
            mapOf(
                    "title" to "Banner 横幅",
                    "desc" to "Banner广告(横幅广告)位于app顶部、中部、底部任意一处，横向贯穿整个app页面；当用户与app互动时，Banner广告会停留在屏幕上，并可在一段时间后自动刷新",
                    "class" to BannerActivity::class.java
            ),
            mapOf(
                    "title" to "Interstitial 插屏广告",
                    "desc" to "在应用开启、暂停、退出时以半屏或全屏的形式弹出，展示时机巧妙避开用户对应用的正常体验。",
                    "class" to InterActivity::class.java
            ),
            mapOf(
                    "title" to "原生模板",
                    "desc" to "相比于Banner广告、插屏广告等，原生广告提供了更加灵活、多样化的广告样式选择",
                    "class" to MainActivity::class.java,
                    "clickMenu" to menuNativeExpress
            ),
            mapOf(
                    "title" to "原生自渲染",
                    "desc" to "Banner广告、插屏广告等，app会“收到”一个完整的view对象，开发者无法自定义广告view内部的组件布局（文字TextView、图片ImageView），而原生广告自渲染方式支持开发者自由拼合这些素材，最大程度的满足开发需求；与原生广告（模板方式）相比，自渲染方式更加自由灵活，开发者可以使用其为开发者的应用打造自定义的布局样式",
                    "class" to MainActivity::class.java,
                    "clickMenu" to menuNative
            ),
            mapOf(
                    "title" to "激励视频广告",
                    "desc" to "激励视频广告是指将短视频融入到app场景当中，成为app“任务”之一，用户观看短视频广告后可以得到一些应用内奖励",
                    "class" to RewardActivity::class.java
            ),
            mapOf(
                    "title" to "全屏视频广告",
                    "desc" to "对应的是优量汇的插屏全屏视频和穿山甲的全屏视频广告。全屏视频广告和激励视频广告比较像。差别在于全屏视频广告不需要完整的看完视频，就可以关闭广告。",
                    "class" to FullVideoActivity::class.java
            ),
            mapOf(
                    "title" to "混合使用",
                    "desc" to "混合使用就是将不同类型的广告展示在同一个广告位。可以解决一些问题或者可以提高收入，这里就举例一些常用的场景",
                    "class" to MainActivity::class.java,
                    "clickMenu" to menuHybrid
            ),
            mapOf(
                    "title" to "采坑指南",
                    "desc" to "持续更新...",
                    "class" to HelpActivity::class.java
            )
    )

    private val menuSplashList = arrayListOf(
            mapOf(
                    "title" to "请求并展示",
                    "desc" to "请求成功后立即自动展示",
                    "class" to SplashHotActivity::class.java
            ),
            mapOf(
                    "title" to "请求和展示分开",
                    "desc" to "请求成功后需手动调用展示。可实现预加载",
                    "class" to SplashProActivity::class.java
            )
    )

    private val menuNativeList = arrayListOf(
            mapOf(
                    "title" to "原生自渲染",
                    "desc" to "简单用法",
                    "class" to NativeSimpleActivity::class.java
            ),
            mapOf(
                    "title" to "原生自渲染",
                    "desc" to "在 RecyclerView 中使用",
                    "class" to NativeRecyclerViewActivity::class.java
            )
    )

    private val menuNativeExpressList = arrayListOf(
            mapOf(
                    "title" to "原生模板 2.0（ 只支持优量汇、穿山甲 ）",
                    "desc" to "简单用法",
                    "class" to NativeExpress2SimpleActivity::class.java
            ),
            mapOf(
                    "title" to "原生模板 2.0（ 只支持优量汇、穿山甲 ）",
                    "desc" to "在RecyclerView中的用法",
                    "class" to NativeExpress2RecyclerViewActivity::class.java
            ),
            mapOf(
                    "title" to "原生模板 1.0（ 只支持优量汇 ）",
                    "desc" to "简单用法",
                    "class" to NativeExpressSimpleActivity::class.java
            ),
            mapOf(
                    "title" to "原生模板 1.0（ 只支持优量汇 ）",
                    "desc" to "在RecyclerView中的用法",
                    "class" to NativeExpressRecyclerViewActivity::class.java
            )
    )

    private val menuHybridList = arrayListOf(
            mapOf(
                    "title" to "开屏和原生自渲染混合",
                    "desc" to "如果你的开屏ecpm比较低，就可以使用原生自渲染模拟开屏展示，以提高开屏广告位的收入",
                    "class" to SplashHybridActivity::class.java
            ),
            mapOf(
                    "title" to "原生模板1.0和原生模板2.0混合",
                    "desc" to "由于优量汇的原生模板2.0需要向平台申请，所以这里将优量汇的原生模板1.0和穿山甲的原生模板2.0混合使用",
                    "class" to NativeExpressHybridActivity::class.java
            ),
            mapOf(
                    "title" to "激励、全屏视频、原生自渲染 混合",
                    "desc" to "自定义需求",
                    "class" to VerticalPreMovieHybridActivity::class.java
            )

    )

    val map = mapOf(
            menuMain to menuMainList,
            menuSplash to menuSplashList,
            menuNative to menuNativeList,
            menuNativeExpress to menuNativeExpressList,
            menuHybrid to menuHybridList
    )

}