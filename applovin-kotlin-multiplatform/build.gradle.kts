import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "io.github.aditya-gupta99"
version = "1.0.0"

kotlin {
    androidTarget {
        publishLibraryVariants("release", "debug")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "applovin-koltin-multiplatform"
            isStatic = false
        }
    }



    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.lifecycle.process)
                implementation(libs.applovin.sdk)
            }
        }
    }
}

android {
    namespace = "com.aditya.gupta99.applovin"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

mavenPublishing {

    coordinates(group.toString(), "applovin-kotlin-multiplatform", version.toString())

    pom {
        name = "AppLovin Kotlin Multiplatform"
        description = "A multiplatform library for Applovin"
        inceptionYear = "2025"
        url = "https://github.com/Aditya-gupta99/AppLovin-kotlin-multiplatform"
        licenses {
            license {
                name = "MIT"
                url = "https://opensource.org/licenses/MIT"
                distribution = "ZZZ"
            }
        }
        developers {
            developer {
                id = "Aditya-gupta99"
                name = "Aditya Gupta"
                url = "https://github.com/Aditya-gupta99"
            }
        }
        scm {
            url = "https://github.com/Aditya-gupta99/AppLovin-kotlin-multiplatform"
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()
}