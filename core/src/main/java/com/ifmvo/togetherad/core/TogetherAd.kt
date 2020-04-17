package com.ifmvo.togetherad.core

import androidx.annotation.NonNull
import com.ifmvo.togetherad.core._enum.AdProviderType
import com.ifmvo.togetherad.core.entity.AdProviderEntity
import com.ifmvo.togetherad.core.utils.logi

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-02.
 */
object TogetherAd {

    /**
     * 默认的广告提供商比例
     */
    private var mRadioDefault = "gdt:1,csj:1,baidu:1"

    /**
     * 所有注册的广告提供商
     */
    private var mProviders = mutableMapOf<String, AdProviderEntity>()

    /**
     * 添加广告提供商
     */
    fun addProvider(@NonNull adProviderEntity: AdProviderEntity) {
        mProviders[adProviderEntity.providerType.type] = adProviderEntity
        "添加广告提供商：$adProviderEntity".logi()
    }

    fun getProvider(providerType: AdProviderType): AdProviderEntity? {
        return mProviders[providerType.type]
    }

    /**
     * 全局配置默认比例
     * radio: "gdt:1,csj:1,baidu:1"
     */
    fun setDefaultProviderRadio(@NonNull radio: String) {
        "设置默认广告提供商比例：$radio".logi()
        mRadioDefault = radio
    }

    /**
     * 全局配置默认比例
     *
     * radioMap:
     * val map = HashMap<AdProviderType, Int>()
     * map.put(AdProviderType.GDT, 1)
     * map.put(AdProviderType.CSJ, 1)
     * map.put(AdProviderType.BAIDU, 2)
     */
    fun setDefaultProviderRadio(@NonNull radioMap: Map<AdProviderType, Int>) {
        val radio = StringBuilder()
        radioMap.entries.forEach {
            radio.append("${it.key.type}:${it.value}")
            radio.append(",")
        }
        "设置默认广告提供商比例：$radio".logi()
        mRadioDefault = radio.toString()
    }

    fun getDefaultProviderRadio(): String {
        return mRadioDefault
    }
}