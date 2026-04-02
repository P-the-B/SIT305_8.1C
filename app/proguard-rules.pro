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

# Retrofit — keep interface methods and annotations
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclassmembernames interface * {
    @retrofit2.http.* <methods>;
}

# Gson — keep model classes used for JSON serialisation
-keep class com.example.llmchatbot.network.model.** { *; }

# Room — keep entity and DAO classes
-keep class com.example.llmchatbot.model.** { *; }
-keep class com.example.llmchatbot.data.db.** { *; }

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# Keep BuildConfig so API key reference survives minification
-keep class com.example.llmchatbot.BuildConfig { *; }