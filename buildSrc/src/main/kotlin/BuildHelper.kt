import configs.AndroidConfigs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import extensions.implementation
import org.gradle.api.JavaVersion
import com.android.build.gradle.BaseExtension

fun Project.configureBaseDependencies() = dependencies {
    implementation(Libraries.Kotlin.StdLib)
    implementation(Libraries.Kotlin.Coroutines)
    implementation(Libraries.Kotlin.CoroutinesAndroid)

    add("coreLibraryDesugaring", Libraries.JavaDesugaring)
}

fun Project.configureBaseUiDependencies() = dependencies {
    implementation(Libraries.LifeCycle.Runtime)
    implementation(Libraries.LifeCycle.LiveData)
    implementation(Libraries.LifeCycle.ViewModel)
    implementation(Libraries.LifeCycle.Compiler)
}

fun Project.configureNetworkingDependencies() = dependencies {
    implementation(Libraries.Retrofit.Retrofit)
    implementation(Libraries.Retrofit.GSonConverter)
}

fun Project.configureAsAndroidLibrary() {
    val androidBase = project
        .extensions
        .findByName("android") as? BaseExtension

    androidBase?.apply {
        compileSdkVersion(AndroidConfigs.Sdk.Compile)

        defaultConfig {
            multiDexEnabled = true
            minSdk = AndroidConfigs.Sdk.Min
            targetSdk = AndroidConfigs.Sdk.Target
            versionCode = Versions.App.Code
            versionName = Versions.App.Name

            testInstrumentationRunner = AndroidConfigs.InstrumentationTestRunner
        }

        buildTypes {
            getByName("release") {
                isMinifyEnabled = true
                proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            }

            getByName("debug") {
                isDebuggable = true
                isTestCoverageEnabled = true
            }
        }

        testOptions {
            unitTests.apply {
                isIncludeAndroidResources = true
                isReturnDefaultValues = true
                animationsDisabled = true
            }
        }

        compileOptions {
            isCoreLibraryDesugaringEnabled = true
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }
}