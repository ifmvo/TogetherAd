package com.ifmvo.togetherad.core.custom.flow

import com.ifmvo.togetherad.core._enum.AdProviderType

/*
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-21.
 */
abstract class BaseNativeTemplate {

    abstract fun getNativeView(adProviderType: AdProviderType): BaseNativeView?

}