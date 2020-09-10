package com.ifmvo.togetherad.core.utils

import android.content.res.Resources
import android.util.TypedValue

/**
 *
 * Created by Matthew Chen on 2020/9/9.
 */
val Float.dp get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)