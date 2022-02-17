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
    }
}

dependencies {
    configureBaseUiDependencies()
    configureNetworkingDependencies()
    implementation(Libraries.LoggingInterceptor)
    implementation(Libraries.Glide)
    api(Libraries.Timber)

    //Hilt
    implementation(Libraries.Google.Hilt.Android)
    kapt(Libraries.Google.Hilt.Compiler)


    implementation(Libraries.AndroidX.Ktx.Core)
    implementation(Libraries.AndroidX.AppCompat)
    implementation(Libraries.Google.Material)
    implementation(Libraries.Glide)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}