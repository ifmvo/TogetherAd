package com.ifmvo.togetherad.core.helper

import android.support.annotation.NonNull
import com.ifmvo.togetherad.core.TogetherAd


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

}