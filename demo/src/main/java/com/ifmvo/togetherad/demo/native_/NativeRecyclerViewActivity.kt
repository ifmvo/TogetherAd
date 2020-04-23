package com.ifmvo.togetherad.demo.native_

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ifmvo.togetherad.demo.R
import com.ifmvo.togetherad.demo.addFragment

/* 
 * (●ﾟωﾟ●)
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

        addFragment(R.id.fgContainer, NativeRecyclerViewFragment())
    }
}