package com.rumtel.ad.helper.preMovie.view;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import com.bytedance.sdk.openadsdk.*;
import com.ifmvo.imageloader.ILFactory;
import com.rumtel.ad.other.AdExtKt;
import com.rumtel.ad.other.AdNameType;

import java.util.ArrayList;
import java.util.List;


/*
 * (●ﾟωﾟ●) 穿山甲
 *
 * Created by Matthew_Chen on 2018/8/14.
 */
public class AdViewPreMovieCsj extends AdViewPreMovieBase {

    public AdViewPreMovieCsj(@NonNull Context context, boolean needTimer) {
        super(context, needTimer);
    }

    public AdViewPreMovieCsj(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AdViewPreMovieCsj(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void start(String locationId) {

        try {
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Point point = new Point();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                wm.getDefaultDisplay().getRealSize(point);
            } else {
                wm.getDefaultDisplay().getSize(point);
            }

            AdSlot adSlot = new AdSlot.Builder()
                    .setCodeId(locationId)
                    .setSupportDeepLink(true)
                    .setImageAcceptedSize(point.x, point.y)
                    .setAdCount(1)
                    .build();

            TTAdSdk.getAdManager().createAdNative(getContext()).loadFeedAd(adSlot, new TTAdNative.FeedAdListener() {
                @Override
                public void onError(int i, String s) {
                    AdExtKt.logd(AdViewPreMovieCsj.this, "errorCode: " + i + "errorMsg: " + s);
                    if (adViewListener != null) {
                        adViewListener.onAdFailed(s);
                    }
                }

                @Override
                public void onFeedAdLoad(List<TTFeedAd> list) {
                    if (list == null || list.size() == 0) {
                        if (adViewListener != null) {
                            AdExtKt.logd(AdViewPreMovieCsj.this, "请求成功但是数量为空");
                            adViewListener.onAdFailed("请求成功但是数量为空");
                        }
                        return;
                    }

                    TTFeedAd adObject = list.get(0);

                    // 可以被点击的view, 也可以把convertView放进来意味整个item可被点击，点击会跳转到落地页
                    List<View> clickViewList = new ArrayList<>();
                    clickViewList.add(mRootView);
                    // 创意点击区域的view 点击根据不同的创意进行下载或拨打电话动作
                    //如果需要点击图文区域也能进行下载或者拨打电话动作，请将图文区域的view传入creativeViewList
                    List<View> creativeViewList = new ArrayList<>();
                    creativeViewList.add(mRootView);
                    // 注册普通点击区域，创意点击区域。重要! 这个涉及到广告计费及交互，必须正确调用。convertView必须使用ViewGroup。
                    adObject.registerViewForInteraction(mRootView, clickViewList, creativeViewList, new TTNativeAd.AdInteractionListener() {
                        @Override
                        public void onAdClicked(View view, TTNativeAd ttNativeAd) {
                        }

                        @Override
                        public void onAdCreativeClick(View view, TTNativeAd ttNativeAd) {
                            AdExtKt.logd(AdViewPreMovieCsj.this, AdNameType.CSJ.getType() + ":前贴：点击了");
                            if (adViewListener != null) {
                                adViewListener.onAdClick();
                            }
                        }

                        @Override
                        public void onAdShow(TTNativeAd ttNativeAd) {
                            AdExtKt.logd(AdViewPreMovieCsj.this, AdNameType.CSJ.getType() + ":前贴：展示了");
                            if (adViewListener != null) {
                                adViewListener.onExposured();
                            }
                        }
                    });

                    mFlDesc.setVisibility(View.VISIBLE);
                    mTvDesc.setText(adObject.getDescription());
                    List<TTImage> imageList = adObject.getImageList();
                    switch (adObject.getImageMode()) {
                        case TTAdConstant.IMAGE_MODE_VIDEO:
                        case TTAdConstant.IMAGE_MODE_VIDEO_VERTICAL:
//                            if (needTimer) {
                            adObject.setVideoAdListener(new TTFeedAd.VideoAdListener() {

                                @Override
                                public void onProgressUpdate(long current, long duration) {

                                }

                                @Override
                                public void onVideoAdComplete(TTFeedAd ad) {

                                }

                                @Override
                                public void onVideoLoad(TTFeedAd ttFeedAd) {

                                }

                                @Override
                                public void onVideoError(int i, int i1) {

                                }

                                @Override
                                public void onVideoAdStartPlay(TTFeedAd ttFeedAd) {

                                }

                                @Override
                                public void onVideoAdPaused(TTFeedAd ttFeedAd) {

                                }

                                @Override
                                public void onVideoAdContinuePlay(TTFeedAd ttFeedAd) {

                                }
                            });
                            mLlAdContainer.setVisibility(View.GONE);
                            mFlAdContainer.setVisibility(View.VISIBLE);
                            View adView = adObject.getAdView();
                            if (adView != null && adView.getParent() == null) {
                                mFlAdContainer.removeAllViews();
                                mFlAdContainer.addView(adView);
                            }
//                            } else {
//                                mLlAdContainer.setVisibility(View.VISIBLE);
//                                mFlAdContainer.setVisibility(View.GONE);
//                                mIvImg1.setVisibility(View.GONE);
//                                mIvImg2.setVisibility(View.GONE);
//                                TTImage videoCoverImage = adObject.getVideoCoverImage();
//                                if (videoCoverImage != null && videoCoverImage.getImageUrl() != null) {
//                                    ILFactory.getLoader().load(AdViewPreMovieCsj.super.getContext(), mIvImg0, videoCoverImage.getImageUrl());
//                                }
//                            }
                            break;

                        case TTAdConstant.IMAGE_MODE_LARGE_IMG:
                        case TTAdConstant.IMAGE_MODE_SMALL_IMG:
                        case TTAdConstant.IMAGE_MODE_VERTICAL_IMG:
                            mLlAdContainer.setVisibility(View.VISIBLE);
                            mFlAdContainer.setVisibility(View.GONE);
                            mIvImg1.setVisibility(View.GONE);
                            mIvImg2.setVisibility(View.GONE);
                            if (imageList != null && imageList.size() > 0 && imageList.get(0) != null && imageList.get(0).isValid()) {
                                ILFactory.getLoader().load(AdViewPreMovieCsj.super.getContext(), mIvImg0, imageList.get(0).getImageUrl());
                            }
                            break;

                        case TTAdConstant.IMAGE_MODE_GROUP_IMG:
                            mLlAdContainer.setVisibility(View.VISIBLE);
                            mFlAdContainer.setVisibility(View.GONE);
                            mIvImg1.setVisibility(View.VISIBLE);
                            mIvImg2.setVisibility(View.VISIBLE);

                            if (imageList != null && imageList.size() > 0 && imageList.get(0) != null && imageList.get(0).isValid()) {
                                mIvImg0.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                ILFactory.getLoader().load(AdViewPreMovieCsj.super.getContext(), mIvImg0, imageList.get(0).getImageUrl());
                            }
                            if (imageList != null && imageList.size() > 1 && imageList.get(1) != null && imageList.get(1).isValid()) {
                                mIvImg0.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                ILFactory.getLoader().load(AdViewPreMovieCsj.super.getContext(), mIvImg1, imageList.get(1).getImageUrl());
                            }
                            if (imageList != null && imageList.size() > 2 && imageList.get(2) != null && imageList.get(2).isValid()) {
                                mIvImg0.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                ILFactory.getLoader().load(AdViewPreMovieCsj.super.getContext(), mIvImg2, imageList.get(2).getImageUrl());
                            }
                            break;
                    }
                    mTvLogoCsj.setVisibility(View.VISIBLE);
                    mTvLogoCsj.setImageBitmap(adObject.getAdLogo());

                    //开始计时
                    if (adViewListener != null) {
                        adViewListener.onAdPrepared();
                    }
                    if (needTimer) {
                        startTimerCount(6000);
                    }
                }
            });
        } catch (Exception e) {
            AdExtKt.logd(AdViewPreMovieCsj.this, "崩溃异常");
            if (adViewListener != null) {
                adViewListener.onAdFailed("崩溃异常");
            }
        }
    }
}
