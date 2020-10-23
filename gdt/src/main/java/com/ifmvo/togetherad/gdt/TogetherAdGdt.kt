package com.ifmvo.togetherad.gdt

import android.content.Context
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.entity.AdProviderEntity
import com.qq.e.comm.managers.GDTADManager
import org.jetbrains.annotations.NotNull

/**
 * 初始化广点通
 *
 * Created by Matthew Chen on 2020-04-04.
 */
object TogetherAdGdt {

    var idMapGDT = mapOf<String, String>()

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull gdtAdAppId: String) {
        init(context, adProviderType, gdtAdAppId, null, null)
    }

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull gdtAdAppId: String, providerClassPath: String? = null) {
        init(context, adProviderType, gdtAdAppId, null, providerClassPath)
    }

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull gdtAdAppId: String, gdtIdMap: Map<String, String>? = null) {
        init(context, adProviderType, gdtAdAppId, gdtIdMap, null)
    }

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull gdtAdAppId: String, gdtIdMap: Map<String, String>? = null, providerClassPath: String? = null) {
        TogetherAd.addProvider(AdProviderEntity(adProviderType, if (providerClassPath?.isEmpty() != false) GdtProvider::class.java.name else providerClassPath))
        gdtIdMap?.let { idMapGDT = it }
        GDTADManager.getInstance().initWith(context, gdtAdAppId)
    }
}