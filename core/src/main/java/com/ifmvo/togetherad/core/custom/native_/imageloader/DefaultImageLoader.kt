package com.ifmvo.togetherad.core.custom.native_.imageloader

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.ifmvo.togetherad.core.utils.loge
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/* 
 *  默认的图片加载处理
 * 
 * Created by Matthew Chen on 2020-05-15.
 */
class DefaultImageLoader : AdImageLoader {

    object Async {
        internal val cache: Executor = Executors.newCachedThreadPool()
        private val HANDLER = Handler(Looper.getMainLooper())
        internal val main: Executor = Executor { command -> HANDLER.post(command) }
    }

    override fun loadImage(context: Context, imageView: ImageView, imgUrl: String) {
        try {
            Async.cache.execute {
                val url = URL(imgUrl)
                val conn = (url.openConnection() as HttpURLConnection)
                conn.doInput = true
                conn.connect()
                val inputStream = conn.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                Async.main.execute {
                    imageView.setImageBitmap(bitmap)
                }
            }
        } catch (e: Exception) {
            """
    |-------------------------------------------------------------------------------------- 
    |  DefaultImageLoader 默认的图片加载器出现异常，请自行设置图片加载器
    |  设置方式,在 Application 中添加如下：
    |  TogetherAd.setCustomImageLoader(object : AdImageLoader {
    |      override fun loadImage(context: Context, imageView: ImageView, imgUrl: String) {
    |          //以 Glide 为例
    |          Glide.with(context).load(imgUrl).into(imageView)
    |      }
    |  })
    |--------------------------------------------------------------------------------------

""".loge()
            e.printStackTrace()
        }
    }
}