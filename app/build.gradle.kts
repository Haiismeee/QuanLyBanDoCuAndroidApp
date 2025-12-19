plugins {
    alias(libs.plugins.androidApplication)

    id("com.google.gms.google-services")
}

android {

    namespace = "com.example.qlybandocu"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.qlybandocu"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //ssp
    implementation("com.intuit.sdp:sdp-android:1.1.1")
    implementation("com.intuit.ssp:ssp-android:1.1.1")
    //lottie
    implementation ("com.airbnb.android:lottie:6.6.6")
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.2.0")

    //glide
    implementation ("com.github.bumptech.glide:glide:5.0.5")

    //image circle
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    //firebase
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore:25.0.0")
    implementation("com.google.android.gms:play-services-auth:20.6.0")
    implementation("androidx.datastore:datastore-preferences:1.1.0")
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    //paper
    implementation ("io.github.pilgr:paperdb:2.7.2")
    //json
    implementation ("com.google.code.gson:gson:2.13.2")
    implementation ("com.github.dhaval2404:imagepicker:2.1")

    implementation("androidx.multidex:multidex:2.0.1")
    implementation("io.reactivex.rxjava3:rxjava:3.1.8")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.9.0")

}