package com.rumtel.ad;

import android.app.Activity;
import android.view.ViewGroup;
import com.baidu.mobads.SplashAd;
import com.baidu.mobads.SplashAdListener;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;

/*
 * (●ﾟωﾟ●) 请求开屏广告
 *
 * Created by Matthew_Chen on 2018/8/8.
 */
public class AdHelperSplashFull {

    /**
     * 显示开屏广告
     * "baidu:2,gdt:8"
     *
     * @param activity        activity
     * @param adsParentLayout 广告容器
     * @param adListener      监听
     */
    public static void showAdFull(Activity activity, String splashConfigStr, ViewGroup adsParentLayout, final AdListenerSplashFull adListener) {

        AdNameType randomAdName = AdRandomUtil.INSTANCE.getRandomAdName(splashConfigStr);
        switch (randomAdName) {
            case BAIDU:
                showAdFullBaiduMob(activity, adsParentLayout, adListener);
                break;
            case GDT:
                showAdFullTecentGDT(activity, adsParentLayout, adListener);
                break;
            default:
                adListener.onAdFailed("AdNameType.NO");
                break;
        }
    }

    /**
     * 开屏广告 腾讯广点通
     * onNoAD(AdError error)	广告加载失败，error对象包含了错误码和错误信息，错误码的详细内容可以参考文档第5章
     * onADDismissed()	广告关闭时调用，可能是用户关闭或者展示时间到。此时一般需要跳过开屏的Activity，进入应用内容页面
     * onADPresent()	广告成功展示时调用，成功展示不等于满足计费条件即有效曝光（如展示时长尚未满足）
     * onADClicked()	广告被点击时调用，不代表满足计费条件（如点击时网络异常）
     * onADExposure()	广告曝光时调用，此处的曝光不等于有效曝光（如展示时长未满足）
     * onADTick(long millisUntilFinished)	倒计时回调，返回广告还将被展示的剩余时间，单位是ms
     */
    private static void showAdFullTecentGDT(Activity activity, ViewGroup adsParentLayout, final AdListenerSplashFull adListener) {
        adListener.onStartRequest(AdConfig.GDT_AD_NAME);

        new SplashAD(activity, adsParentLayout, AdConfig.GDT_AD_APP_ID, AdConfig.GDT_AD_SPLASH, new SplashADListener() {
            @Override
            public void onADDismissed() {
                adListener.onAdDismissed();
            }

            @Override
            public void onNoAD(AdError adError) {
                adListener.onAdFailed(adError.getErrorMsg());
            }

            @Override
            public void onADPresent() {
                adListener.onAdPrepared(AdConfig.GDT_AD_NAME);
            }

            @Override
            public void onADClicked() {
                adListener.onAdClick(AdConfig.GDT_AD_NAME);
            }

            @Override
            public void onADTick(long l) {
                //倒计时
            }

            public void onADExposure() {
                //倒计时
            }
        });
    }

    /**
     * 开屏广告 百度Mob
     */
    private static void showAdFullBaiduMob(Activity activity, ViewGroup adsParentLayout, final AdListenerSplashFull adListener) {
        adListener.onStartRequest(AdConfig.BAIDU_AD_NAME);
        new SplashAd(activity, adsParentLayout, new SplashAdListener() {
            @Override
            public void onAdPresent() {
                adListener.onAdPrepared(AdConfig.BAIDU_AD_NAME);
            }

            @Override
            public void onAdDismissed() {
                adListener.onAdDismissed();
            }

            @Override
            public void onAdFailed(String s) {
                adListener.onAdFailed(s);
            }

            @Override
            public void onAdClick() {
                adListener.onAdClick(AdConfig.BAIDU_AD_NAME);
            }

        }, AdConfig.BAIDU_AD_SPLASH, true);
    }

    public interface AdListenerSplashFull {
        void onStartRequest(String channel);

        /**
         * 渠道 百度、广点通、。。。。
         */
        void onAdClick(String channel);

        void onAdFailed(String failedMsg);

        void onAdDismissed();

        void onAdPrepared(String channel);
    }
}
