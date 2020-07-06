package com.ifmvo.togetherad.core.helper

import android.support.annotation.NonNull


/*
 * Created by Matthew Chen on 2020-04-03.
 */
abstract class BaseHelper {

    fun filterType(@NonNull radioMap: Map<String, Int>, adProviderType: String): MutableMap<String, Int> {
        val newRadioMap = mutableMapOf<String, Int>()
        newRadioMap.putAll(radioMap)
        newRadioMap[adProviderType] = 0
        return newRadioMap
    }

}