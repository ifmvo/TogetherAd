package com.rumtel.ad;

import android.app.Activity;
import android.util.Log;
import com.baidu.mobad.feeds.BaiduNative;
import com.baidu.mobad.feeds.NativeErrorCode;
import com.baidu.mobad.feeds.NativeResponse;
import com.baidu.mobad.feeds.RequestParameters;
import com.qq.e.ads.nativ.NativeMediaAD;
import com.qq.e.ads.nativ.NativeMediaADData;
import com.qq.e.comm.util.AdError;

import java.util.List;

/*
 * (●ﾟωﾟ●) 信息流广告
 *
 * Created by Matthew_Chen on 2018/8/22.
 */
public class AdHelperList {
    public static final String TAG = "AdHelperList";

    public static void getAdList(Activity activity, String listConfigStr, final AdListenerList adListener) {
        AdNameType randomAdName = AdRandomUtil.INSTANCE.getRandomAdName(listConfigStr);
        switch (randomAdName) {
            case BAIDU:
                getAdListBaiduMob(activity, listConfigStr, adListener);
                break;
            case GDT:
                getAdListTecentGDT(activity, listConfigStr, adListener);
                break;
            default:
                adListener.onAdFailed("AdNameType.NO");
                break;
        }
    }

    private static void getAdListBaiduMob(final Activity activity, final String listConfigStr, final AdListenerList adListener) {
        adListener.onStartRequest(AdConfig.BAIDU_AD_NAME);
        BaiduNative baidu = new BaiduNative(activity, AdConfig.BAIDU_AD_HOT, new BaiduNative.BaiduNativeNetworkListener(){

            @Override
            public void onNativeLoad(List<NativeResponse> list) {
                adListener.onAdLoaded(AdConfig.BAIDU_AD_NAME, list);
            }

            @Override
            public void onNativeFail(NativeErrorCode nativeErrorCode) {
                String newListConfig = listConfigStr.replace("baidu", AdConfig.MASK_NAME);
                getAdList(activity, newListConfig, adListener);
            }
        });
//        BaiduNative baidu = new BaiduNative(activity, AdConfig.BAIDU_AD_HOT, new BaiduNative.BaiduNativeNetworkListener() {
//
//            @Override
//            public void onNativeFail(NativeErrorCode arg0) {
//                Log.w(TAG, "onNativeFail reason:" + arg0.name());
////                adListener.onAdFailed("onNativeFail reason:" + arg0.name());
//
//                String newListConfig = listConfigStr.replace("baidu", AdConfig.MASK_NAME);
//                getAdList(activity, newListConfig, adListener);
//            }
//
//            @Override
//            public void onNativeLoad(List<NativeResponse> arg0) {
//                // 一个广告只允许展现一次，多次展现、点击只会计入一次
//                adListener.onAdLoaded(AdConfig.BAIDU_AD_NAME, arg0);
//            }
//        }, AdConfig.getBaiduConfig());

        /*
         * Step 2. 创建requestParameters对象，并将其传给baidu.makeRequest来请求广告
         */
        // 用户点击下载类广告时，是否弹出提示框让用户选择下载与否
        RequestParameters requestParameters =
                new RequestParameters.Builder()
//                        .downloadAppConfirmPolicy(RequestParameters.DOWNLOAD_APP_CONFIRM_NEVER)
                        .build();

        baidu.makeRequest(requestParameters);
    }

    private static void getAdListTecentGDT(final Activity activity, final String listConfigStr, final AdListenerList adListener) {
        adListener.onStartRequest(AdConfig.GDT_AD_NAME);

        NativeMediaAD.NativeMediaADListener adListenerNative = new NativeMediaAD.NativeMediaADListener() {
            @Override
            public void onADLoaded(List<NativeMediaADData> adList) {
                adListener.onAdLoaded(AdConfig.GDT_AD_NAME, adList);
            }

            @Override
            public void onNoAD(AdError adError) {
                String newListConfig = listConfigStr.replace("gdt", AdConfig.MASK_NAME);
                getAdList(activity, newListConfig, adListener);
                Log.e(TAG, String.format("广告加载失败，错误码：%d，错误信息：%s", adError.getErrorCode(), adError.getErrorMsg()));
            }

            @Override
            public void onADStatusChanged(NativeMediaADData ad) {
            }

            @Override
            public void onADError(NativeMediaADData adData, AdError adError) {
                String newListConfig = listConfigStr.replace("gdt", AdConfig.MASK_NAME);
                getAdList(activity, newListConfig, adListener);
//                adListener.onAdFailed("广告返回错误");

                Log.i(TAG, adData.getTitle() + " onADError, error code: " + adError.getErrorCode()
                        + ", error msg: " + adError.getErrorMsg());
            }

            @Override
            public void onADVideoLoaded(NativeMediaADData adData) {
                Log.i(TAG, adData.getTitle() + " ---> 视频素材加载完成"); // 仅仅是加载视频文件完成，如果没有绑定MediaView视频仍然不可以播放
            }

            @Override
            public void onADExposure(NativeMediaADData adData) {
                Log.i(TAG, adData.getTitle() + " onADExposure");

            }

            @Override
            public void onADClicked(NativeMediaADData adData) {
//                adListener.onClicked(AdConfig.GDT_AD_NAME);
            }
        };

        NativeMediaAD mADManager = new NativeMediaAD(activity, AdConfig.GDT_AD_APP_ID, AdConfig.GDT_AD_HOT, adListenerNative);

        try {
            mADManager.loadAD(4);
        } catch (Exception e) {
            adListener.onAdFailed("加载失败");
        }
    }


    public interface AdListenerList {

        void onAdFailed(String failedMsg);

        void onAdLoaded(String channel, List<?> adList);

        void onStartRequest(String channel);
    }
}
