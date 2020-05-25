package com.ifmvo.togetherad.demo

import android.app.ListActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.ifmvo.togetherad.demo.help.HelpActivity
import com.ifmvo.togetherad.demo.native_.NativeRecyclerViewActivity
import com.ifmvo.togetherad.demo.native_.NativeSimpleActivity
import com.ifmvo.togetherad.demo.reward.RewardActivity
import com.ifmvo.togetherad.demo.splash.SplashActivity

class MainActivity : ListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListOf(
                "开屏",
                "原生自渲染简单用法",
                "原生自渲染在 RecyclerView 中使用",
                "激励广告",
                "采坑指南"
        ))
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        when (position) {
            0 -> {
                SplashActivity.action(this)
            }
            1 -> {
                NativeSimpleActivity.action(this)
            }
            2 -> {
                NativeRecyclerViewActivity.action(this)
            }
            3 -> {
                RewardActivity.action(this)
            }
            4 -> {
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