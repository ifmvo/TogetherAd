package com.ifmvo.togetherad.gdt.other;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * Copy from gdt demo.
 */
public class NetworkRequestAsyncTask extends AsyncTask<String, Void, String> {
    public static final String TAG = "NetworkRequestAsyncTask";
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;

    @Override
    protected String doInBackground(String... params) {
        String stringUrl = params[0];

        if (TextUtils.isEmpty(stringUrl)) {
            return null;
        }

        String httpResult;
        String inputLine;
        Log.d(TAG, "开始请求应用信息:" + stringUrl);
        try {
            URL myUrl = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection)
                    myUrl.openConnection();
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            connection.connect();
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);

            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            reader.close();
            streamReader.close();
            httpResult = stringBuilder.toString();
        } catch (IOException e) {
            Log.e(TAG, "请求应用信息错误", e);
            httpResult = null;
        }
        return httpResult;
    }
}
