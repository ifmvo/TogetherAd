package com.ifmvo.togetherad.core.config

import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import java.lang.reflect.Constructor

/* 
 * Created by Matthew Chen on 2020-04-03.
 */
object AdProviderLoader {

    fun loadAdProvider(providerType: String): BaseAdProvider? {
        var adProvider: BaseAdProvider? = null
        try {
            val providerInstance = getProviderInstance(providerType)
            if (providerInstance is BaseAdProvider) {
                adProvider = providerInstance
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return adProvider
    }

    private fun getProviderInstance(providerType: String): Any? {
        var instance: Any? = null
        try {
            TogetherAd.getProvider(providerType)?.classPath?.let { classPath ->
                getSDKClass(classPath)?.let { clz ->
                    instance = getConstructor(clz)?.newInstance()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return instance
    }

    private fun getConstructor(clz: Class<*>): Constructor<*>? {
        var result: Constructor<*>? = null
        try {
            result = clz.getConstructor()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    private fun getSDKClass(classPath: String): Class<*>? {
        var result: Class<*>? = null
        try {
            result = Class.forName(classPath)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        return result
    }

}