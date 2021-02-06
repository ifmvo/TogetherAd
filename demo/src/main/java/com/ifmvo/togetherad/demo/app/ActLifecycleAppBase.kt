package com.ifmvo.togetherad.demo.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.ifmvo.togetherad.demo.splash.SplashActivity
import com.ifmvo.togetherad.demo.splash.SplashHotActivity
import java.util.concurrent.atomic.AtomicInteger

/**
 * 通过观察所有 Activity 的生命周期，实现热启动监听
 *
 * Created by Matthew_Chen on 2021.02.06.
 */
open class ActLifecycleAppBase : Application() {

    //保存处于活跃状态的 Activity 个数
    private val mActivityCount = AtomicInteger(0)

    //应用退到后台的时间戳
    private var mAppStopTimeMillis = 0L

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            override fun onActivityStarted(activity: Activity?) {
                //热启动 && 应用退到后台时间超过10s
                if (mActivityCount.get() == 0 && System.currentTimeMillis() - mAppStopTimeMillis > 10 * 1000 && activity !is SplashActivity) {
                    SplashHotActivity.action(activity!!)
                }

                //+1
                mActivityCount.getAndAdd(1)
            }

            override fun onActivityStopped(activity: Activity?) {
                //-1
                mActivityCount.getAndDecrement()

                //退到后台，记录时间
                if (mActivityCount.get() == 0) {
                    mAppStopTimeMillis = System.currentTimeMillis()
                }
            }

            override fun onActivityPaused(activity: Activity?) {}
            override fun onActivityResumed(activity: Activity?) {}
            override fun onActivityDestroyed(activity: Activity?) {}
            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}
            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {}
        })
    }
}