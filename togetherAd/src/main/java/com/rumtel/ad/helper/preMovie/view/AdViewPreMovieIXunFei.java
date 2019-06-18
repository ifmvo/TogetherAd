//package com.rumtel.ad.helper.preMovie.view;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.util.AttributeSet;
//import android.view.View;
//import com.iflytek.voiceads.IFLYNativeAd;
//import com.iflytek.voiceads.config.AdError;
//import com.iflytek.voiceads.conn.NativeDataRef;
//import com.iflytek.voiceads.listener.IFLYNativeListener;
//import com.ifmvo.imageloader.ILFactory;
//import com.ifmvo.imageloader.LoadListener;
//import com.ifmvo.imageloader.progress.LoaderOptions;
//
//
///*
// * (●ﾟωﾟ●)
// *
// * Created by Matthew_Chen on 2018/12/24.
// */
//public class AdViewPreMovieIXunFei extends AdViewPreMovieBase {
//
//    public AdViewPreMovieIXunFei(@NonNull Context context) {
//        super(context);
//    }
//
//    public AdViewPreMovieIXunFei(@NonNull Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public AdViewPreMovieIXunFei(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    NativeDataRef mAd;
//    IFLYNativeAd nativeAd;
//
//    @Override
//    public void start(String locationId) {
//        nativeAd = new IFLYNativeAd(super.getContext(), locationId, new IFLYNativeListener() {
//
//            @Override
//            public void onAdLoaded(NativeDataRef nativeDataRef) {
//                if (nativeDataRef == null) {
//                    if (adViewListener != null) {
//                        adViewListener.onAdFailed("IFLYNativeAd:onADLoaded");
//                    }
//                    return;
//                }
//
//                mAd = nativeDataRef;
//                initAd();
//            }
//
//            @Override
//            public void onAdFailed(AdError adError) {
//                if (adViewListener != null) {
//                    adViewListener.onAdFailed(adError.getErrorDescription());
//                }
//            }
//
//            @Override
//            public void onConfirm() {
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//        });
//        nativeAd.loadAd();
//    }
//
//    private void initAd() {
//        mTvDesc.setText(mAd.getTitle());
//        ILFactory.getLoader().load(AdViewPreMovieIXunFei.super.getContext(), mIvImg, mAd.getImgUrl(), new LoaderOptions(), new LoadListener() {
//            @Override
//            public boolean onLoadCompleted(Drawable drawable) {
//                if (mAd.onExposure(mIvImg)) {
//                    if (adViewListener != null) {
//                        adViewListener.onExposured();
//                    }
//                }
//                mRootView.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        // 点击响应
//                        mAd.onClick(view);
//                        if (adViewListener != null) {
//                            adViewListener.onAdClick();
//                        }
//                    }
//                });
//                startTimerCount(6000);
//                return false;
//            }
//        });
//    }
//}
