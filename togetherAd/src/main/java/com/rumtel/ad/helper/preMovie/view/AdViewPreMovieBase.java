package com.rumtel.ad.helper.preMovie.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.rumtel.ad.R;
import com.rumtel.ad.TogetherAd;

/*
 * (●ﾟωﾟ●) 前贴广告自定义 View 的 Base
 *
 * Created by Matthew_Chen on 2018/8/15.
 */
public abstract class AdViewPreMovieBase extends FrameLayout {

    protected Boolean needTimer = true;

    ViewGroup mRootView;

    FrameLayout mFlAdContainer;
    LinearLayout mLlAdContainer;

    ImageView mIvImg0;
    ImageView mIvImg1;
    ImageView mIvImg2;

    TextView mTextCountDown;

    View mFlDesc;
    TextView mTvDesc;

    TextView mTvLogoCommon;
    ImageView mTvLogoGdt;
    ImageView mTvLogoCsj;

    TimerCount mTimerCount;

    boolean stop = false;

    public AdViewPreMovieListener adViewListener;

    public AdViewPreMovieBase(@NonNull Context context) {
        super(context);
        initView();
    }

    public AdViewPreMovieBase(Context context, Boolean needTimer) {
        super(context);
        this.needTimer = needTimer;
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
        mRootView = (ViewGroup) View.inflate(super.getContext(), R.layout.view_ad_pre_movie, this);

        mFlAdContainer = mRootView.findViewById(R.id.fl_ad_container);
        mLlAdContainer = mRootView.findViewById(R.id.ll_ad_container);

        mIvImg0 = mRootView.findViewById(R.id.iv_img0);
        mIvImg1 = mRootView.findViewById(R.id.iv_img1);
        mIvImg2 = mRootView.findViewById(R.id.iv_img2);

        mTextCountDown = mRootView.findViewById(R.id.text_count_down);

        mTvDesc = mRootView.findViewById(R.id.tv_desc);
        mFlDesc = mRootView.findViewById(R.id.fl_desc);

        //广告标示
        mTvLogoCommon = mRootView.findViewById(R.id.mTvLogoCommon);
        mTvLogoGdt = mRootView.findViewById(R.id.mTvLogoGdt);
        mTvLogoCsj = mRootView.findViewById(R.id.mTvLogoCsj);

        setPadding(mTextCountDown, TogetherAd.INSTANCE.getPreMoivePaddingSize());
    }

    private void setPadding(View view, int height) {
        if (Build.VERSION.SDK_INT > 16) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                ((ViewGroup.MarginLayoutParams) lp).topMargin += height;//增高
            }
            view.setLayoutParams(lp);
        }
    }

    /**
     * 开始计数
     */
    protected void startTimerCount(long millisInFuture) {
        cancelTimerCount();
        mTimerCount = new TimerCount(millisInFuture, 1000);
        mTimerCount.start();
    }

    /**
     * 取消计数
     */
    private void cancelTimerCount() {
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
                if (mTextCountDown.getVisibility() == View.GONE) {
                    mTextCountDown.setVisibility(View.VISIBLE);
                }
                mTextCountDown.setText(String.format("%d s", millisUntilFinished / 1000));
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
