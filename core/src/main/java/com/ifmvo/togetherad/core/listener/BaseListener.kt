package com.ifmvo.togetherad.core.listener

import org.jetbrains.annotations.NotNull


/**
 * Created by Matthew Chen on 2020-04-20.
 */
interface BaseListener {

    fun onAdStartRequest(@NotNull providerType: String) {}

    fun onAdFailedAll() {}

    fun onAdFailed(@NotNull providerType: String, failedMsg: String?) {}

}