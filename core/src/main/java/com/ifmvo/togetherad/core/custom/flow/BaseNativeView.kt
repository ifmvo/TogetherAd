package com.ifmvo.togetherad.core.custom.flow

import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.ifmvo.togetherad.core.listener.NativeViewListener

/*
 * Created by Matthew Chen on 2020-04-21.
 */
abstract class BaseNativeView {

    abstract fun showNative(@NonNull adProviderType: String, @NonNull adObject: Any, @NonNull container: ViewGroup, @Nullable listener: NativeViewListener? = null)

}