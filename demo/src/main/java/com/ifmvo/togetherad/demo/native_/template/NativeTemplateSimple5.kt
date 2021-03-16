package com.ifmvo.togetherad.demo.native_.template

import com.ifmvo.togetherad.baidu.native_.view.NativeViewBaiduSimple5
import com.ifmvo.togetherad.core.custom.native_.BaseNativeTemplate
import com.ifmvo.togetherad.core.custom.native_.BaseNativeView
import com.ifmvo.togetherad.csj.native_.view.NativeViewCsjSimple5
import com.ifmvo.togetherad.demo.app.AdProviderType
import com.ifmvo.togetherad.gdt.native_.view.NativeViewGdtSimple5

/*
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeTemplateSimple5(onClose: (providerType: String) -> Unit = {}) : BaseNativeTemplate() {

    private var mOnClose = onClose

    override fun getNativeView(adProviderType: String): BaseNativeView? {
        return when (adProviderType) {
            AdProviderType.GDT.type -> {
                NativeViewGdtSimple5(mOnClose)
            }
            AdProviderType.CSJ.type -> {
                NativeViewCsjSimple5(mOnClose)
            }
            AdProviderType.BAIDU.type -> {
                NativeViewBaiduSimple5(mOnClose)
            }
            else -> throw Exception("模板配置错误")
        }
    }
}