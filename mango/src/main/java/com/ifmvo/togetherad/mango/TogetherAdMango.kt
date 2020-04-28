package com.ifmvo.togetherad.mango

import android.app.Application
import androidx.annotation.NonNull
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.entity.AdProviderEntity
import com.mangolm.ad.MGMob

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-27.
 */
object TogetherAdMango {

    //广点通
    fun init(@NonNull context: Application, @NonNull adProviderType: String) {
        TogetherAd.addProvider(AdProviderEntity(adProviderType, MangoProvider::class.java.name))
        MGMob.getInstance().init(context)
    }
}