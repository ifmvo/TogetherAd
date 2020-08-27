package com.ifmvo.togetherad.core.listener

import org.jetbrains.annotations.NotNull


/**
 * Created by Matthew Chen on 2020-04-22.
 */
interface RewardListener : BaseListener {

    fun onAdLoaded(@NotNull providerType: String) {}

    fun onAdClicked(@NotNull providerType: String) {}

    fun onAdShow(@NotNull providerType: String) {}

    fun onAdExpose(@NotNull providerType: String) {}

    fun onAdVideoComplete(@NotNull providerType: String) {}

    fun onAdVideoCached(@NotNull providerType: String) {}

    fun onAdRewardVerify(@NotNull providerType: String) {}

    fun onAdClose(@NotNull providerType: String) {}

}