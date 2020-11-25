package com.ifmvo.togetherad.demo.extend

import android.content.Context
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.entity.AdProviderEntity
import org.jetbrains.annotations.NotNull

/**
 *
 * Created by Matthew Chen on 2020/10/23.
 */
object TogetherAdXiaomi {

    var idMapBaidu = mapOf<String, String>()

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull xiaomiAdAppId: String) {
        init(context, adProviderType, xiaomiAdAppId, null, null)
    }

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull xiaomiAdAppId: String, providerClassPath: String? = null) {
        init(context, adProviderType, xiaomiAdAppId, null, providerClassPath)
    }

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull xiaomiAdAppId: String, xiaomiIdMap: Map<String, String>? = null) {
        init(context, adProviderType, xiaomiAdAppId, xiaomiIdMap, null)
    }

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull xiaomiAdAppId: String, xiaomiIdMap: Map<String, String>? = null, providerClassPath: String? = null) {
        TogetherAd.addProvider(AdProviderEntity(adProviderType, if (providerClassPath?.isEmpty() != false) XiaomiProvider::class.java.name else providerClassPath))
        xiaomiIdMap?.let { idMapBaidu = it }

        //小米SDK初始化
    }

}