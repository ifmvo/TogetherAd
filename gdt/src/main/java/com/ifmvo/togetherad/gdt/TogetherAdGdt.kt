package com.ifmvo.togetherad.gdt

import android.app.Application
import androidx.annotation.NonNull
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core._enum.AdProviderType
import com.ifmvo.togetherad.core.entity.AdProviderEntity
import com.ifmvo.togetherad.core.utils.logi

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-04.
 */
object TogetherAdGdt {

    var idMapGDT = mutableMapOf<String, String>()
        private set
    /**
     * 广点通的 AppId
     */
    var appIdGDT = ""
        private set

    //广点通
    fun init(@NonNull context: Application, @NonNull gdtAdAppId: String, @NonNull gdtIdMap: MutableMap<String, String>) {

        TogetherAd.addProvider(AdProviderEntity(AdProviderType.GDT, GdtProvider::class.java.name))

        idMapGDT = gdtIdMap
        appIdGDT = gdtAdAppId
        "初始化: ${AdProviderType.GDT.type}".logi()
    }
}