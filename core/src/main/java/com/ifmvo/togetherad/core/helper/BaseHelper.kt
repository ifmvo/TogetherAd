package com.ifmvo.togetherad.core.helper

import android.os.CountDownTimer
import org.jetbrains.annotations.NotNull
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.listener.BaseListener
import com.ifmvo.togetherad.core.utils.logd
import com.ifmvo.togetherad.core.utils.logi
import com.ifmvo.togetherad.core.utils.logv


/*
 * Created by Matthew Chen on 2020-04-03.
 */
abstract class BaseHelper {

    /**
     * 将传进来的 adProviderType 权重设置为 0，其他不变
     * 如果是不允许失败切换的时候，将所有广告提供商的权重都清空
     */
    fun filterType(@NotNull ratioMap: Map<String, Int>, adProviderType: String): MutableMap<String, Int> {
        val newRatioMap = mutableMapOf<String, Int>()
        newRatioMap.putAll(ratioMap)
        newRatioMap[adProviderType] = 0

        //不允许失败切换的时候，将所有广告提供商的权重都清空
        if (!TogetherAd.failedSwitchEnable) {
            newRatioMap.keys.forEach { newRatioMap[it] = 0 }
        }

        return newRatioMap
    }

    private var mTimer: CountDownTimer? = null
    var isFetchOverTime = false

    /**
     * 启动超时计时
     */
    fun startTimer(listener: BaseListener?) {
        //0 就不开启倒计时
        if (TogetherAd.maxFetchDelay <= 0L) {
            return
        }

        cancelTimer()
        "开始倒计时：${TogetherAd.maxFetchDelay}".logv()
        mTimer = object : CountDownTimer(TogetherAd.maxFetchDelay, 1000) {
            override fun onFinish() {
                "倒计时结束".logv()
                "请求超时".logi()
                isFetchOverTime = true
                listener?.onAdFailedAll()
            }

            override fun onTick(millisUntilFinished: Long) {
                "倒计时：$millisUntilFinished".logv()
            }
        }
        isFetchOverTime = false
        mTimer?.start()
    }

    /**
     * 取消超时计时
     */
    fun cancelTimer() {
        mTimer?.cancel()
        mTimer = null
    }


}