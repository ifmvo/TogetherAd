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

    /**
     * 位ID的Map
     */
    var idMapBaidu = mutableMapOf<String, String?>()
        private set
    var idMapGDT = mutableMapOf<String, String?>()
        private set
    var idMapXunFei = mutableMapOf<String, String?>()
        private set

    /**
     * 广点通的 AppId
     */
    var appIdGDT = ""
        private set

    /**
     * 超时时间
     */
    var timeOutMillis: Long = 3000
        private set

    /**
     * 初始化广告
     */
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