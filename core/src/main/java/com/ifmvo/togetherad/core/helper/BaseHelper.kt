package com.ifmvo.togetherad.core.helper

import android.os.CountDownTimer
import android.support.annotation.NonNull
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.listener.BaseListener


/*
 * Created by Matthew Chen on 2020-04-03.
 */
abstract class BaseHelper {

    fun filterType(@NonNull radioMap: Map<String, Int>, adProviderType: String): MutableMap<String, Int> {
        val newRadioMap = mutableMapOf<String, Int>()
        newRadioMap.putAll(radioMap)
        newRadioMap[adProviderType] = 0

        //不允许失败切换的时候，将所有广告提供商的权重都清空
        if (!TogetherAd.failedSwitchEnable) {
            newRadioMap.keys.forEach { newRadioMap[it] = 0 }
        }

        return newRadioMap
    }

//    private var mTimer: CountDownTimer? = null
//
//    fun startTimer(millisInFuture: Long, listener: BaseListener?) {
//        cancelTimer()
//        mTimer = object : CountDownTimer(millisInFuture, 1000) {
//            override fun onFinish() {
//                listener?.onAdFailedAll()
//            }
//
//            override fun onTick(millisUntilFinished: Long) {
//
//            }
//        }
//        mTimer?.start()
//    }
//
//    fun cancelTimer() {
//        mTimer?.cancel()
//    }


}