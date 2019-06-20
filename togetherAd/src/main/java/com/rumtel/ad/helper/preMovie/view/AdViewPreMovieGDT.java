package com.rumtel.ad.helper.preMovie.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;
import com.ifmvo.imageloader.ILFactory;
import com.ifmvo.imageloader.LoadListener;
import com.ifmvo.imageloader.progress.LoaderOptions;
import com.qq.e.ads.nativ.NativeMediaAD;
import com.qq.e.ads.nativ.NativeMediaADData;
import com.qq.e.comm.util.AdError;
import com.rumtel.ad.R;
import com.rumtel.ad.TogetherAd;
import com.rumtel.ad.other.AdExtKt;
import com.rumtel.ad.other.AdNameType;

import java.util.List;


/*
 * (●ﾟωﾟ●)
 *
 * Created by Matthew_Chen on 2018/8/14.
 */
public class AdViewPreMovieGDT extends AdViewPreMovieBase {

    public AdViewPreMovieGDT(@NonNull Context context) {
        super(context);
    }

    public AdViewPreMovieGDT(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AdViewPreMovieGDT(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void start(String locationId) {

        NativeMediaAD.NativeMediaADListener nativeMediaADListener = new NativeMediaAD.NativeMediaADListener() {
            @Override
            public void onADLoaded(List<NativeMediaADData> list) {
                if (stop) {
                    return;
                }
                if (list == null || list.size() == 0) {
                    if (adViewListener != null) {
                        AdExtKt.logd(AdViewPreMovieGDT.this, "请求成功但是数量为空");
                        adViewListener.onAdFailed("请求成功但是数量为空");
                    }
                    return;
                }

                AdExtKt.logd(AdViewPreMovieGDT.this, "list.size():" + list.size());

                final NativeMediaADData mAD = list.get(0);
                mTvDesc.setText(mAD.getTitle());
                mLlAdContainer.setVisibility(View.VISIBLE);
                mFlAdContainer.setVisibility(View.GONE);
                mIvImg0.setVisibility(View.VISIBLE);
                mIvImg1.setVisibility(View.GONE);
                mIvImg2.setVisibility(View.GONE);
                try {
                    String url = mAD.getImgUrl();
                    ILFactory.getLoader().load(AdViewPreMovieGDT.super.getContext(), mIvImg0, url, new LoaderOptions(), new LoadListener() {
                        @Override
                        public boolean onLoadCompleted(Drawable drawable) {
                            mTvLogoGdt.setVisibility(View.VISIBLE);
                            mAD.onExposured(mRootView);
                            mRootView.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (adViewListener != null) {
                                        adViewListener.onAdClick();
                                    }
                                    mAD.onClicked(v);
                                }
                            });

                            startTimerCount(6000);
                            return false;
                        }
                    });
                } catch (Exception e) {
                    AdExtKt.loge(AdViewPreMovieGDT.this, e.getMessage());
                }
            }

            @Override
            public void onADStatusChanged(NativeMediaADData nativeMediaADData) {

            }

            @Override
            public void onADError(NativeMediaADData nativeMediaADData, AdError adError) {
                if (adViewListener != null) {
                    adViewListener.onAdFailed(adError.getErrorMsg());
                }
            }

            @Override
            public void onADVideoLoaded(NativeMediaADData nativeMediaADData) {

            }

            @Override
            public void onADExposure(NativeMediaADData nativeMediaADData) {
                AdExtKt.logd(AdViewPreMovieGDT.this, AdNameType.GDT.getType() + ":前贴：" + AdViewPreMovieGDT.super.getContext().getString(R.string.exposure));
            }

            @Override
            public void onADClicked(NativeMediaADData nativeMediaADData) {
                AdExtKt.logd(AdViewPreMovieGDT.this, AdNameType.GDT.getType() + ":前贴：" + AdViewPreMovieGDT.super.getContext().getString(R.string.clicked));
            }

            @Override
            public void onNoAD(AdError adError) {
                if (adViewListener != null) {
                    adViewListener.onAdFailed("没有广告了：" + adError.getErrorMsg());
                }
            }
        };

        NativeMediaAD mADManager = new NativeMediaAD(super.getContext(), TogetherAd.INSTANCE.getAppIdGDT(), locationId, nativeMediaADListener);

        try {
            mADManager.loadAD(1);
        } catch (Exception e) {
            Toast.makeText(super.getContext(), "加载失败", Toast.LENGTH_SHORT).show();
        }
    }
}
