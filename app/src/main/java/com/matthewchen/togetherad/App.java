package com.matthewchen.togetherad;

import android.app.Application;
import com.rumtel.ad.AdConfig;

/*
 * (●ﾟωﾟ●)
 *
 * Created by Matthew_Chen on 2018/12/20.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initTogetherAd();
    }

    private void initTogetherAd() {
        AdConfig.setAdConfigBaidu(new AdConfig.AdConfigBaiduId() {
            @Override
            public String BAIDU_AD_APP_ID() {
//                return "";
                return "cd3d8b16";
            }

            @Override
            public String BAIDU_AD_SPLASH() {
                return "5873732";
            }

            @Override
            public String BAIDU_AD_INTER() {
                return "5873733";
            }

            @Override
            public String BAIDU_AD_PLAYER() {
                return "5873734";
            }

            @Override
            public String BAIDU_AD_HOT() {
                return "5873735";
            }
        });

        AdConfig.setAdConfigTencent(new AdConfig.AdConfigGDTId() {
            @Override
            public String GDT_AD_APP_ID() {
                return "1107588700";
//                return "";
            }

            @Override
            public String GDT_AD_SPLASH() {
                return "7050336739309371";
            }

            @Override
            public String GDT_AD_INTER() {
                return "8040732709304302";
            }

            @Override
            public String GDT_AD_HOT() {
                return "9040038759705323";
            }

            @Override
            public String GDT_AD_PLAYER() {
                return "3040239779004334";
            }
        });

        AdConfig.init(getApplicationContext());
    }
}
