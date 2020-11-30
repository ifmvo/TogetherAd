package com.ifmvo.togetherad.core.custom.express

import android.view.ViewGroup
import org.jetbrains.annotations.NotNull

/*
 * Created by Matthew Chen on 2020-04-21.
 */
abstract class BaseNativeExpressView {

    abstract fun showNativeExpress(@NotNull adProviderType: String, @NotNull adObject: Any, @NotNull container: ViewGroup)

}