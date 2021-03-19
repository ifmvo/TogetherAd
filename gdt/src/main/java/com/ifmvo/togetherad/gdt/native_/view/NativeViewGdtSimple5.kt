package com.ifmvo.togetherad.gdt.native_.view

import android.view.Gravity
import android.widget.FrameLayout

/**
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeViewGdtSimple5(onClose: (adProviderType: String) -> Unit = {}) : BaseNativeViewGdt(onClose) {

    /**
     * 重写该方法，可以设置广告Logo标识的位置
     */
    override fun getLogoLayoutParams(): FrameLayout.LayoutParams? {
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.BOTTOM or Gravity.END
        layoutParams.bottomMargin = 100
        return layoutParams
    }
}