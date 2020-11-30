package com.ifmvo.togetherad.demo

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import com.ifmvo.togetherad.demo.banner.BannerActivity
import com.ifmvo.togetherad.demo.hybrid.SplashHybridActivity
import com.ifmvo.togetherad.demo.inter.InterActivity
import com.ifmvo.togetherad.demo.native_.*
import com.ifmvo.togetherad.demo.express.NativeExpressRecyclerViewActivity
import com.ifmvo.togetherad.demo.express2.NativeExpress2RecyclerViewActivity
import com.ifmvo.togetherad.demo.express2.NativeExpress2SimpleActivity
import com.ifmvo.togetherad.demo.express.NativeExpressSimpleActivity
import com.ifmvo.togetherad.demo.other.HelpActivity
import com.ifmvo.togetherad.demo.reward.RewardActivity
import com.ifmvo.togetherad.demo.splash.SplashActivity
import com.ifmvo.togetherad.demo.splash.SplashProActivity

class MainActivity : ListActivity() {

    private val list = arrayListOf(
            mapOf(
                    "title" to "开屏",
                    "desc" to "请求并展示",
                    "class" to SplashActivity::class.java
            ),
            mapOf(
                    "title" to "开屏",
                    "desc" to "请求和展示分开，可实现预加载",
                    "class" to SplashProActivity::class.java
            ),
            mapOf(
                    "title" to "原生模板 2.0",
                    "desc" to "简单用法",
                    "class" to NativeExpress2SimpleActivity::class.java
            ),
            mapOf(
                    "title" to "原生模板 2.0",
                    "desc" to "在RecyclerView中的用法",
                    "class" to NativeExpress2RecyclerViewActivity::class.java
            ),
            mapOf(
                    "title" to "原生模板 1.0",
                    "desc" to "简单用法",
                    "class" to NativeExpressSimpleActivity::class.java
            ),
            mapOf(
                    "title" to "原生模板 1.0",
                    "desc" to "在RecyclerView中的用法",
                    "class" to NativeExpressRecyclerViewActivity::class.java
            ),
            mapOf(
                    "title" to "原生自渲染",
                    "desc" to "简单用法",
                    "class" to NativeSimpleActivity::class.java
            ),
            mapOf(
                    "title" to "原生自渲染",
                    "desc" to "在 RecyclerView 中使用",
                    "class" to NativeRecyclerViewActivity::class.java
            ),
            mapOf(
                    "title" to "激励广告",
                    "desc" to "",
                    "class" to RewardActivity::class.java
            ),
            mapOf(
                    "title" to "Banner横幅广告",
                    "desc" to "",
                    "class" to BannerActivity::class.java
            ),
            mapOf(
                    "title" to "Interstitial插屏广告",
                    "desc" to "",
                    "class" to InterActivity::class.java
            ),
            mapOf(
                    "title" to "混合使用",
                    "desc" to "gdt是开屏，baidu、csj是原生自渲染伪装成开屏",
                    "class" to SplashHybridActivity::class.java
            ),
            mapOf(
                    "title" to "采坑指南",
                    "desc" to "持续更新...",
                    "class" to HelpActivity::class.java
            )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listAdapter = SimpleAdapter(this, list, R.layout.list_item_main, arrayOf("title", "desc"), intArrayOf(R.id.text1, R.id.text2))
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        val classStr = list[position]["class"] ?: return
        val intent = Intent(this, classStr as Class<*>)
        startActivity(intent)
    }

    private var lastClickTimeLong = 0L

    override fun onBackPressed() {

        if (System.currentTimeMillis() - lastClickTimeLong > 1000) {
            lastClickTimeLong = System.currentTimeMillis()
            Toast.makeText(this@MainActivity, "再点一次退出", Toast.LENGTH_SHORT).show()
            return
        }

        super.onBackPressed()
    }

}