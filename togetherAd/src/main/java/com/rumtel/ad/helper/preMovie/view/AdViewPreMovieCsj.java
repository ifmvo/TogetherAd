package com.rumtel.ad.helper.preMovie.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.bytedance.sdk.openadsdk.AdSlot;


/*
 * (●ﾟωﾟ●) 穿山甲
 *
 * Created by Matthew_Chen on 2018/8/14.
 */
public class AdViewPreMovieCsj extends AdViewPreMovieBase {

    // 与广告有关的变量，用来显示广告素材的UI
//    private NativeMediaADData mAD;                        // 加载的原生视频广告对象，本示例为简便只演示加载1条广告的示例
//    private NativeMediaAD mADManager;                     // 原生广告manager，用于管理广告数据的加载，监听广告回调

    public AdViewPreMovieCsj(@NonNull Context context) {
        super(context);
    }

    public AdViewPreMovieCsj(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AdViewPreMovieCsj(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void start(String locationId) {

        mTvLogoCommon.setVisibility(View.GONE);
        mIvAdLogo.setVisibility(View.VISIBLE);

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId("901121737")
                .setSupportDeepLink(true)
                .setImageAcceptedSize(640, 320)
                .setAdCount(3)
                .build();



//        NativeMediaAD.NativeMediaADListener nativeMediaADListener = new NativeMediaAD.NativeMediaADListener() {
//            @Override
//            public void onADLoaded(List<NativeMediaADData> list) {
//                if (list != null && list.size() > 0) {
//                    AdExtKt.logd(AdViewPreMovieCsj.this, "list.size():" + list.size());
//                    mAD = list.get(0);
//                    mTvDesc.setText(mAD.getTitle());
//                    if (!stop) {
//                        try {
//                            ILFactory.getLoader().load(AdViewPreMovieCsj.super.getContext(), mIvImg, mAD.getImgUrl(), new LoaderOptions(), new LoadListener() {
//                                @Override
//                                public boolean onLoadCompleted(Drawable drawable) {
//                                    mAD.onExposured(mRootView);
//                                    mRootView.setOnClickListener(new OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            if (adViewListener != null) {
//                                                adViewListener.onAdClick();
//                                            }
//                                            mAD.onClicked(v);
//                                        }
//                                    });
//
//                                    startTimerCount(6000);
//                                    return false;
//                                }
//                            });
//                        } catch (Exception e) {
//                        }
//                    }
//                } else {
//                    if (adViewListener != null) {
//                        AdExtKt.logd(AdViewPreMovieCsj.this, "请求成功但是数量为空");
//                        adViewListener.onAdFailed("请求成功但是数量为空");
//                    }
//                }
//            }
//
//            @Override
//            public void onADStatusChanged(NativeMediaADData nativeMediaADData) {
//
//            }
//
//            @Override
//            public void onADError(NativeMediaADData nativeMediaADData, AdError adError) {
//                if (adViewListener != null) {
//                    adViewListener.onAdFailed(adError.getErrorMsg());
//                }
//            }
//
//            @Override
//            public void onADVideoLoaded(NativeMediaADData nativeMediaADData) {
//
//            }
//
//            @Override
//            public void onADExposure(NativeMediaADData nativeMediaADData) {
//                AdExtKt.logd(AdViewPreMovieCsj.this, AdNameType.GDT.getType() + ":前贴：" + AdViewPreMovieCsj.super.getContext().getString(R.string.exposure));
//            }
//
//            @Override
//            public void onADClicked(NativeMediaADData nativeMediaADData) {
//                AdExtKt.logd(AdViewPreMovieCsj.this, AdNameType.GDT.getType() + ":前贴：" + AdViewPreMovieCsj.super.getContext().getString(R.string.clicked));
//            }
//
//            @Override
//            public void onNoAD(AdError adError) {
//                if (adViewListener != null) {
//                    adViewListener.onAdFailed("没有广告了：" + adError.getErrorMsg());
//                }
//            }
//        };
//
//        mADManager = new NativeMediaAD(super.getContext(), TogetherAd.INSTANCE.getAppIdGDT(), locationId, nativeMediaADListener);
//
//        try {
//            mADManager.loadAD(2);
//        } catch (Exception e) {
//            Toast.makeText(super.getContext(), "加载失败", Toast.LENGTH_SHORT).show();
//        }
    }
}
