package com.ifmvo.togetherad.gdt

import android.content.Context
import android.support.annotation.NonNull
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.entity.AdProviderEntity
import com.qq.e.comm.managers.GDTADManager

/* 
 *
 * 
 * Created by Matthew Chen on 2020-04-04.
 */
object TogetherAdGdt {

    var idMapGDT = mapOf<String, String>()
        private set

//    /**
//     * 广点通的 AppId
//     */
//    var appIdGDT = ""
//        private set

    //广点通
    fun init(@NonNull context: Context, @NonNull adProviderType: String, @NonNull gdtAdAppId: String, @NonNull gdtIdMap: Map<String, String>) {
        TogetherAd.addProvider(AdProviderEntity(adProviderType, GdtProvider::class.java.name))
        idMapGDT = gdtIdMap
//        appIdGDT = gdtAdAppId
        GDTADManager.getInstance().initWith(context, gdtAdAppId)
    }
}