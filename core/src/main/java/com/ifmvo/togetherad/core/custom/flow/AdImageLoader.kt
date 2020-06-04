package com.ifmvo.togetherad.core.custom.flow

import android.content.Context
import android.support.annotation.NonNull
import android.widget.ImageView

/**
 * 加载图片的接口
 *
 * Created by Matthew Chen on 2020-05-15.
 */
interface AdImageLoader {

    fun loadImage(@NonNull context: Context, @NonNull imageView: ImageView, @NonNull imgUrl: String)

}