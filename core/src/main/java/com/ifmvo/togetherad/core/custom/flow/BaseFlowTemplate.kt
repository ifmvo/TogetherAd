package com.ifmvo.togetherad.core.custom.flow

/*
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-21.
 */
abstract class BaseFlowTemplate {

    open fun getFlowViewGdt(): BaseFlowView? {
        return null
    }

    open fun getFlowViewBaidu(): BaseFlowView? {
        return null
    }

    open fun getFlowViewCsj(): BaseFlowView? {
        return null
    }

}