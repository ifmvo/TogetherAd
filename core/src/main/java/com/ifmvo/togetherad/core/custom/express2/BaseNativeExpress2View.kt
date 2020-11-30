package com.ifmvo.togetherad.core.custom.express2

import android.app.Activity
import android.view.ViewGroup
import com.ifmvo.togetherad.core.listener.NativeExpress2ViewListener
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

/*
 * Created by Matthew Chen on 2020-04-21.
 */
abstract class BaseNativeExpress2View {

    abstract fun showNativeExpress2(@NotNull activity: Activity, @NotNull adProviderType: String, @NotNull adObject: Any, @NotNull container: ViewGroup, @Nullable listener: NativeExpress2ViewListener? = null)

}