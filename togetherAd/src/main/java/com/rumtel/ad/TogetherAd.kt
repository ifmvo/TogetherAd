package com.rumtel.ad

import android.content.Context
import android.support.annotation.NonNull
import com.baidu.mobads.AdView

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew_Chen on 2018/12/26.
 */
object TogetherAd {

    var idMapBaidu = mutableMapOf<String, String?>()
        private set
    var idMapGDT = mutableMapOf<String, String?>()
        private set
    var idMapXunFei = mutableMapOf<String, String?>()
        private set

    var appIdGDT = ""
        private set

    var timeOutMillis: Long = 3000
        private set

    fun initBaiduAd(@NonNull context: Context, @NonNull baiduAdAppId: String, baiduIdMap: MutableMap<String, String?>) {
        AdView.setAppSid(context, baiduAdAppId)
        idMapBaidu = baiduIdMap
    }

    fun initGDTAd(@NonNull context: Context, @NonNull gdtAdAppId: String, @NonNull gdtIdMap: MutableMap<String, String?>) {
        idMapGDT = gdtIdMap
        appIdGDT = gdtAdAppId
    }

    fun initXunFeiAd(@NonNull context: Context, @NonNull xunfeiIdMap: MutableMap<String, String?>) {
        idMapXunFei = xunfeiIdMap
    }


}