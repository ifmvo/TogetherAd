package com.ifmvo.togetherad.core

import androidx.annotation.NonNull
import com.ifmvo.togetherad.core.custom.flow.AdImageLoader
import com.ifmvo.togetherad.core.custom.flow.DefaultImageLoader
import com.ifmvo.togetherad.core.entity.AdProviderEntity
import com.ifmvo.togetherad.core.utils.logi

/* 
 *
 * 
 * Created by Matthew Chen on 2020-04-02.
 */
object TogetherAd {

    /**
     * 自定义公共的的的广告提供商比例
     */
    private val mRadioPublicMap = mutableMapOf<String, Int>()

    /**
     * 所有注册的广告提供商
     */
    var mProviders = mutableMapOf<String, AdProviderEntity>()
        private set

    /**
     * 添加广告提供商
     */
    fun addProvider(@NonNull adProviderEntity: AdProviderEntity) {
        mProviders[adProviderEntity.providerType] = adProviderEntity
        "添加广告提供商：${adProviderEntity.providerType}".logi()
    }

    internal fun getProvider(providerType: String): AdProviderEntity? {
        return mProviders[providerType]
    }

    /**
     * 全局配置默认比例
     *
     * radioMap:
     * val map = HashMap<String, Int>()
     * map.put(AdProviderType.GDT, 1)
     * map.put(AdProviderType.CSJ, 1)
     * map.put(AdProviderType.BAIDU, 2)
     */
    fun setPublicProviderRadio(@NonNull radioMap: Map<String, Int>) {
        val radio = StringBuilder()
        radioMap.entries.forEach {
            radio.append("${it.key}:${it.value}")
            radio.append(",")
        }
        "设置默认广告提供商比例：$radio".logi()

        mRadioPublicMap.clear()
        mRadioPublicMap.putAll(radioMap)
    }

    /**
     * 有自定义的就用自定义的，没有自定义就每个注册的广告商等比
     */
    fun getPublicProviderRadio(): Map<String, Int> {
        return if (mRadioPublicMap.isNotEmpty()) {
            mRadioPublicMap
        } else {
            val defaultMap = mutableMapOf<String, Int>()
            mProviders.entries.forEach {
                defaultMap[it.key] = 1 //所有注册的广告商权重都是 1
            }
            defaultMap
        }
    }

    /**
     * 可自定义图片加载处理
     */
    var mImageLoader: AdImageLoader? = DefaultImageLoader()
        private set

    fun setCustomImageLoader(@NonNull imageLoader: AdImageLoader) {
        mImageLoader = imageLoader
    }
}