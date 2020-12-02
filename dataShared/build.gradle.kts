plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("kotlin-android-extensions")
    id("kotlinx-serialization")
}

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
    maven("https://kotlin.bintray.com/kotlinx")
    maven("https://dl.bintray.com/jetbrains/kotlin-native-dependencies")
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    maven("https://plugins.gradle.org/m2/")
    maven("https://atitto.jfrog.io/artifactory/MviFlowCore")
}

kotlin {

    targets {
        android()
        iosArm64 {
            binaries {
                framework("MultiPlatformLibrary")
            }
        }
        iosX64 {
            binaries {
                framework("MultiPlatformLibrary")
            }
        }
    }

    android()

    sourceSets["commonMain"].dependencies {
        implementation("androidx.core:core-ktx:1.3.2")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
        implementation("io.ktor:ktor-client-core:1.4.2")
        implementation("io.ktor:ktor-client-json:1.4.2")
        implementation("io.ktor:ktor-client-serialization:1.4.2")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.0")
        api("com.atitto.MviFlowCore:mviKmm:0.1")
    }
    sourceSets["androidMain"].dependencies {
        implementation("io.ktor:ktor-client-android:1.4.2")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    }

    sourceSets {
        val iosX64Main by sourceSets.getting
        val iosArm64Main by sourceSets.getting
        val iosMain by sourceSets.creating {
            dependsOn(sourceSets["commonMain"])
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
        }
    }

}

android {
    compileSdkVersion(29)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(23)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
