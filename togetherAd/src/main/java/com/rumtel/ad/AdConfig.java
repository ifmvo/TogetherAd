package com.rumtel.ad;

import android.content.Context;
import com.baidu.mobads.AdView;


/*
 * (●ﾟωﾟ●)
 *
 * 广告的配置
 *
 * Created by Matthew_Chen on 2018/8/8.
 */
public class AdConfig {

    public static String MASK_NAME = "HIDE";

    /**
     * ------------------------百度 Mob 👇------------------------
     */
    public static String BAIDU_AD_NAME = "百度";
    private static String BAIDU_AD_APP_ID;//APP_ID
    public static String BAIDU_AD_SPLASH;//开屏
    public static String BAIDU_AD_INTER;//插屏
    public static String BAIDU_AD_PLAYER;//播放器前贴
    public static String BAIDU_AD_HOT;//热播信息流

    /**
     * ------------------------腾讯 广点通 👇-----------------------
     */
    public static String GDT_AD_NAME = "广点通";
    public static String GDT_AD_APP_ID;//APP_ID
    public static String GDT_AD_SPLASH;//开屏
    public static String GDT_AD_INTER;//插屏
    public static String GDT_AD_HOT;//热播信息流
    public static String GDT_AD_PLAYER;//播放器前贴

    public static void init(Context context) {
        /*
         * ------------------------百度 Mob 👇------------------------
         */

        AdView.setAppSid(context, BAIDU_AD_APP_ID);
//        AppActivity.setActionBarColorTheme(AppActivity.ActionBarColorTheme.ACTION_BAR_WHITE_THEME);
//        AdSettings.setSupportHttps(false);

        /*
         * ------------------------腾讯 GDT 👇------------------------
         */
//        MultiProcessFlag.setMultiProcess(true);
    }

    public static void setAdConfigBaidu(AdConfigBaiduId adConfigId) {
        if (adConfigId != null) {
            BAIDU_AD_APP_ID = adConfigId.BAIDU_AD_APP_ID();
            BAIDU_AD_SPLASH = adConfigId.BAIDU_AD_SPLASH();
            BAIDU_AD_INTER = adConfigId.BAIDU_AD_INTER();
            BAIDU_AD_PLAYER = adConfigId.BAIDU_AD_PLAYER();
            BAIDU_AD_HOT = adConfigId.BAIDU_AD_HOT();
        }
    }

    public static void setAdConfigTencent(AdConfigGDTId adConfigId) {
        if (adConfigId != null) {
            GDT_AD_APP_ID = adConfigId.GDT_AD_APP_ID();
            GDT_AD_SPLASH = adConfigId.GDT_AD_SPLASH();
            GDT_AD_INTER = adConfigId.GDT_AD_INTER();
            GDT_AD_HOT = adConfigId.GDT_AD_HOT();
            GDT_AD_PLAYER = adConfigId.GDT_AD_PLAYER();
        }
    }

//    public static IAdConfig getBaiduConfig() {
//        IAdConfig adConfig = new com.baidu.mobads.AdConfig();
//        adConfig.setAppsid(BAIDU_AD_APP_ID);
//        return adConfig;
//    }

    public interface AdConfigBaiduId {
        String BAIDU_AD_APP_ID();

        String BAIDU_AD_SPLASH();

        String BAIDU_AD_INTER();

        String BAIDU_AD_PLAYER();

        String BAIDU_AD_HOT();
    }

    public interface AdConfigGDTId {
        String GDT_AD_APP_ID();

        String GDT_AD_SPLASH();

        String GDT_AD_INTER();

        String GDT_AD_HOT();

        String GDT_AD_PLAYER();
    }
}
