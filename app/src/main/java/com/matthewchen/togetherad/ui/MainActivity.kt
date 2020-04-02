package com.matthewchen.togetherad.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.matthewchen.togetherad.R
import com.matthewchen.togetherad.utils.Kits

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew_Chen on 2019/1/2.
 */
class MainActivity : AppCompatActivity() {

    object MainAct {
        fun action(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Kits.StatuBar.immersive(this)

        setContentView(R.layout.activity_main)

        replaceFragment(R.id.fl_container, IndexFragment())
    }

    private fun replaceFragment(contentLayoutId: Int, fragment: Fragment?) {
        if (fragment == null) {
            return
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(contentLayoutId, fragment, fragment.javaClass.name)
        transaction.commit()
    }

    private var lastTimeMillis: Long = 0
    override fun finish() {
        if (System.currentTimeMillis() - lastTimeMillis >= 1500) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show()
            lastTimeMillis = System.currentTimeMillis()
            return
        }
        super.finish()
    }

}