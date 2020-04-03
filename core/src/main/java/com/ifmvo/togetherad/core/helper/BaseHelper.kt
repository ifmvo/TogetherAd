package com.ifmvo.togetherad.core.helper

import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.config.AdProviderLoader
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.AdRandomUtil

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-03.
 */
abstract class BaseHelper {

    fun randomProvider(radio: String?): BaseAdProvider? {
        val currentRadio = if (radio?.isEmpty() != false) TogetherAd.getDefaultProviderRadio() else radio
        val adProviderType = AdRandomUtil.getRandomAdProvider(currentRadio)
        return AdProviderLoader.loadAdProvider(adProviderType)
    }
}