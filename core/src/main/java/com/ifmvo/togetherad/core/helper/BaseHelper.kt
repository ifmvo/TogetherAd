package com.ifmvo.togetherad.core.helper

import androidx.annotation.NonNull

/*
 * Created by Matthew Chen on 2020-04-03.
 */
abstract class BaseHelper {

    fun filterType(@NonNull radioMap: Map<String, Int>, adProviderType: String): MutableMap<String, Int> {
        radioMap.entries.forEach {
            "前：${it.key} ${it.value}"
        }
        val newRadioMap = mutableMapOf<String, Int>()
        newRadioMap.putAll(radioMap)
        newRadioMap[adProviderType] = 0

        newRadioMap.entries.forEach {
            "后：${it.key} ${it.value}"
        }
        return newRadioMap
    }

}