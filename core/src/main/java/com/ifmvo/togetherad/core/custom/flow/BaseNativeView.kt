package com.ifmvo.togetherad.core.custom.flow

import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.view.ViewGroup
import com.ifmvo.togetherad.core.listener.NativeViewListener

/*
 * Created by Matthew Chen on 2020-04-21.
 */
abstract class BaseNativeView {

    abstract fun showNative(@NonNull adProviderType: String, @NonNull adObject: Any, @NonNull container: ViewGroup, @Nullable listener: NativeViewListener? = null)

}