package com.rumtel.ad.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.ifmvo.imageloader.ILFactory;
import com.ifmvo.imageloader.LoadListener;
import com.ifmvo.imageloader.progress.LoaderOptions;
import com.qq.e.ads.nativ.NativeAD;
import com.qq.e.ads.nativ.NativeADDataRef;
import com.qq.e.comm.util.AdError;
import com.rumtel.ad.AdConfig;

import java.util.List;


/*
 * (●ﾟωﾟ●)
 *
 * Created by Matthew_Chen on 2018/8/14.
 */
public class AdViewPreMovieGDT_old extends AdViewPreMovieBase {

    public static final String TAG = "AdViewPreMovie";

//    MediaView gdt_media_view;

    // 与广告有关的变量，用来显示广告素材的UI
    private static NativeADDataRef mAD;                        // 加载的原生视频广告对象，本示例为简便只演示加载1条广告的示例
    private static NativeAD mADManager;                     // 原生广告manager，用于管理广告数据的加载，监听广告回调

    public AdViewPreMovieGDT_old(@NonNull Context context) {
        super(context);
    }

    public AdViewPreMovieGDT_old(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AdViewPreMovieGDT_old(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        super.initView();

        NativeAD.NativeAdListener nativeAdListener = new NativeAD.NativeAdListener() {
            @Override
            public void onADLoaded(List<NativeADDataRef> adList) {
                Log.e("ifmvo", "GDT:onADLoaded");
                if (adList != null && adList.size() > 0) {
                    mAD = adList.get(0);
                }

                mTvDesc.setText(mAD.getTitle());
                if (!stop) {
                    try {
                        ILFactory.getLoader().load(AdViewPreMovieGDT_old.super.getContext(), mIvImg, mAD.getImgUrl(), new LoaderOptions(), new LoadListener() {
                            @Override
                            public boolean onLoadCompleted(Drawable drawable) {
                                mAD.onExposured(mRootView);
                                mRootView.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mAD.onClicked(v);
                                    }
                                });

                                startTimerCount(6000);
                                return false;
                            }
                        });
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onNoAD(AdError adError) {
                Log.e("ifmvo", "GDT:onNoAD:" + adError.getErrorMsg());
                adViewListener.onAdFailed("没有广告了：" + adError.getErrorMsg());
            }

            @Override
            public void onADStatusChanged(NativeADDataRef nativeADDataRef) {
            }

            @Override
            public void onADError(NativeADDataRef nativeADDataRef, AdError adError) {
                Log.e("ifmvo", "GDT:onADError:" + adError.getErrorMsg());
                adViewListener.onAdFailed(adError.getErrorMsg());
            }
        };

        mADManager = new NativeAD(super.getContext(), AdConfig.GDT_AD_APP_ID, AdConfig.GDT_AD_PLAYER, nativeAdListener);

        try {
            mADManager.loadAD(1);
        } catch (Exception e) {
            Toast.makeText(super.getContext(), "加载失败", Toast.LENGTH_SHORT).show();
        }
    }


}
