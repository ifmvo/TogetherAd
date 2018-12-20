package com.rumtel.ad;

import android.app.Activity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.baidu.mobads.AdSize;
import com.baidu.mobads.InterstitialAd;
import com.baidu.mobads.InterstitialAdListener;
import com.qq.e.ads.interstitial.InterstitialAD;
import com.qq.e.ads.interstitial.InterstitialADListener;
import com.qq.e.comm.util.AdError;

/*
 * (●ﾟωﾟ●) 插屏广告
 *
 * Created by Matthew_Chen on 2018/8/14.
 */
public class AdHelperInterstitial {

    public static void showAdInterstitial(Activity activity, String interstitialConfigStr, boolean isLandscape, RelativeLayout adIntersContainer, final AdListenerInterstitial adListener){

        //目前这个版本先这样写，横屏下广点通大概率展示不出来的问题
        if (isLandscape && !TextUtils.isEmpty(interstitialConfigStr)){
            interstitialConfigStr = interstitialConfigStr.replace("gdt", AdConfig.MASK_NAME);
        }

        AdNameType randomAdName = AdRandomUtil.INSTANCE.getRandomAdName(interstitialConfigStr);
        switch (randomAdName){
            case BAIDU:
                showAdInterstitialBaiduMob(activity, interstitialConfigStr, isLandscape, adIntersContainer, adListener);
                break;
            case GDT:
                showAdInterstitialTecentGDT(activity, interstitialConfigStr, isLandscape, adIntersContainer, adListener);
                break;
            default:
                adListener.onAdFailed("AdNameType.NO");
                break;
        }
    }

    /**
         onNoAD(AdError error)	广告加载失败，error对象包含了错误码和错误信息，错误码的详细内容可以参考文档第5章
         onADReceive()	插屏广告加载完毕，此回调后才可以调用show方法
         onADOpened()	插屏广告展开时回调
         onADExposure()	插屏广告曝光时回调
         onADClicked()	插屏广告点击时回调
         onADClosed()	插屏广告关闭时回调
         onADLeftApplication()	插屏广告点击离开应用时回调
     */
    private static void showAdInterstitialTecentGDT(final Activity activity, final String interstitialConfigStr, final boolean isLandscape, final RelativeLayout adIntersContainer, final AdListenerInterstitial adListener) {

        adListener.onStartRequest(AdConfig.GDT_AD_NAME);
        final InterstitialAD iad = new InterstitialAD(activity, AdConfig.GDT_AD_APP_ID, AdConfig.GDT_AD_INTER);
        iad.setADListener(new InterstitialADListener() {
            @Override
            public void onADReceive() {
                iad.show();
            }

            @Override
            public void onNoAD(AdError error) {
                Log.i("InterstitialAdTecentGDT", String.format("LoadInterstitialAd Fail, error code: %d, error msg: %s", error.getErrorCode(), error.getErrorMsg()));
                String newConfigStr = interstitialConfigStr.replace("gdt", AdConfig.MASK_NAME);
                showAdInterstitial(activity, newConfigStr, isLandscape, adIntersContainer, adListener);
            }

            @Override
            public void onADOpened() {
                Log.i("InterstitialAdTecentGDT", "onADOpened");
            }

            @Override
            public void onADExposure() {
                Log.i("InterstitialAdTecentGDT", "onADExposure");
                adListener.onAdPrepared(AdConfig.GDT_AD_NAME);
            }

            @Override
            public void onADClicked() {
                Log.i("InterstitialAdTecentGDT", "onADClicked");
                adListener.onAdClick(AdConfig.GDT_AD_NAME);
            }

            @Override
            public void onADLeftApplication() {
                Log.i("InterstitialAdTecentGDT", "onADLeftApplication");
            }

            @Override
            public void onADClosed() {
                Log.i("InterstitialAdTecentGDT", "onADClosed");
            }
        });

        iad.loadAD();
    }

    private static void showAdInterstitialBaiduMob(final Activity activity, final String interstitialConfigStr, final boolean isLandscape, final RelativeLayout adIntersContainer, final AdListenerInterstitial adListener){

        adListener.onStartRequest(AdConfig.BAIDU_AD_NAME);
        adIntersContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adIntersContainer.setVisibility(View.GONE);
            }
        });
        adIntersContainer.setVisibility(View.GONE);

        final InterstitialAd interAd = new InterstitialAd(activity, AdSize.InterstitialForVideoPausePlay, AdConfig.BAIDU_AD_INTER);

        final DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        int n = (int) ((dm.widthPixels > dm.heightPixels ? dm.heightPixels : dm.widthPixels) * 0.8);
        interAd.loadAdForVideoApp(n, (int) (n * 0.8));
        interAd.setListener(new InterstitialAdListener() {
            @Override
            public void onAdReady() {
                adIntersContainer.setVisibility(View.VISIBLE);

                interAd.showAdInParentForVideoApp(activity, adIntersContainer);
                Log.e("InterstitialAdBaiduMob", "onAdReady");
            }

            @Override
            public void onAdPresent() {
                Log.e("InterstitialAdBaiduMob", "onAdPresent");
                adListener.onAdPrepared(AdConfig.BAIDU_AD_NAME);
            }

            @Override
            public void onAdClick(InterstitialAd interstitialAd) {
                Log.e("InterstitialAdBaiduMob", "onAdPresent:i" + interstitialAd.toString());
                adListener.onAdClick(AdConfig.BAIDU_AD_NAME);
            }

            @Override
            public void onAdDismissed() {
                adIntersContainer.setVisibility(View.GONE);
                Log.e("InterstitialAdBaiduMob", "onAdPresent");
                adListener.onAdDismissed();
                interAd.loadAd();
            }

            @Override
            public void onAdFailed(String s) {
                Log.e("InterstitialAdBaiduMob", "onAdFailed:s:" + s);
                String newConfigStr = interstitialConfigStr.replace("baidu", AdConfig.MASK_NAME);
                showAdInterstitial(activity, newConfigStr, isLandscape, adIntersContainer, adListener);
            }
        });
    }

    public interface AdListenerInterstitial {

        void onStartRequest(String channel);

        void onAdClick(String channel);

        void onAdFailed(String failedMsg);

        void onAdDismissed();

        void onAdPrepared(String channel);
    }
}
