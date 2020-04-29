package com.ifmvo.togetherad.demo

import android.app.ListActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import com.ifmvo.togetherad.demo.reward.RewardActivity

class MainActivity : ListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListOf(
//                "开屏",
//                "原生信息流",
                "激励广告"
        ))
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        when (position) {
//            0 -> {
//                SplashActivity.action(this)
//            }
//            1 -> {
//                NativeActivity.action(this)
//            }
            0 -> {
                RewardActivity.action(this)
            }
        }
    }
}