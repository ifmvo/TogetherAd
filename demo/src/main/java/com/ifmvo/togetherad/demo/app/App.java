//package com.ifmvo.togetherad.demo;
//
//import android.app.Application;
//
//import com.ifmvo.togetherad.baidu.TogetherAdBaidu;
//import com.ifmvo.togetherad.csj.TogetherAdCsj;
//import com.ifmvo.togetherad.gdt.TogetherAdGdt;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by Matthew Chen on 2020/6/23.
// */
//public class App extends Application {
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        //初始化穿山甲
//        TogetherAdCsj.INSTANCE.init(this, AdProviderType.CSJ.getType(), "5001121", this.getString(R.string.app_name));
//        //初始化广点通
//        TogetherAdGdt.INSTANCE.init(this, AdProviderType.GDT.getType(), "1101152570");
//        //初始化百青藤
//        TogetherAdBaidu.INSTANCE.init(this, AdProviderType.BAIDU.getType(), "e866cfb0");
//
//        /*
//         * -------------配置所有广告位ID--------------
//         */
//        //穿山甲
//        Map<String, String> idMapCsj = new HashMap<>();
//        idMapCsj.put(TogetherAdAlias.AD_SPLASH, "801121648");
//        idMapCsj.put(TogetherAdAlias.AD_NATIVE_SIMPLE, "901121737");
//        idMapCsj.put(TogetherAdAlias.AD_NATIVE_RECYCLERVIEW, "901121737");
//        idMapCsj.put(TogetherAdAlias.AD_BANNER, "901121246");
//        idMapCsj.put(TogetherAdAlias.AD_REWARD, "901121365");
//        TogetherAdCsj.INSTANCE.setIdMapCsj(idMapCsj);
//
//        //广点通
//        Map<String, String> idMapGdt = new HashMap<>();
//        idMapGdt.put(TogetherAdAlias.AD_SPLASH, "8863364436303842593");
//        idMapGdt.put(TogetherAdAlias.AD_NATIVE_SIMPLE, "6040749702835933");
//        idMapGdt.put(TogetherAdAlias.AD_NATIVE_RECYCLERVIEW, "6040749702835933");
//        idMapGdt.put(TogetherAdAlias.AD_BANNER, "4080052898050840");
//        idMapGdt.put(TogetherAdAlias.AD_REWARD, "2090845242931421");
//        TogetherAdGdt.INSTANCE.setIdMapGDT(idMapGdt);
//
//        //百度Mob
//        Map<String, String> idMapBaidu = new HashMap<>();
//        idMapBaidu.put(TogetherAdAlias.AD_SPLASH, "2058622");
//        idMapBaidu.put(TogetherAdAlias.AD_NATIVE_SIMPLE, "2058628");
//        idMapBaidu.put(TogetherAdAlias.AD_NATIVE_RECYCLERVIEW, "2058628");
//        idMapBaidu.put(TogetherAdAlias.AD_BANNER, "2015351");
//        idMapBaidu.put(TogetherAdAlias.AD_REWARD, "5925490");
//        TogetherAdBaidu.INSTANCE.setIdMapBaidu(idMapBaidu);
//    }
//}
