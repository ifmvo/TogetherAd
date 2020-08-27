package com.ifmvo.togetherad.core.custom.flow

import android.content.Context
import org.jetbrains.annotations.NotNull
import android.widget.ImageView

/**
 * 加载图片的接口
 *
 * Created by Matthew Chen on 2020-05-15.
 */
interface AdImageLoader {

    fun loadImage(@NotNull context: Context, @NotNull imageView: ImageView, @NotNull imgUrl: String)

}