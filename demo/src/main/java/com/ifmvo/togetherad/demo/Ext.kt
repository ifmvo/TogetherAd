package com.ifmvo.togetherad.demo

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-21.
 */
fun AppCompatActivity.addFragment(containerViewId: Int, fragment: Fragment) {
    val transaction = supportFragmentManager.beginTransaction()
    if (!fragment.isAdded) {
        transaction.add(containerViewId, fragment)
    }
    transaction.show(fragment)
    transaction.setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
    transaction.commitAllowingStateLoss()
}