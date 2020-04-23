package com.ifmvo.togetherad.demo.native_

import com.ifmvo.togetherad.baidu.NativeViewBaiduCommon
import com.ifmvo.togetherad.core._enum.AdProviderType
import com.ifmvo.togetherad.core.custom.flow.BaseFlowTemplate
import com.ifmvo.togetherad.core.custom.flow.BaseFlowView
import com.ifmvo.togetherad.csj.NativeViewCsjCommon
import com.ifmvo.togetherad.gdt.NativeViewGdtCommon

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeTemplateCommon : BaseFlowTemplate() {

    override fun getFlowView(adProviderType: AdProviderType): BaseFlowView? {
        return when (adProviderType) {
            AdProviderType.BAIDU -> {
                NativeViewBaiduCommon()
            }
            AdProviderType.GDT -> {
                NativeViewGdtCommon()
            }
            AdProviderType.CSJ -> {
                NativeViewCsjCommon()
            }
            else -> {
                null
            }
        }
    }
}