plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.tehnici_actuale_motoare_anti_malware"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tehnici_actuale_motoare_anti_malware"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // Java 11 bytecode
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // --- Dependențe de bază AndroidX ---
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.github.PhilJay:MPAndroidChart:v3.0.2")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")

    testImplementation(libs.junit)               // 'junit:junit:4.13.2'
    androidTestImplementation(libs.ext.junit)    // 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation(libs.espresso.core)// 'androidx.test.espresso:espresso-core:3.5.1'
}
