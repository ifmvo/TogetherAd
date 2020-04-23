package com.ifmvo.togetherad.core.custom.flow

import com.ifmvo.togetherad.core._enum.AdProviderType

/*
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-21.
 */
abstract class BaseFlowTemplate {

    abstract fun getFlowView(adProviderType: AdProviderType): BaseFlowView?

}