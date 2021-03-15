package com.ifmvo.togetherad.gdt.other;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.qq.e.comm.compliance.DownloadConfirmCallBack;
import com.qq.e.comm.compliance.DownloadConfirmListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 * Copy from gdt demo.
 */
public class DownloadConfirmHelper {
    public static final String TAG = "DownloadConfirmHelper";

    private static final String JSON_INFO_PARAMETER = "&resType=api";

    private static final String JSON_RESULT_KEY = "ret";
    private static final String JSON_DATA_KEY = "data";

    private static final String ICON_URL_KEY = "iconUrl";//App Icon
    private static final String APP_NAME_KEY = "appName";//App 名称
    private static final String VERSION_NAME_KEY = "versionName";//版本号
    private static final String AUTHOR_NAME_KEY = "authorName";//开发者
    private static final String PERMISSIONS_KEY = "permissions";//权限列表（具体权限信息，非URL）
    private static final String PRIVACY_AGREEMENT_KEY = "privacyAgreement";//隐私政策（URL）
    private static final String UPDATE_TIME_KEY = "apkPublishTime";//版本更新时间
    private static final String APK_FILE_SIZE_KEY = "fileSize";//apk文件大小，bytes

    public static class ApkInfo {
        public String iconUrl;
        public String appName;
        public String versionName;
        public String authorName;
        public List<String> permissions;
        public String privacyAgreementUrl;
        public long apkPublishTime;
        public long fileSize;
    }

    public static final DownloadConfirmListener DOWNLOAD_CONFIRM_LISTENER = new DownloadConfirmListener() {
        @Override
        public void onDownloadConfirm(Activity context, int scenes, String infoUrl, DownloadConfirmCallBack callBack) {
            Log.d(TAG, "scenes:" + scenes + " info url:" + infoUrl);

            //获取对应的json数据并自定义显示
//            new DownloadApkConfirmDialog(context, getApkJsonInfoUrl(infoUrl), callBack).show();

            new DownloadApkConfirmDialogWebView(context, infoUrl, callBack).show();//使用webview显示
        }
    };

    public static String getApkJsonInfoUrl(String infoUrl) {
        return infoUrl + JSON_INFO_PARAMETER;
    }

    public static ApkInfo getAppInfoFromJson(String jsonStr) {
        ApkInfo result = null;

        if (TextUtils.isEmpty(jsonStr)) {
//            Log.d(TAG, "请求应用信息返回值为空");
            return null;
        }
        try {
            JSONObject json = new JSONObject(jsonStr);
            int retCode = json.optInt(JSON_RESULT_KEY, -1);
            if (retCode != 0) {
                Log.d(TAG, "请求应用信息返回值错误");
                return null;
            }
            JSONObject dataJson = json.optJSONObject(JSON_DATA_KEY);
            if (dataJson == null) {
                Log.d(TAG, "请求应用信息返回值错误" + JSON_DATA_KEY);
                return null;
            }

            if (dataJson != null) {
                result = new ApkInfo();
                result.iconUrl = dataJson.optString(ICON_URL_KEY);
                result.appName = dataJson.optString(APP_NAME_KEY);
                result.versionName = dataJson.optString(VERSION_NAME_KEY);
                result.authorName = dataJson.optString(AUTHOR_NAME_KEY);
                JSONArray jsonPermissions = dataJson.optJSONArray(PERMISSIONS_KEY);
                if (jsonPermissions != null) {
                    result.permissions = new ArrayList<>();
                    for (int i = 0; i < jsonPermissions.length(); i++) {
                        result.permissions.add(jsonPermissions.getString(i));
                    }
                }
                result.privacyAgreementUrl = dataJson.optString(PRIVACY_AGREEMENT_KEY);
                //后台返回的时间可能是秒也可能是毫秒，这里需要统一下为毫秒
                //2000年1月1日1时0分0秒对应的 秒是946688401 毫秒是 946688401000
                long publicTime = dataJson.optLong(UPDATE_TIME_KEY);
                result.apkPublishTime = publicTime > 946688401000L ? publicTime : publicTime * 1000;
                result.fileSize = dataJson.optLong(APK_FILE_SIZE_KEY);//单位是字节
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
