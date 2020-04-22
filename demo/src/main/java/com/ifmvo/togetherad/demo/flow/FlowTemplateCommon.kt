package com.ifmvo.togetherad.demo.flow

import com.ifmvo.togetherad.baidu.FlowViewBaiduCommon
import com.ifmvo.togetherad.core.custom.flow.BaseFlowTemplate
import com.ifmvo.togetherad.core.custom.flow.BaseFlowView
import com.ifmvo.togetherad.csj.FlowViewCsjCommon
import com.ifmvo.togetherad.gdt.FlowViewGdtCommon

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-21.
 */
class FlowTemplateCommon : BaseFlowTemplate() {

    override fun getFlowViewGdt(): BaseFlowView? {
        return FlowViewGdtCommon()
    }

    override fun getFlowViewBaidu(): BaseFlowView? {
        return FlowViewBaiduCommon()
    }

    override fun getFlowViewCsj(): BaseFlowView? {
        return FlowViewCsjCommon()
    }
}