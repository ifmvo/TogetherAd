package com.ifmvo.togetherad.core.custom.flow

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
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
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}