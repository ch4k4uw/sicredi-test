import configs.AndroidConfigs
import extensions.implementation
import extensions.internalModule
import extensions.kapt

plugins {
    id(Plugins.Android.Application)
    id(Plugins.Kotlin.Android)
    id(Plugins.Kotlin.Parcelize)
    id(Plugins.Hilt)
    id(Plugins.Kotlin.Kapt)
    id(Plugins.Android.NavigationSafeArgs)
}

android {
    compileSdk = AndroidConfigs.Sdk.Compile

    defaultConfig {
        multiDexEnabled = true
        applicationId = AndroidConfigs.appId
        minSdk = AndroidConfigs.Sdk.Min
        targetSdk = AndroidConfigs.Sdk.Target
        versionCode = Versions.App.Code
        versionName = Versions.App.Name

        testInstrumentationRunner = AndroidConfigs.InstrumentationTestRunner
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.Compose.Compose
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    packagingOptions {
        resources.excludes += "META-INF/DEPENDENCIES.txt"
        resources.excludes += "META-INF/LICENSE.txt"
        resources.excludes += "META-INF/NOTICE.txt"
        resources.excludes += "META-INF/NOTICE"
        resources.excludes += "META-INF/LICENSE"
        resources.excludes += "META-INF/DEPENDENCIES"
        resources.excludes += "META-INF/notice.txt"
        resources.excludes += "META-INF/license.txt"
        resources.excludes += "META-INF/dependencies.txt"
        resources.excludes += "META-INF/LGPL2.1"
    }

    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("./app/src/debug/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    testOptions {
        unitTests.apply {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
            animationsDisabled = true
        }
    }

    kapt {
        correctErrorTypes = true
    }

}

configurations {
    api.configure {
        //exclude(group = "androidx.annotation", module = "annotation")
    }
}

dependencies {
    configureBaseDependencies()
    configureComposeDependencies()
    configureBaseUiDependencies()

    //Internal modules
    internalModule(InternalModules.presenter)

    //UI
    implementation(Libraries.AndroidX.Ktx.Core)
    implementation(Libraries.AndroidX.AppCompat)
    implementation(Libraries.Google.Material)
    implementation(Libraries.AndroidX.ActivityKtx)
    implementation(Libraries.AndroidX.FragmentKtx)
    implementation(Libraries.AndroidX.NavigationKtx)
    implementation(Libraries.AndroidX.NavigationUiKtx)
    implementation(Libraries.AndroidX.CollectionKtx)
    implementation(Libraries.AndroidX.SwipeToRefresh)

    //Hilt
    implementation(Libraries.Google.Hilt.Android)
    kapt(Libraries.Google.Hilt.Compiler)

    //Multidex
    implementation(Libraries.AndroidX.Multidex)

    //Data store
    implementation(Libraries.DataStore)
    implementation(Libraries.DataStorePreferences)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:core:1.4.0")
    androidTestImplementation("androidx.test.ext:truth:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}