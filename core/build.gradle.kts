import extensions.implementation
import extensions.api
import extensions.kapt

plugins {
    id(Plugins.Android.LibraryModule)
    id(Plugins.Kotlin.Parcelize)
}

android {
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.Compose.Compose
    }
}

dependencies {
    configureBaseUiDependencies()
    configureComposeDependencies()
    configureNetworkingDependencies()

    // UI
    api(Libraries.Compose.Icons)
    api(Libraries.Compose.IconsExtended)
    implementation(Libraries.AndroidX.Ktx.Core)
    implementation(Libraries.AndroidX.AppCompat)
    implementation(Libraries.Google.Material)

    // Networking
    implementation(Libraries.LoggingInterceptor)

    // Accompanist
    api(Libraries.Accompanist.Insets)
    api(Libraries.Accompanist.InsetsUi)
    api(Libraries.Accompanist.SysUIController)

    // Image download
    implementation(Libraries.Glide)

    // Logging
    api(Libraries.Timber)

    // Hilt
    implementation(Libraries.Google.Hilt.Android)
    kapt(Libraries.Google.Hilt.Compiler)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}