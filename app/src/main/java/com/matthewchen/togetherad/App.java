//package com.matthewchen.togetherad;
//
//import android.app.Application;
//import com.rumtel.ad.TogetherAd;
//
//import java.util.HashMap;
//import java.util.Map;
//
///*
// * (●ﾟωﾟ●)
// *
// * Created by Matthew_Chen on 2018/12/20.
// */
//public class App extends Application {
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        Map<String, String> baiduIdMap = new HashMap<>();
//        baiduIdMap.put(TogetherAdConst.AD_SPLASH, "2543740");
//        baiduIdMap.put(TogetherAdConst.AD_INTER, "2543741");
//        baiduIdMap.put(TogetherAdConst.AD_FLOW_INDEX, "2715031");
//        baiduIdMap.put(TogetherAdConst.AD_TIEPIAN_LIVE, "5985131");
//        TogetherAd.INSTANCE.initBaiduAd(getApplicationContext(), "ee93e58e", baiduIdMap);
//
//        Map<String, String> gdtIdMap = new HashMap<>();
//        baiduIdMap.put(TogetherAdConst.AD_SPLASH, "8030228893573270");
//        baiduIdMap.put(TogetherAdConst.AD_INTER, "4090620883979242");
//        baiduIdMap.put(TogetherAdConst.AD_FLOW_INDEX, "4010231735332811");
//        baiduIdMap.put(TogetherAdConst.AD_TIEPIAN_LIVE, "4060449650093530");
//        TogetherAd.INSTANCE.initGDTAd(getApplicationContext(), "1106572734", gdtIdMap);
//
//        Map<String, String> iFlyIdMap = new HashMap<>();
//        baiduIdMap.put(TogetherAdConst.AD_SPLASH, "FD0AC8FDE5CE0B317A6C4077E68D34CC");
//        baiduIdMap.put(TogetherAdConst.AD_INTER, "6FD44C667D5EFD97730CC1E3F174D965");
//        baiduIdMap.put(TogetherAdConst.AD_FLOW_INDEX, "EE2009111A1DF0BCA9DAD3723A95602F");
//        baiduIdMap.put(TogetherAdConst.AD_TIEPIAN_LIVE, "93D157AAFA8B7EF64165B1F0ECEE2623");
//        TogetherAd.INSTANCE.initXunFeiAd(getApplicationContext(), iFlyIdMap);
//
//    }
//}
