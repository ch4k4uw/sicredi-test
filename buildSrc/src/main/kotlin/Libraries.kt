object Libraries {
    object Kotlin {
        const val StdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.Kotlin.Gradle}"
        const val Coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Kotlin.Coroutines}"
        const val CoroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.Kotlin.Coroutines}"
        const val CoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Kotlin.Coroutines}"
        const val CoroutinesPlayServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.Kotlin.CoroutinesPlayServices}"
    }
    object LifeCycle {
        const val Runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LifeCycle}"
        const val LiveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LifeCycle}"
        const val ViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LifeCycle}"
        const val Kapt = "androidx.lifecycle:lifecycle-compiler:${Versions.LifeCycle}"
        const val Compiler = "androidx.lifecycle:lifecycle-common-java8:${Versions.LifeCycle}"
    }
    const val JavaDesugaring = "com.android.tools:desugar_jdk_libs:${Versions.JavaDesugaring}"
    object AndroidX {
        object Ktx {
            const val Core = "androidx.core:core-ktx:${Versions.AndroidX.Ktx.Core}"
        }
        const val AppCompat = "androidx.appcompat:appcompat:${Versions.AndroidX.AppCompat}"
        const val Palette = "androidx.palette:palette:${Versions.AndroidX.Palette}"
        const val Multidex = "androidx.multidex:multidex:${Versions.AndroidX.Multidex}"
        const val ArchCoreTesting = "androidx.arch.core:core-testing:${Versions.AndroidX.ArchCoreTesting}"
    }
    object Google {
        const val Material = "com.google.android.material:material:${Versions.Google.Material}"
        object Hilt {
            const val Android = "com.google.dagger:hilt-android:${Versions.Hilt}"
            const val Compiler = "com.google.dagger:hilt-android-compiler:${Versions.Hilt}"
        }
    }
    const val Glide = "com.github.bumptech.glide:glide:${Versions.Glide}"
    object Retrofit {
        const val Retrofit = "com.squareup.retrofit2:retrofit:${Versions.Retrofit}"
        const val GSonConverter = "com.squareup.retrofit2:converter-gson:${Versions.Retrofit}"
    }
    const val LoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.LoggingInterceptor}"
    const val Timber = "com.jakewharton.timber:timber:${Versions.Timber}"
    const val MockK = "io.mockk:mockk:${Versions.MockK}"
}