package com.rumtel.ad.helper.preMovie.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.baidu.mobad.feeds.BaiduNative;
import com.baidu.mobad.feeds.NativeErrorCode;
import com.baidu.mobad.feeds.NativeResponse;
import com.baidu.mobad.feeds.RequestParameters;
import com.ifmvo.imageloader.ILFactory;
import com.ifmvo.imageloader.LoadListener;
import com.ifmvo.imageloader.progress.LoaderOptions;

import java.util.List;

/*
 * (●ﾟωﾟ●)
 *
 * Created by Matthew_Chen on 2018/8/17.
 */
public class AdViewPreMovieBaidu extends AdViewPreMovieBase {

    private NativeResponse mAd;

    public AdViewPreMovieBaidu(@NonNull Context context) {
        super(context);
    }

    public AdViewPreMovieBaidu(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AdViewPreMovieBaidu(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void start(String locationId) {

        BaiduNative baidu = new BaiduNative(super.getContext(), locationId, new BaiduNative.BaiduNativeNetworkListener() {

            @Override
            public void onNativeFail(NativeErrorCode arg0) {
                if (adViewListener != null) {
                    adViewListener.onAdFailed("onNativeFail reason:" + arg0.toString());
                }
            }

            @Override
            public void onNativeLoad(List<NativeResponse> arg0) {
                // 一个广告只允许展现一次，多次展现、点击只会计入一次
                if (arg0 == null || arg0.size() == 0) {
                    if (adViewListener != null) {
                        adViewListener.onAdFailed("没有广告了：百度");
                    }
                    return;
                }

                // demo仅简单地显示一条。可将返回的多条广告保存起来备用。
                mAd = arg0.get(0);
                mTvDesc.setText(mAd.getTitle());
                if (stop) {
                    return;
                }
                mLlAdContainer.setVisibility(View.VISIBLE);
                mFlAdContainer.setVisibility(View.GONE);
                mIvImg0.setVisibility(View.VISIBLE);
                mIvImg1.setVisibility(View.GONE);
                mIvImg2.setVisibility(View.GONE);
                ILFactory.getLoader().load(AdViewPreMovieBaidu.super.getContext(), mIvImg0, mAd.getImageUrl(), new LoaderOptions(), new LoadListener() {
                    @Override
                    public boolean onLoadCompleted(Drawable drawable) {
                        mTvLogoCommon.setVisibility(View.VISIBLE);
                        // 警告：调用该函数来发送展现，勿漏！
                        mAd.recordImpression(mRootView);
                        mRootView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // 点击响应
                                mAd.handleClick(view);
                                if (adViewListener != null) {
                                    adViewListener.onAdClick();
                                }
                            }
                        });
                        startTimerCount(6000);
                        return false;
                    }
                });
            }
        });

        // 用户点击下载类广告时，是否弹出提示框让用户选择下载与否
        RequestParameters requestParameters =
                new RequestParameters.Builder()
                        .build();

        baidu.makeRequest(requestParameters);
    }
}
