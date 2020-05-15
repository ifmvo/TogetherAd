package com.ifmvo.togetherad.demo.native_

import android.app.ListActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView

/* 
 *
 * 
 * Created by Matthew Chen on 2020-04-20.
 */
class NativeActivity : ListActivity() {

    companion object {
        fun action(context: Context) {
            context.startActivity(Intent(context, NativeActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListOf(
                "原生信息流简单用法",
                "原生信息流在 RecyclerView 中使用"
        ))
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        when (position) {
            0 -> {
                NativeSimpleActivity.action(this)
            }
            1 -> {
                NativeRecyclerViewActivity.action(this)
            }
        }
    }
}