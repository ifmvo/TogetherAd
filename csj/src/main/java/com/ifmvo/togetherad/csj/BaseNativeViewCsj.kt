package com.ifmvo.togetherad.csj

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAppDownloadListener
import com.bytedance.sdk.openadsdk.TTNativeAd
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.custom.flow.BaseNativeView
import com.ifmvo.togetherad.core.listener.NativeViewListener

/**
 *
 * Created by Matthew Chen on 2020/9/27.
 */
abstract class BaseNativeViewCsj : BaseNativeView() {

    var rootView: View? = null

    open fun getLayoutRes(): Int {
        return R.layout.layout_native_view_csj
    }

    //广告Logo
    open fun getAdLogoImageView(): ImageView? {
        return rootView?.findViewById(R.id.csj_ad_logo)
    }

    //标题文字
    open fun getTitleTextView(): TextView? {
        return rootView?.findViewById(R.id.csj_tv_title)
    }

    //描述文字
    open fun getDescTextView(): TextView? {
        return rootView?.findViewById(R.id.csj_tv_desc)
    }

    //描述文字
    open fun getSourceTextView(): TextView? {
        return rootView?.findViewById(R.id.csj_tv_source)
    }

    //icon 图
    open fun getIconImageView(): ImageView? {
        return rootView?.findViewById(R.id.csj_img_logo)
    }

    //主图1
    open fun getMainImageView_1(): ImageView? {
        return rootView?.findViewById(R.id.csj_img_poster1)
    }

    //主图2
    open fun getMainImageView_2(): ImageView? {
        return rootView?.findViewById(R.id.csj_img_poster2)
    }

    //主图3
    open fun getMainImageView_3(): ImageView? {
        return rootView?.findViewById(R.id.csj_img_poster2)
    }

    //视频容器
    open fun getVideoContainer(): ViewGroup? {
        return rootView?.findViewById(R.id.csj_video_container)
    }

    //图片容器
    open fun getImageContainer(): ViewGroup? {
        return rootView?.findViewById(R.id.csj_img_container)
    }

    open fun getActionButton(): Button? {
        return rootView?.findViewById(R.id.csj_btn_action)
    }

