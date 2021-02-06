package com.ifmvo.togetherad.demo

import android.app.ListActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import com.ifmvo.togetherad.demo.other.MainMenuHelper
import com.ifmvo.togetherad.demo.splash.SplashActivity

class MainActivity : ListActivity() {

    private val menu by lazy { intent.getStringExtra("menu") ?: MainMenuHelper.menuMain }

    private val menuList by lazy { MainMenuHelper.map[menu] }

    companion object {
        fun action(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listAdapter = SimpleAdapter(this, menuList, R.layout.list_item_main, arrayOf("title", "desc"), intArrayOf(R.id.text1, R.id.text2))
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        if (menuList == null) return
        val map = menuList!![position]
        val classStr = map["class"]
        if (classStr !is Class<*>) return
        val intent = Intent(this, classStr)
        intent.putExtra("menu", map["clickMenu"])
        startActivity(intent)
    }

    private var lastClickTimeLong = 0L

    override fun onBackPressed() {

        if (System.currentTimeMillis() - lastClickTimeLong > 1000 && menu == MainMenuHelper.menuMain) {
            lastClickTimeLong = System.currentTimeMillis()
            Toast.makeText(this@MainActivity, "再点一次退出", Toast.LENGTH_SHORT).show()
            return
        }

        super.onBackPressed()
    }

}