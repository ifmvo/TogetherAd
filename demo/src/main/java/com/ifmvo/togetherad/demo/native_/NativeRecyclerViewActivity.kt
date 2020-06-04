package com.ifmvo.togetherad.demo.native_

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ifmvo.togetherad.demo.R

/**
 * 具体用法在 Fragment 中
 *
 * Created by Matthew Chen on 2020-04-20.
 */
class NativeRecyclerViewActivity : AppCompatActivity() {

    companion object {
        fun action(context: Context) {
            context.startActivity(Intent(context, NativeRecyclerViewActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)

//        addFragment(R.id.fgContainer, NativeRecyclerViewFragment())
    }
}