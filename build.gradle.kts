buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Dependencies.Gradle)
        classpath(Dependencies.Kotlin)
        classpath(Dependencies.DaggerHilt)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    }
}

allprojects {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>() {
        kotlinOptions {
            freeCompilerArgs = listOf(
                "-Xopt-in=kotlinx.coroutines.FlowPreview",
                "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            )
            jvmTarget = "1.8"
        }
    }
}


tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}