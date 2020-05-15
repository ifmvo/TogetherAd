package com.ifmvo.togetherad.gdt

import android.app.Application
import androidx.annotation.NonNull
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.entity.AdProviderEntity

/* 
 *
 * 
 * Created by Matthew Chen on 2020-04-04.
 */
object TogetherAdGdt {

    var idMapGDT = mapOf<String, String>()
        private set
    /**
     * 广点通的 AppId
     */
    var appIdGDT = ""
        private set

    //广点通
    fun init(@NonNull context: Application, @NonNull providerType: String, @NonNull gdtAdAppId: String, @NonNull gdtIdMap: Map<String, String>) {
        TogetherAd.addProvider(AdProviderEntity(providerType, GdtProvider::class.java.name))
        idMapGDT = gdtIdMap
        appIdGDT = gdtAdAppId
    }
}