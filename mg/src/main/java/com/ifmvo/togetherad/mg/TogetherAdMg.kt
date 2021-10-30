package com.ifmvo.togetherad.mg

import android.content.Context
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.entity.AdProviderEntity
import com.ifmvo.togetherad.mg.provider.MgProvider
import com.mango.wakeupsdk.ManGoSDK
import com.mango.wakeupsdk.open.error.ErrorMessage
import com.mango.wakeupsdk.open.listener.OnInitListener
import org.jetbrains.annotations.NotNull

object TogetherAdMg {

    var idMapMg = mutableMapOf<String, String>()

    //判断是否初始化了
    var isInit = ManGoSDK.getInstance().isInit

    //初始化监听
    var onInitListener: OnInitListener? = null

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull mgAdAppId: String, @NotNull mgAdAppKey: String) {
        return init(context, adProviderType, mgAdAppId, mgAdAppKey, null, null)
    }

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull mgAdAppId: String, @NotNull mgAdAppKey: String, providerClassPath: String? = null) {
        return init(context, adProviderType, mgAdAppId, mgAdAppKey, null, providerClassPath)
    }

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull mgAdAppId: String, @NotNull mgAdAppKey: String, mgIdMap: Map<String, String>? = null) {
        return init(context, adProviderType, mgAdAppId, mgAdAppKey, mgIdMap, null)
    }

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull mgAdAppId: String, @NotNull mgAdAppKey: String, mgIdMap: Map<String, String>? = null, providerClassPath: String? = null) {
        TogetherAd.addProvider(AdProviderEntity(adProviderType, if (providerClassPath?.isEmpty() != false) MgProvider::class.java.name else providerClassPath))
        mgIdMap?.let { idMapMg.putAll(it) }
        ManGoSDK.getInstance().init(context, mgAdAppId, mgAdAppKey, onInitListener)
    }

}