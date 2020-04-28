package com.ifmvo.togetherad.core.helper

import androidx.annotation.NonNull

/*
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-03.
 */
abstract class BaseHelper {

    fun filterType(@NonNull radioMap: Map<String, Int>, adProviderType: String): MutableMap<String, Int> {
        val newRadioMap = mutableMapOf<String, Int>()
        newRadioMap.putAll(radioMap)
        newRadioMap.remove(adProviderType)
        return newRadioMap
    }

}