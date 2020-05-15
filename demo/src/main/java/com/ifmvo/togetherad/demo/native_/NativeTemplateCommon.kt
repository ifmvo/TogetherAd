package com.ifmvo.togetherad.demo.native_

import com.ifmvo.togetherad.baidu.NativeViewBaiduCommon
import com.ifmvo.togetherad.core.custom.flow.BaseNativeTemplate
import com.ifmvo.togetherad.core.custom.flow.BaseNativeView
import com.ifmvo.togetherad.csj.NativeViewCsjCommon
import com.ifmvo.togetherad.demo.AdProviderType
import com.ifmvo.togetherad.gdt.NativeViewGdtCommon

/*
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeTemplateCommon : BaseNativeTemplate() {

    override fun getNativeView(adProviderType: String): BaseNativeView? {
        return when (adProviderType) {
            AdProviderType.BAIDU.type -> {
                NativeViewBaiduCommon()
            }
            AdProviderType.GDT.type -> {
                NativeViewGdtCommon()
            }
            AdProviderType.CSJ.type -> {
                NativeViewCsjCommon()
            }
            else -> {
                null
            }
        }
    }
}