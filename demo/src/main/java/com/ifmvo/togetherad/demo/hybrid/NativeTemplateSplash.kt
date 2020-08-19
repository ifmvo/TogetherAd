package com.ifmvo.togetherad.demo.hybrid

import com.ifmvo.togetherad.core.custom.flow.BaseNativeTemplate
import com.ifmvo.togetherad.core.custom.flow.BaseNativeView
import com.ifmvo.togetherad.demo.AdProviderType
import com.ifmvo.togetherad.demo.hybrid.NativeViewGdtSplash

/*
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeTemplateSplash(onDismiss: (providerType: String) -> Unit) : BaseNativeTemplate() {

    private var mOnDismiss: (providerType: String) -> Unit = onDismiss

    override fun getNativeView(adProviderType: String): BaseNativeView? {
        return when (adProviderType) {
            AdProviderType.GDT.type -> {
                NativeViewGdtSplash(mOnDismiss)
            }
            AdProviderType.CSJ.type -> {
                NativeViewCsjSplash(mOnDismiss)
            }
            else -> throw Exception("模板配置错误")
        }
    }
}