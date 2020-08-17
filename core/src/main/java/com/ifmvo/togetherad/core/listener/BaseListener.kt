package com.ifmvo.togetherad.core.listener

import android.support.annotation.NonNull

/**
 * Created by Matthew Chen on 2020-04-20.
 */
interface BaseListener {

    fun onAdStartRequest(@NonNull providerType: String) {}

    fun onAdFailedAll() {}

}