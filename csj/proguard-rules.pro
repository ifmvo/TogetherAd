# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#------------------------穿山甲的混淆---------------------------#
-keep class com.ifmvo.togetherad.csj.** { *; }

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
-keep class com.bytedance.sdk.openadsdk.** {*;}
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
-keep class com.pgl.sys.ces.* {*;}

-keepclassmembers class * {
    *** getContext(...);
    *** getActivity(...);
    *** getResources(...);
    *** startActivity(...);
    *** startActivityForResult(...);
    *** registerReceiver(...);
    *** unregisterReceiver(...);
    *** query(...);
    *** getType(...);
    *** insert(...);
    *** delete(...);
    *** update(...);
    *** call(...);
    *** setResult(...);
    *** startService(...);
    *** stopService(...);
    *** bindService(...);
    *** unbindService(...);
    *** requestPermissions(...);
    *** getIdentifier(...);
   }

-keep class com.bytedance.pangle.** {*;}
-keep class com.bytedance.sdk.openadsdk.** { *; }

-keep class ms.bd.c.Pgl.**{*;}
-keep class com.bytedance.mobsec.metasec.ml.**{*;}

-keep class com.bytedance.embedapplog.** {*;}
-keep class com.bytedance.embed_dr.** {*;}

-keep class com.bykv.vk.** {*;}

-keep class com.lynx.** { *; }

-keep class com.ss.android.**{*;}

-keep class android.support.v4.app.FragmentActivity{}
-keep class androidx.fragment.app.FragmentActivity{}
-keepclassmembernames class *{
	*** _GET_PLUGIN_PKG();
}
-keep class androidx.fragment.app.FragmentFactory{
    *** sClassMap;
}
-keep class com.volcengine.zeus.LocalBroadcastManager{
    *** getInstance(**);
    *** registerReceiver(**,**);
    *** unregisterReceiver(**);
    *** sendBroadcast(**);
    *** sendBroadcastSync(**);
}
-keep class com.bytedance.pangle.LocalBroadcastManager{
    *** getInstance(**);
    *** registerReceiver(**,**);
    *** unregisterReceiver(**);
    *** sendBroadcast(**);
    *** sendBroadcastSync(**);
}


-keep class android.support.v4.app.FragmentActivity{}
-keep class androidx.fragment.app.FragmentActivity{}
-keepclassmembernames class *{
	*** _GET_PLUGIN_PKG();
}
-keep class androidx.fragment.app.FragmentFactory{
    *** sClassMap;
}
-keep class com.volcengine.zeus.LocalBroadcastManager{
    *** getInstance(**);
    *** registerReceiver(**,**);
    *** unregisterReceiver(**);
    *** sendBroadcast(**);
    *** sendBroadcastSync(**);
}
-keep class com.bytedance.pangle.LocalBroadcastManager{
    *** getInstance(**);
    *** registerReceiver(**,**);
    *** unregisterReceiver(**);
    *** sendBroadcast(**);
    *** sendBroadcastSync(**);
}

