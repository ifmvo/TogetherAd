package com.ifmvo.togetherad.demo

import android.app.ListActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.ifmvo.togetherad.demo.banner.BannerActivity
import com.ifmvo.togetherad.demo.hybrid.SplashHybridActivity
import com.ifmvo.togetherad.demo.inter.InterActivity
import com.ifmvo.togetherad.demo.native_.NativeRecyclerViewActivity
import com.ifmvo.togetherad.demo.native_.NativeSimpleActivity
import com.ifmvo.togetherad.demo.other.HelpActivity
import com.ifmvo.togetherad.demo.reward.RewardActivity
import com.ifmvo.togetherad.demo.splash.SplashActivity
import com.ifmvo.togetherad.demo.splash.SplashProActivity

class MainActivity : ListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListOf(
                "开屏（ 请求并展示 ）",
                "开屏（ 请求和展示分开，可实现预加载 ）",
                "开屏&原生混合使用(gdt、csj是原生自渲染伪装成开屏)",
                "原生自渲染简单用法",
                "原生自渲染在 RecyclerView 中使用",
                "激励广告",
                "Banner 横幅广告",
                "Interstitial插屏广告",
                "采坑指南"
        ))
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        when (position) {
            0 -> {
                SplashActivity.action(this)
            }
            1 -> {
                SplashProActivity.action(this)
            }
            2 -> {
                SplashHybridActivity.action(this)
            }
            3 -> {
                NativeSimpleActivity.action(this)
            }
            4 -> {
                NativeRecyclerViewActivity.action(this)
            }
            5 -> {
                RewardActivity.action(this)
            }
            6 -> {
                BannerActivity.action(this)
            }
            7 -> {
                InterActivity.action(this)
            }
            8 -> {
                HelpActivity.action(this)
            }

        }
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