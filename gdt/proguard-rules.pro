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
-keep class com.qq.e.** {
    public protected *;
}
-keep class android.support.v4.**{
    public *;
}
-keep class android.support.v7.**{
    public *;
}
-keep class yaq.gdtadv{
    *;
}
-keep class com.qq.e.** {
    *;
}
-keep class com.tencent.** {
    *;
}
-keep class cn.mmachina.JniClient {
    *;
}
-keep class c.t.m.li.tsa.** {
    *;
}

-keep class * extends java.lang.annotation.Annotation { *; }
-keep interface * extends java.lang.annotation.Annotation { *; }

-keep, allowobfuscation class com.qq.e.comm.plugin.services.SDKServerService {*;}
-keepclassmembers, allowobfuscation class com.qq.e.comm.plugin.net.SecurePackager {
    public *;
}

-keepclasseswithmembers,includedescriptorclasses class * {
native <methods>;
}

-keep class * extends com.qq.e.mediation.interfaces.BaseNativeUnifiedAd { *; }
-keep class * extends com.qq.e.mediation.interfaces.BaseSplashAd { *; }
-keep class * extends com.qq.e.mediation.interfaces.BaseRewardAd { *; }


-keep class com.ifmvo.togetherad.gdt.** { *; }