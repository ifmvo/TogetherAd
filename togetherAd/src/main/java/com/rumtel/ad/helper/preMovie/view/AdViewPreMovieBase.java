package com.rumtel.ad.helper.preMovie.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.rumtel.ad.R;

/*
 * (●ﾟωﾟ●) 前贴广告自定义 View 的 Base
 *
 * Created by Matthew_Chen on 2018/8/15.
 */
public abstract class AdViewPreMovieBase extends FrameLayout {

    View mRootView;
    ImageView mIvImg;
    TextView mTextCountDown;
    TextView mTvDesc;
    TextView mTvLogoCommon;
    ImageView mIvAdLogo;

    TimerCount mTimerCount;

    boolean stop = false;

    public AdViewPreMovieListener adViewListener;

    public AdViewPreMovieBase(@NonNull Context context) {
        super(context);
        initView();
    }

    public AdViewPreMovieBase(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AdViewPreMovieBase(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {
        mRootView = View.inflate(super.getContext(), R.layout.view_ad_pre_movie, this);
        mIvImg = mRootView.findViewById(R.id.iv_img);
        mTextCountDown = mRootView.findViewById(R.id.text_count_down);
        mTvDesc = mRootView.findViewById(R.id.tv_desc);
        mTvLogoCommon = mRootView.findViewById(R.id.mTvLogoCommon);
        mIvAdLogo = mRootView.findViewById(R.id.mIvAdLogo);
    }

    /**
     * 开始计数
     */
    protected void startTimerCount(long millisInFuture) {
        cancelTimerCount();
        mTimerCount = new TimerCount(millisInFuture, 1000);
        mTimerCount.start();
        if (adViewListener != null) {
            adViewListener.onAdPrepared();
        }
    }

    /**
     * 取消计数
     */
    protected void cancelTimerCount() {
        if (mTimerCount != null) {
            mTimerCount.cancel();
        }
    }

    public AdViewPreMovieBase setAdViewPreMovieListener(AdViewPreMovieListener adViewListener) {
        this.adViewListener = adViewListener;
        return this;
    }

    public abstract void start(String locationId);

    /**
     * 监听回调
     */
    public interface AdViewPreMovieListener {

        void onAdClick();

        void onAdFailed(String failedMsg);

        void onAdDismissed();

        void onAdPrepared();

        void onExposured();
    }

    /**
     * 倒计时
     */
    @SuppressLint("DefaultLocale")
    class TimerCount extends CountDownTimer {

        TimerCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (mTextCountDown != null) {
                mTextCountDown.setText(String.format("%d S", millisUntilFinished / 1000));
            }
        }

        @Override
        public void onFinish() {
            if (mTimerCount != null) {
                mTimerCount.cancel();
            }
            if (adViewListener != null) {
                adViewListener.onAdDismissed();
            }
        }
    }

    public void cancel() {
        cancelTimerCount();
    }

    public void stop() {
        stop = true;
    }

}
