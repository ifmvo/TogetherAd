//package com.rumtel.ad.view;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import com.ifmvo.imageloader.ILFactory;
//import com.ifmvo.imageloader.LoadListener;
//import com.ifmvo.imageloader.progress.LoaderOptions;
//import com.qq.e.ads.nativ.MediaListener;
//import com.qq.e.ads.nativ.MediaView;
//import com.qq.e.ads.nativ.NativeMediaAD;
//import com.qq.e.ads.nativ.NativeMediaADData;
//import com.qq.e.comm.constants.AdPatternType;
//import com.qq.e.comm.util.AdError;
//import com.rumtel.ad.AdConfig;
//
//import java.util.List;
//
///*
// * (●ﾟωﾟ●)
// *
// * Created by Matthew_Chen on 2018/8/14.
// */
//public class AdViewPreMovieGDT extends AdViewPreMovieBase
//{
//
//    public static final String TAG = "AdViewPreMovie";
//
//    MediaView gdt_media_view;
//
//    // 与广告有关的变量，用来显示广告素材的UI
//    private static NativeMediaADData mAD;                        // 加载的原生视频广告对象，本示例为简便只演示加载1条广告的示例
//    private static NativeMediaAD     mADManager;                     // 原生广告manager，用于管理广告数据的加载，监听广告回调
//
//    public AdViewPreMovieGDT(@NonNull Context context)
//    {
//        super(context);
//    }
//
//    public AdViewPreMovieGDT(@NonNull Context context, @Nullable AttributeSet attrs)
//    {
//        super(context, attrs);
//    }
//
//    public AdViewPreMovieGDT(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
//    {
//        super(context, attrs, defStyleAttr);
//    }
//
//    @Override
//    public void initView(){
//        super.initView();
//
//        NativeMediaAD.NativeMediaADListener adListener = new NativeMediaAD.NativeMediaADListener(){
//            @Override
//            public void onADLoaded(List<NativeMediaADData> adList) {
//                if (adList != null && adList.size() > 0) {
//                    mAD = adList.get(0);
//                }
//
//                int patternType = mAD.getAdPatternType();
//                if (patternType == AdPatternType.NATIVE_2IMAGE_2TEXT || patternType == AdPatternType.NATIVE_VIDEO) {
//                    mTvDesc.setText(mAD.getDesc());
//                    ILFactory.getLoader().load(AdViewPreMovieGDT.super.getContext(), mIvImg, mAD.getImgUrl(), new LoaderOptions(), new LoadListener() {
//                        @Override
//                        public boolean onLoadCompleted(Drawable drawable) {
//                            mAD.onExposured(mRootView);
//                            mRootView.setOnClickListener(new OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    mAD.onClicked(v);
//                                }
//                            });
//
//                            if (mAD.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
//                                mAD.preLoadVideo();
//                            }
//                            else {
//                                startTimerCount(6000);
//                            }
//                            return false;
//                        }
//                    });
//                }
//
//            }
//
//            @Override
//            public void onNoAD(AdError adError)
//            {
//                Log.e(TAG, String.format("广告加载失败，错误码：%d，错误信息：%s", adError.getErrorCode(),
//                        adError.getErrorMsg()));
//            }
//
//            @Override
//            public void onADStatusChanged(NativeMediaADData ad)
//            {
//            }
//
//            @Override
//            public void onADError(NativeMediaADData adData, AdError adError)
//            {
//                Log.i(TAG, adData.getTitle() + " onADError, error code: " + adError.getErrorCode()
//                           + ", error msg: " + adError.getErrorMsg());
//            }
//
//            @Override
//            public void onADVideoLoaded(NativeMediaADData adData)
//            {
//                Log.i(TAG, adData.getTitle() + " ---> 视频素材加载完成"); // 仅仅是加载视频文件完成，如果没有绑定MediaView视频仍然不可以播放
//                bindMediaView();
//            }
//
//            @Override
//            public void onADExposure(NativeMediaADData adData)
//            {
//                Log.i(TAG, adData.getTitle() + " onADExposure");
//
//            }
//
//            @Override
//            public void onADClicked(NativeMediaADData adData)
//            {
//                Log.i(TAG, adData.getTitle() + " onADClicked");
//            }
//        };
//
//        mADManager = new NativeMediaAD(super.getContext(), AdConfig.GDT_AD_APP_ID, AdConfig.GDT_AD_PLAYER, adListener);
//
//        try {
//            mADManager.loadAD(1);
//        } catch (Exception e) {
//            Toast.makeText(super.getContext(), "加载失败", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    private void bindMediaView() {
//        if (mAD.isVideoAD() && gdt_media_view != null && mAD != null) {
//            mIvImg.setVisibility(View.GONE);
//            gdt_media_view.setVisibility(View.VISIBLE);
//
//            /*
//             *      bindView(MediaView view, boolean useDefaultController):
//             *    - useDefaultController: false，不会调用广点通的默认视频控制条
//             *    - useDefaultController: true，使用SDK内置的播放器控制条，此时开发者需要把 demo 下的res文件夹里面的图片拷贝到自己项目的res文件夹去
//             *
//             * 在这里绑定 MediaView后，SDK会根据视频素材的宽高比例，重新给 MediaView 设置新的宽高
//             */
//            mAD.bindView(gdt_media_view, true);
//            mAD.play();
//
//            /* 设置视频播放过程中的监听器 */
//            mAD.setMediaListener(new MediaListener() {
//
//                /**
//                 * 视频播放器初始化完成，准备好可以播放了
//                 *
//                 * @param videoDuration 视频素材的总时长
//                 */
//                @Override
//                public void onVideoReady(long videoDuration) {
//                    Log.i(TAG, "onVideoReady, videoDuration = " + videoDuration);
//                    duration = videoDuration;
//                }
//
//                /** 视频开始播放 */
//                @Override
//                public void onVideoStart() {
//                    Log.i(TAG, "onVideoStart");
//                    startTimerCount(duration);
////                    tikTokHandler.post(countDown);
//                    mTextCountDown.setVisibility(View.VISIBLE);
//                }
//
//                /** 视频暂停 */
//                @Override
//                public void onVideoPause() {
//                    Log.i(TAG, "onVideoPause");
//                    mTextCountDown.setVisibility(View.GONE);
//                }
//
//                /** 视频自动播放结束，到达最后一帧 */
//                @Override
//                public void onVideoComplete() {
//                    Log.i(TAG, "onVideoComplete");
//                    releaseCountDown();
//                    mTextCountDown.setVisibility(View.GONE);
//                }
//
//                /** 视频播放时出现错误 */
//                @Override
//                public void onVideoError(AdError adError) {
//                    Log.i(TAG, String.format("onVideoError, errorCode: %d, errorMsg: %s",
//                            adError.getErrorCode(), adError.getErrorMsg()));
//                }
//
//                /** SDK内置的播放器控制条中的重播按钮被点击 */
//                @Override
//                public void onReplayButtonClicked() {
//                    Log.i(TAG, "onReplayButtonClicked");
//                }
//
//                /**
//                 * SDK内置的播放器控制条中的下载/查看详情按钮被点击
//                 * 注意: 这里是指UI中的按钮被点击了，而广告的点击事件回调是在onADClicked中，开发者如需统计点击只需要在onADClicked回调中进行一次统计即可。
//                 */
//                @Override
//                public void onADButtonClicked() {
//                    Log.i(TAG, "onADButtonClicked");
//                }
//
//                /** SDK内置的全屏和非全屏切换回调，进入全屏时inFullScreen为true，退出全屏时inFullScreen为false */
//                @Override
//                public void onFullScreenChanged(boolean inFullScreen) {
//                    Log.i(TAG, "onFullScreenChanged, inFullScreen = " + inFullScreen);
//
//                    // 原生视频广告默认静音播放，进入到全屏后建议开发者可以设置为有声播放
//                    if (inFullScreen) {
//                        mAD.setVolumeOn(true);
//                    } else {
//                        mAD.setVolumeOn(false);
//                    }
//                }
//            });
//        }
//    }
//
//    private long duration;
//    private void releaseCountDown() {
//        cancelTimerCount();
//    }
//}
