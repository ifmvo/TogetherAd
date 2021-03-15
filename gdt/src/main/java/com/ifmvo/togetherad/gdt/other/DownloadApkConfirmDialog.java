package com.ifmvo.togetherad.gdt.other;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ifmvo.togetherad.gdt.R;
import com.qq.e.comm.compliance.DownloadConfirmCallBack;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;


/*
 * Copy from gdt demo.
 */
public class DownloadApkConfirmDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = "ConfirmDialog";
    private Context context;
    private int orientation;
    private DownloadConfirmCallBack callBack;
    private TextView textView;
    private ImageView close;
    private Button confirm;

    private ViewGroup contentHolder;
    private ProgressBar loadingBar;
    private Button reloadButton;

    private String url;

    private static final String RELOAD_TEXT = "重新加载";
    private static final String LOAD_ERROR_TEXT = "抱歉，应用信息获取失败";

    public DownloadApkConfirmDialog(Context context, String infoUrl,
                                    DownloadConfirmCallBack callBack) {
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
        textView = new TextView(context);
        ScrollView sv = new ScrollView(context);
        sv.addView(textView);
        layout.addView(sv);
    }

    @Override
    public void show() {
        super.show();
        try {
            loadUrl(url);
        } catch (Exception e) {
            Log.e(DownloadApkConfirmDialog.TAG, "load error url:" + url, e);
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
        new NetworkRequestAsyncTask() {
            @Override
            protected void onPostExecute(String str) {
                loadingBar.setVisibility(View.GONE);
                reloadButton.setVisibility(View.GONE);
                contentHolder.setVisibility(View.VISIBLE);

                DownloadConfirmHelper.ApkInfo apkInfo = DownloadConfirmHelper.getAppInfoFromJson(str);
                if (apkInfo == null) {
                    loadingBar.setVisibility(View.GONE);
                    reloadButton.setVisibility(View.VISIBLE);
                    contentHolder.setVisibility(View.GONE);
                    return;
                }


                textView.append("icon链接:\n");
                textView.append(apkInfo.iconUrl);

                textView.append("\n应用名:\n");
                textView.append("\t" + apkInfo.appName);

                textView.append("\n应用版本:\n");
                textView.append("\t" + apkInfo.versionName);

                textView.append("\n开发者:\n");
                textView.append("\t" + apkInfo.authorName);

                textView.append("\n应用大小:\n");
                textView.append("\t" + readableFileSize(apkInfo.fileSize));

                textView.append("\n更新时间:\n");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                textView.append("\t" + sdf.format(new Date(apkInfo.apkPublishTime)));

                textView.append("\n隐私条款链接:\n");
                textView.append(apkInfo.privacyAgreementUrl);

                textView.append("\n权限信息:\n");

                for (String i : apkInfo.permissions) {
                    textView.append("\t" + i + "\n");
                }
                Linkify.TransformFilter filter = new Linkify.TransformFilter() {
                    public final String transformUrl(final Matcher match, String url) {
                        return match.group();
                    }
                };
                Linkify.addLinks(textView, Patterns.WEB_URL, null, null, filter);
                loadingBar.setVisibility(View.GONE);
                reloadButton.setVisibility(View.GONE);
                contentHolder.setVisibility(View.VISIBLE);
            }
        }.execute(url);
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

    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

}
