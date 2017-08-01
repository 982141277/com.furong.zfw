# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Administrator\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontoptimize
-optimizationpasses 5
-dontusemixedcaseclassnames

-dontpreverify
-verbose

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes InnerClasses,LineNumberTable
-printmapping mapping.txt

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.app.View
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.**

-keep class com.meiyin.erp.GreenDao.** { *; }
-keep class com.meiyin.erp.entity.** { *; }

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class **.R$* {
    *;
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}




##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.ssll.entity.simple.** { *; }

##---------------End: proguard configuration for Gson  ----------


#DOM4J
-dontwarn org.dom4j.**
-keep class org.dom4j.**{*;}


#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
#baidumap
-dontwarn com.baidu.**
-dontwarn com.baidu.mapapi.**
-dontwarn com.baidu.platform.comapi.**
-dontwarn com.baidu.location.**
-dontwarn com.baidu.vi.**
-dontwarn vi.com.gdi.bgl.android.**

-keep class com.baidu.**{*;}
-keep class com.baidu.mapapi.**{*;}
-keep class com.baidu.platform.comapi.**{*;}
-keep class com.baidu.location.**{*;}
-keep class com.baidu.vi.**{*;}
-keep class vi.com.gdi.bgl.android.**{*;}


#-libraryjars ../myandroidlibrary/src/main/java/libs/junrar-0.7.jar
#-libraryjars ../myandroidlibrary/src/main/java/libs/dom4j-1.6.1.jar
#-libraryjars ../myandroidlibrary/src/main/java/libs/universal-image-loader-1.9.3-with-sources.jar
#-libraryjars ../myandroidlibrary/src/main/java/libs/android-async-http-1.4.5.jar


-keep class org.apache.**{*;}
-dontwarn org.apache.**

-keep class org.greenrobot.greendao.**{*;}
-dontwarn org.greenrobot.greendao.**
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties
##
-dontwarn freemarker.**
-keep class freemarker.** { *;}

#retrofit2
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-dontwarn org.robovm.**
-keep class org.robovm.** { *; }

#okhttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-keep class okhttp3.** { *;}
-keep class okio.** { *;}
-dontwarn sun.security.**
-keep class sun.security.** { *;}
-dontwarn okio.**
-dontwarn okhttp3.**

-keep class com.github.junrar.**{*;}
-dontwarn com.github.junrar.**
#rxjava
-dontwarn rx.**
-keep class rx.** { *; }