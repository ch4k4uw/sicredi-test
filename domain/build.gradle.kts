import extensions.implementation
import extensions.internalModule
import extensions.kapt
import extensions.testImplementation

plugins {
    id(Plugins.Android.LibraryModule)
    id(Plugins.Kotlin.Parcelize)
    id(Plugins.Hilt)
    id(Plugins.Kotlin.Kapt)
}

android {
    buildTypes {
        debug {
            buildConfigField("String", "INSTACREDI_API_URL", "\"https://5f5a8f24d44d640016169133.mockapi.io/\"")
        }
        release {
            buildConfigField("String", "INSTACREDI_API_URL", "\"https://5f5a8f24d44d640016169133.mockapi.io/\"")
        }
    }
}

dependencies {
    configureNetworkingDependencies()

    //Internal modules
    internalModule(InternalModules.core)

    //Hilt
    implementation(Libraries.Google.Hilt.Android)
    kapt(Libraries.Google.Hilt.Compiler)

    //Data store
    implementation(Libraries.DataStore)
    implementation(Libraries.DataStorePreferences)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}