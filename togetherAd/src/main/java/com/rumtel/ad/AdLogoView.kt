package com.rumtel.ad

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.baidu.mobad.feeds.NativeResponse
import com.ifmvo.imageloader.ILFactory
import com.rumtel.ad.other.AdNameType

/* 
 * (●ﾟωﾟ●) 这是一个展示广告 Logo 的 自定义View
 * 
 * Created by Matthew Chen on 2020-03-27.
 */
class AdLogoView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    /**
     * 百度
     */
    private var mContainerLogoBaidu: ViewGroup? = null
    private var mIvLogoBaidu: ImageView? = null
    private var mIvAdBaidu: ImageView? = null

    /**
     * 穿山甲
     */
    private var mContainerLogoCsj: ViewGroup? = null
    private var mIvLogoCsj: ImageView? = null


    init {
        val view = View.inflate(context, R.layout.view_ad_logo, this)
        mContainerLogoBaidu = view.findViewById(R.id.container_logo_baidu)
        mIvLogoBaidu = view.findViewById(R.id.img_logo_baidu)
        mIvAdBaidu = view.findViewById(R.id.img_ad)

        mContainerLogoCsj = view.findViewById(R.id.container_logo_csj)
        mIvLogoCsj = view.findViewById(R.id.img_logo_csj)
    }


    /**
     * 设置广告的类型，就会展示相应的 Logo
     * adObject: 对应广告的对象，必须和类型一致，可传 null
     */
    fun setAdLogoType(adNameType: AdNameType, adObject: Any? = null) {

        setAdLogoVisibility(adNameType)

        when (adNameType) {
            AdNameType.BAIDU -> {
                if (adObject == null) {
                    return
                }
                adObject as NativeResponse
                ILFactory.getLoader().load(context, mIvLogoBaidu, adObject.baiduLogoUrl)
                ILFactory.getLoader().load(context, mIvAdBaidu, adObject.adLogoUrl)
            }
            AdNameType.CSJ -> {
                mIvLogoCsj?.setImageResource(R.drawable.ic_ad_logo_csj)
            }
            AdNameType.GDT -> {
            }
            AdNameType.NO -> {
            }
        }
    }

    /**
     * 设置可见性
     */
    private fun setAdLogoVisibility(adNameType: AdNameType) {
        when (adNameType) {
            AdNameType.BAIDU -> {
                mContainerLogoBaidu?.visibility = View.VISIBLE
                mContainerLogoCsj?.visibility = View.GONE
            }
            AdNameType.CSJ -> {
                mContainerLogoBaidu?.visibility = View.GONE
                mContainerLogoCsj?.visibility = View.VISIBLE
            }
            AdNameType.GDT -> {
                this.visibility = View.GONE
            }
            AdNameType.NO -> {
                this.visibility = View.GONE
            }
        }
    }
}