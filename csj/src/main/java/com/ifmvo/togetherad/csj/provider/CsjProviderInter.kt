package com.ifmvo.togetherad.csj.provider

import android.app.Activity
import com.bytedance.sdk.openadsdk.*
import com.ifmvo.togetherad.core.listener.InterListener
import com.ifmvo.togetherad.csj.CsjProvider
import com.ifmvo.togetherad.csj.TogetherAdCsj

/**
 *
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class CsjProviderInter : CsjProviderBanner() {

    private var mTtInteractionAd: TTInteractionAd? = null
    override fun requestInterAd(activity: Activity, adProviderType: String, alias: String, listener: InterListener) {

        callbackInterStartRequest(adProviderType, listener)

        destroyInterAd()

        val adSlot = AdSlot.Builder()
                .setCodeId(TogetherAdCsj.idMapCsj[alias])
                .setSupportDeepLink(CsjProvider.Inter.supportDeepLink)
                .setImageAcceptedSize(CsjProvider.Inter.imageAcceptedSizeWidth, CsjProvider.Inter.imageAcceptedSizeHeight) //根据广告平台选择的尺寸，传入同比例尺寸
                .build()

        TTAdSdk.getAdManager().createAdNative(activity).loadInteractionAd(adSlot, object : TTAdNative.InteractionAdListener {
            override fun onError(errorCode: Int, errorMsg: String?) {
                //出错
                callbackInterFailed(adProviderType, listener, "错误码: $errorCode, 错误信息：$errorMsg")
            }

            override fun onInteractionAdLoad(ttInteractionAd: TTInteractionAd?) {
                //填充
                callbackInterLoaded(adProviderType, listener)

                mTtInteractionAd = ttInteractionAd
                mTtInteractionAd?.setAdInteractionListener(object : TTInteractionAd.AdInteractionListener {
                    override fun onAdDismiss() {
                        //消失
                        callbackInterClosed(adProviderType, listener)
                    }

                    override fun onAdClicked() {
                        //点击
                        callbackInterClicked(adProviderType, listener)
                    }

                    override fun onAdShow() {
                        //曝光
                        callbackInterExpose(adProviderType, listener)
                    }
                })

                if (mTtInteractionAd?.interactionType == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                    mTtInteractionAd?.setDownloadListener(object : TTAppDownloadListener {
                        override fun onIdle() {
                        }

                        override fun onDownloadPaused(totalBytes: Long, currBytes: Long, fileName: String?, appName: String?) {
                        }

                        override fun onDownloadFailed(totalBytes: Long, currBytes: Long, fileName: String?, appName: String?) {
                        }

                        override fun onDownloadActive(totalBytes: Long, currBytes: Long, fileName: String?, appName: String?) {
                        }

                        override fun onDownloadFinished(totalBytes: Long, fileName: String?, appName: String?) {
                        }

                        override fun onInstalled(fileName: String?, appName: String?) {
                        }
                    })
                }
            }
        })
    }

    override fun showInterAd(activity: Activity) {
        mTtInteractionAd?.showInteractionAd(activity)
    }

    override fun destroyInterAd() {
        mTtInteractionAd = null
    }

}