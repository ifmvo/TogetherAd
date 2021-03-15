package com.ifmvo.togetherad.gdt.other;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ifmvo.togetherad.gdt.R;
import com.qq.e.comm.compliance.DownloadConfirmCallBack;


/*
 * Copy from gdt demo.
 */
public class DownloadApkConfirmDialogWebView extends Dialog implements View.OnClickListener {
    private static final String TAG = "ConfirmDialogWebView";
    private Context context;
    private int orientation;
    private DownloadConfirmCallBack callBack;
    private WebView webView;
    private ImageView close;
    private Button confirm;

    private ViewGroup contentHolder;
    private ProgressBar loadingBar;
    private Button reloadButton;

    private String url;
    private boolean urlLoadError = false;

    private static final String RELOAD_TEXT = "重新加载";
    private static final String LOAD_ERROR_TEXT = "抱歉，应用信息获取失败";

    public DownloadApkConfirmDialogWebView(Context context, String infoUrl, DownloadConfirmCallBack callBack) {
        super(context, R.style.DownloadConfirmDialogFullScreen);//需要全屏显示，同时显示非窗口蒙版
        this.context = context;
        this.callBack = callBack;
        this.url = infoUrl;
        orientation = context.getResources().getConfiguration().orientation;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(true);
        initView();
    }

    private void initView() {
        setContentView(R.layout.download_confirm_dialog);
        View root = findViewById(R.id.download_confirm_root);
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            root.setBackgroundResource(R.drawable.download_confirm_background_portrait);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            root.setBackgroundResource(R.drawable.download_confirm_background_landscape);
        }
        close = findViewById(R.id.download_confirm_close);
        close.setOnClickListener(this);
        reloadButton = findViewById(R.id.download_confirm_reload_button);
        reloadButton.setOnClickListener(this);
        confirm = findViewById(R.id.download_confirm_confirm);
        confirm.setOnClickListener(this);
        loadingBar = findViewById(R.id.download_confirm_progress_bar);
        contentHolder = findViewById(R.id.download_confirm_content);
        createTextView();
    }

    private void createTextView() {
        FrameLayout layout = findViewById(R.id.download_confirm_holder);
        webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new Client());
        layout.addView(webView);
    }

    @Override
    public void show() {
        super.show();
        try {
            loadUrl(url);
        } catch (Exception e) {
            Log.e(DownloadApkConfirmDialogWebView.TAG, "load error url:" + url, e);
        }
    }


    private void loadUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            loadingBar.setVisibility(View.GONE);
            contentHolder.setVisibility(View.GONE);
            reloadButton.setVisibility(View.VISIBLE);
            reloadButton.setText(LOAD_ERROR_TEXT);
            reloadButton.setEnabled(false);
            return;
        }
        urlLoadError = false;
        Log.d(TAG, "download confirm load url:" + url);
        webView.loadUrl(url);
    }

    @Override
    protected void onStart() {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = (int) (height * 0.6);
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.windowAnimations = R.style.DownloadConfirmDialogAnimationUp;
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutParams.width = (int) (width * 0.5);
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.gravity = Gravity.RIGHT;
            layoutParams.windowAnimations = R.style.DownloadConfirmDialogAnimationRight;
        }
        //弹窗外区域蒙版50%透明度
        layoutParams.dimAmount = 0.5f;

        //resume后动画会重复，在显示出来后重置无动画
        window.setAttributes(layoutParams);
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                try {
                    Window window = getWindow();
                    window.setWindowAnimations(0);
                } catch (Throwable t) {
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == close) {
            if (callBack != null) {
                callBack.onCancel();
            }
            dismiss();
        } else if (v == confirm) {
            if (callBack != null) {
                callBack.onConfirm();
            }
            dismiss();
        } else if (v == reloadButton) {
            loadUrl(url);
        }

    }

    @Override
    public void cancel() {
        super.cancel();
        if (callBack != null) {
            callBack.onCancel();
        }
    }

    class Client extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!urlLoadError) {
                loadingBar.setVisibility(View.GONE);
                reloadButton.setVisibility(View.GONE);
                contentHolder.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Log.d(TAG, "doConfirmWithInfo onReceivedError:" + error + " " + request);
            urlLoadError = true;
            loadingBar.setVisibility(View.GONE);
            contentHolder.setVisibility(View.GONE);
            reloadButton.setVisibility(View.VISIBLE);
            reloadButton.setText(RELOAD_TEXT);
            reloadButton.setEnabled(true);
        }
    }


}
