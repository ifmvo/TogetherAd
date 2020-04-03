package com.ifmvo.togetherad.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ifmvo.togetherad.baidu.BaiduProvider
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core._enum.AdProviderType
import com.ifmvo.togetherad.core.entity.AdProviderEntity
import com.ifmvo.togetherad.core.helper.AdHelper
import com.ifmvo.togetherad.core.listener.CommonListener
import com.ifmvo.togetherad.csj.CsjProvider
import com.ifmvo.togetherad.gdt.GdtProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TogetherAd.addProvider(AdProviderEntity(AdProviderType.GDT, GdtProvider::class.java.name))
        TogetherAd.addProvider(AdProviderEntity(AdProviderType.CSJ, CsjProvider::class.java.name))
        TogetherAd.addProvider(AdProviderEntity(AdProviderType.BAIDU, BaiduProvider::class.java.name))

        val map = HashMap<AdProviderType, Int>()
        map.put(AdProviderType.GDT, 1)
        map.put(AdProviderType.CSJ, 1)
        map.put(AdProviderType.BAIDU, 2)

        TogetherAd.setDefaultProviderRadio(mapOf(
                AdProviderType.GDT to 1,
                AdProviderType.CSJ to 1,
                AdProviderType.BAIDU to 0
        ))

        TogetherAd.setDefaultProviderRadio("gdt:1,csj:1,baidu:0")

        AdHelper.showSplashAd(activity = this, alias = "", container = adContainer, listener = object : CommonListener {
            override fun onStartRequest(providerType: AdProviderType) {
            }

            override fun onAdPrepared(providerType: AdProviderType) {
            }

            override fun onAdClick(providerType: AdProviderType) {
            }

            override fun onAdFailed(failedMsg: String?) {
            }

            override fun onAdDismissed(providerType: AdProviderType) {
            }
        })
    }
}
