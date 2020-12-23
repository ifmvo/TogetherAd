package com.ifmvo.togetherad.core.listener

import org.jetbrains.annotations.NotNull


/**
 * Created by Matthew Chen on 2020-04-20.
 */
interface BaseListener {

    /**
     * 开始请求前回调
     */
    fun onAdStartRequest(@NotNull providerType: String) {}

    /**
     * 所有的提供商都请求失败，或请求超时，或没有分配任何广告商比例
     */
    fun onAdFailedAll(failedMsg: String?) {}

    /**
     * 单个提供商请求失败
     */
    fun onAdFailed(@NotNull providerType: String, failedMsg: String?) {}

}