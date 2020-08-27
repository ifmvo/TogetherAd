package com.ifmvo.togetherad.gdt

import android.content.Context
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.entity.AdProviderEntity
import com.qq.e.comm.managers.GDTADManager
import org.jetbrains.annotations.NotNull

/* 
 *
 * 
 * Created by Matthew Chen on 2020-04-04.
 */
object TogetherAdGdt {

    var idMapGDT = mapOf<String, String>()

    //照顾Java朋友
    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull gdtAdAppId: String) {
        init(context, adProviderType, gdtAdAppId, null)
    }

    //广点通
    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull gdtAdAppId: String, gdtIdMap: Map<String, String>? = null) {
        TogetherAd.addProvider(AdProviderEntity(adProviderType, GdtProvider::class.java.name))
        gdtIdMap?.let { idMapGDT = it }
        GDTADManager.getInstance().initWith(context, gdtAdAppId)
    }
}