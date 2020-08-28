package com.ifmvo.togetherad.demo.native_

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.helper.AdHelperNativePro
import com.ifmvo.togetherad.core.listener.NativeViewListener
import com.ifmvo.togetherad.core.utils.ScreenUtil
import com.ifmvo.togetherad.demo.R

/**
 * 一个普通的多类型列表的 Adapter
 *
 * Created by Matthew Chen on 2020/6/30.
 */
class NativeAdapter(list: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val ITEM_VIEW_TYPE_AD = 0
        private const val ITEM_VIEW_TYPE_CONTENT = 1
    }

    private var mList: List<Any> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_VIEW_TYPE_CONTENT) {
            ContentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_native_content, parent, false))
        } else {
            AdViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_native_ad, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM_VIEW_TYPE_CONTENT -> {
                val contentViewHolder = holder as ContentViewHolder
                val contentDataEntity = mList[position] as ContentDataEntity
                contentViewHolder.imageView.layoutParams.height = ScreenUtil.getDisplayMetricsWidth(contentViewHolder.imageView.context) * 9 / 16
                TogetherAd.mImageLoader?.loadImage(contentViewHolder.imageView.context, contentViewHolder.imageView, contentDataEntity.imgUrl)
                contentViewHolder.textView.text = contentDataEntity.title
            }
            ITEM_VIEW_TYPE_AD -> {
                val adViewHolder = holder as AdViewHolder
                adViewHolder.adContainer.removeAllViews()
                AdHelperNativePro.show(mList[position], adViewHolder.adContainer, NativeTemplateSimple1(), object : NativeViewListener {
                    override fun onAdExposed(providerType: String) {
                        Toast.makeText(adViewHolder.adContainer.context, "原生广告曝光了：$providerType", Toast.LENGTH_LONG).show()
                    }

                    override fun onAdClicked(providerType: String) {
                        Toast.makeText(adViewHolder.adContainer.context, "原生广告点击了：$providerType", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mList[position] is ContentDataEntity) ITEM_VIEW_TYPE_CONTENT else ITEM_VIEW_TYPE_AD
    }
}

class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var adContainer: ViewGroup = itemView.findViewById(R.id.adContainer)
}

class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var imageView: ImageView = itemView.findViewById(R.id.img)
    var textView: TextView = itemView.findViewById(R.id.txt)
}