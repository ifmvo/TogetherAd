package com.ifmvo.togetherad.gdt

import android.app.Application
import androidx.annotation.NonNull
import com.ifmvo.togetherad.core._enum.AdProviderType

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
    fun initGDTAd(@NonNull context: Application, @NonNull gdtAdAppId: String, @NonNull gdtIdMap: MutableMap<String, String>) {
        idMapGDT = gdtIdMap
        appIdGDT = gdtAdAppId
        logd("初始化: ${AdProviderType.GDT.type}")
    }
}