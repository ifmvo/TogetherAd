package com.rumtel.ad.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.ifmvo.imageloader.ILFactory;
import com.ifmvo.imageloader.LoadListener;
import com.ifmvo.imageloader.progress.LoaderOptions;

import java.util.List;

/*
 * (●ﾟωﾟ●)
 *
 * Created by Matthew_Chen on 2018/12/24.
 */
public class AdViewPreMovieIXunFei extends AdViewPreMovieBase {

    public AdViewPreMovieIXunFei(@NonNull Context context) {
        super(context);
    }

    public AdViewPreMovieIXunFei(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AdViewPreMovieIXunFei(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    NativeADDataRef mAd;
    IFLYNativeAd nativeAd;

    @Override
    public void start(String locationId) {
        nativeAd = new IFLYNativeAd(super.getContext(), locationId, new IFLYNativeListener() {
            @Override
            public void onADLoaded(List<NativeADDataRef> list) {
                if (list == null || list.size() <= 0) {
                    if (adViewListener != null) {
                        adViewListener.onAdFailed("IFLYNativeAd:onADLoaded");
                    }
                    return;
                }

                mAd = list.get(0);
                initAd();

            }

            @Override
            public void onAdFailed(com.iflytek.voiceads.AdError adError) {
                if (adViewListener != null) {
                    adViewListener.onAdFailed(adError.getErrorDescription());
                }
            }

            @Override
            public void onConfirm() {

            }

            @Override
            public void onCancel() {

            }
        });
        int count = 1; // 一次拉取的广告条数:范围 1-30(目前仅支持每次请求一条)
        nativeAd.loadAd(count);
    }

    private void initAd() {
        mTvDesc.setText(mAd.getTitle());
        ILFactory.getLoader().load(AdViewPreMovieIXunFei.super.getContext(), mIvImg, mAd.getImage(), new LoaderOptions(), new LoadListener() {
            @Override
            public boolean onLoadCompleted(Drawable drawable) {
                if (mAd.onExposured(mIvImg)) {
                    if (adViewListener != null) {
                        adViewListener.onExposured();
                    }
                }
                mRootView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 点击响应
                        mAd.onClicked(view);
                        if (adViewListener != null) {
                            adViewListener.onAdClick();
                        }
                    }
                });
                startTimerCount(6000);
                return false;
            }
        });
        mRootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        nativeAd.setParameter(AdKeys.CLICK_POS_DX, event.getX() + "");
                        nativeAd.setParameter(AdKeys.CLICK_POS_DY, event.getY() + "");
                        break;
                    case MotionEvent.ACTION_UP:
                        nativeAd.setParameter(AdKeys.CLICK_POS_UX, event.getX() + "");
                        nativeAd.setParameter(AdKeys.CLICK_POS_UY, event.getY() + "");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }
}