    override fun showNative(adProviderType: String, adObject: Any, container: ViewGroup, listener: NativeViewListener?) {
        if (adObject !is TTNativeAd) {
            return
        }

        //findView
        rootView = View.inflate(container.context, getLayoutRes(), container)

        //AdLogo
        getAdLogoImageView()?.setImageBitmap(adObject.adLogo)

        //Icon
        getIconImageView()?.let { TogetherAd.mImageLoader?.loadImage(container.context, it, adObject.icon.imageUrl) }

        //标题和描述
        getTitleTextView()?.text = adObject.title
        getDescTextView()?.text = adObject.description
        getSourceTextView()?.text = if (adObject.source?.isNotEmpty() == true) adObject.source else "广告来源"
        getActionButton()?.text = getActionBtnText(adObject)

        adObject.setDownloadListener(object: TTAppDownloadListener{
            override fun onIdle() {
                getActionButton()?.text = "开始下载"
            }

            override fun onDownloadPaused(totalBytes: Long, currBytes: Long, fileName: String?, appName: String?) {
                getActionButton()?.text = "下载暂停"
            }

            override fun onDownloadFailed(totalBytes: Long, currBytes: Long, fileName: String?, appName: String?) {
                getActionButton()?.text = "重新下载"
            }

            override fun onDownloadActive(p0: Long, p1: Long, p2: String?, p3: String?) {
                getActionButton()?.text = "下载中"
            }

            override fun onDownloadFinished(totalBytes: Long, fileName: String?, appName: String?) {
                getActionButton()?.text = "点击安装"
            }

            override fun onInstalled(fileName: String?, appName: String?) {
                getActionButton()?.text = "点击打开"
            }
        })

        // 可以被点击的view, 也可以把convertView放进来意味整个item可被点击，点击会跳转到落地页
        val clickViewList = mutableListOf<View>()
        clickViewList.add(rootView!!)
        // 创意点击区域的view 点击根据不同的创意进行下载或拨打电话动作
        //如果需要点击图文区域也能进行下载或者拨打电话动作，请将图文区域的view传入creativeViewList
        val creativeViewList = mutableListOf<View>()
        getActionButton()?.let { creativeViewList.add(it) }
        // 注册普通点击区域，创意点击区域。重要! 这个涉及到广告计费及交互，必须正确调用。convertView必须使用ViewGroup。
        adObject.registerViewForInteraction(rootView as ViewGroup, clickViewList, creativeViewList, object : TTNativeAd.AdInteractionListener {
            override fun onAdClicked(view: View, ad: TTNativeAd) {
                // 点击普通区域的回调
            }

            override fun onAdCreativeClick(view: View, ad: TTNativeAd) {
                // 点击创意区域的回调
                listener?.onAdClicked(adProviderType)
            }

            override fun onAdShow(ad: TTNativeAd) {
                // 广告曝光展示的回调
                listener?.onAdExposed(adProviderType)
            }
        })

        when (adObject.imageMode) {
            //视频类型
            TTAdConstant.IMAGE_MODE_VIDEO, TTAdConstant.IMAGE_MODE_VIDEO_VERTICAL -> {
                getImageContainer()?.visibility = View.VISIBLE
                getVideoContainer()?.visibility = View.GONE
                getMainImageView_2()?.visibility = View.GONE
                getMainImageView_3()?.visibility = View.GONE
                val videoCoverImage = adObject.videoCoverImage

                //信息流如果是视频的话，就只展示视频封面
                if (videoCoverImage != null && videoCoverImage.imageUrl != null) {
                    getMainImageView_1()?.let {
                        TogetherAd.mImageLoader?.loadImage(container.context, it, videoCoverImage.imageUrl)
                    }
                }
            }
            //单个图片的类型
            TTAdConstant.IMAGE_MODE_LARGE_IMG, TTAdConstant.IMAGE_MODE_SMALL_IMG, TTAdConstant.IMAGE_MODE_VERTICAL_IMG -> {
                getImageContainer()?.visibility = View.VISIBLE
                getVideoContainer()?.visibility = View.GONE
                getMainImageView_2()?.visibility = View.GONE
                getMainImageView_3()?.visibility = View.GONE
                val imageList = adObject.imageList

                if (imageList.isNotEmpty() && imageList[0] != null && imageList[0].isValid) {
                    getMainImageView_1()?.let {
                        TogetherAd.mImageLoader?.loadImage(container.context, it, imageList[0].imageUrl)
                    }
                }
            }
            //多个图片的类型
            TTAdConstant.IMAGE_MODE_GROUP_IMG -> {
                getImageContainer()?.visibility = View.VISIBLE
                getVideoContainer()?.visibility = View.GONE
                getMainImageView_2()?.visibility = View.VISIBLE
                getMainImageView_3()?.visibility = View.VISIBLE
                val imageList = adObject.imageList

                if (imageList.isNotEmpty() && imageList[0] != null && imageList[0].isValid) {
                    getMainImageView_1()?.let {
                        TogetherAd.mImageLoader?.loadImage(container.context, it, imageList[0].imageUrl)
                    }
                }
                if (imageList.isNotEmpty() && imageList.size > 1 && imageList[1] != null && imageList[1].isValid) {
                    getMainImageView_2()?.let {
                        TogetherAd.mImageLoader?.loadImage(container.context, it, imageList[1].imageUrl)
                    }
                }
                if (imageList.isNotEmpty() && imageList.size > 2 && imageList[2].isValid) {
                    getMainImageView_3()?.let {
                        TogetherAd.mImageLoader?.loadImage(container.context, it, imageList[2].imageUrl)
                    }
                }
            }
        }
    }

    /**
     * 根据广告的状态返回按钮应该展示的文字
     */
    open fun getActionBtnText(ad: TTNativeAd): String {
        return when (ad.interactionType) {
            TTAdConstant.INTERACTION_TYPE_DOWNLOAD -> {
                "下载"
            }
            TTAdConstant.INTERACTION_TYPE_DIAL -> {
                "立即拨打"
            }
            TTAdConstant.INTERACTION_TYPE_LANDING_PAGE, TTAdConstant.INTERACTION_TYPE_BROWSER -> {
                "查看详情"
            }
            else -> {
                "查看详情"
            }
        }
    }

}