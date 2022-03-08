import extensions.implementation
import extensions.internalApi
import extensions.kapt
import extensions.testImplementation

plugins {
    id(Plugins.Android.LibraryModule)
    id(Plugins.Kotlin.Parcelize)
}

android {
}

dependencies {
    configureBaseUiDependencies()

    //Internal modules
    internalApi(InternalModules.core)
    internalApi(InternalModules.domain)

    //Hilt
    implementation(Libraries.Google.Hilt.Android)
    kapt(Libraries.Google.Hilt.Compiler)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}