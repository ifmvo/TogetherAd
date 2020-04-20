package com.ifmvo.togetherad.demo.flow

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-20.
 */
class FlowSimpleActivity : AppCompatActivity() {

    companion object {
        fun action(context: Context) {
            context.startActivity(Intent(context, FlowSimpleActivity::class.java))
        }
    }

}