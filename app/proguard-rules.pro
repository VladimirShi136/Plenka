# ProGuard rules for Plenka

# OkHttp & Retrofit
-dontwarn okhttp3.**
-dontwarn okio.**
-keepattributes Signature
-keepattributes *Annotation*

# Gson DTOs
-keep class com.app.plenka.data.remote.dto.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Room Entities & DAOs
-keep class com.app.plenka.data.local.entity.** { *; }
-keep class com.app.plenka.data.local.** { *; }
-keepattributes *Annotation*,EnclosingMethod,InnerClasses

# Hilt
-keep class com.app.plenka.** { *; }
-dontwarn dagger.hilt.android.internal.builders.**
-dontwarn dagger.hilt.android.internal.modules.**
