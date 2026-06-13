plugins {
    id("com.android.library")
}

android {
    namespace = "net.kdt.pojavlaunch"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        // Consumer apps (DroidBridge) may target a higher SDK
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // Exclude native NDK build — we only need the Java sources
    externalNativeBuild {
        ndkBuild {
            path = null
        }
    }

    // Suppress lint errors from PojavLauncher source
    lint {
        abortOnError = false
    }

    sourceSets {
        getByName("main") {
            java {
                setSrcDirs(listOf("$rootDir/libraries/PojavLauncher/app_pojavlauncher/src/main/java"))
            }
            res.srcDirs("$rootDir/libraries/PojavLauncher/app_pojavlauncher/src/main/res")
            assets.srcDirs("$rootDir/libraries/PojavLauncher/app_pojavlauncher/src/main/assets")
        }
    }

    // PojavLauncher uses JNI libs via packagingOptions
    packaging {
        jniLibs {
            useLegacyPackaging = true
        }
    }
}

dependencies {
    // PojavLauncher core dependencies
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("commons-codec:commons-codec:1.15")
    implementation("androidx.preference:preference:1.2.0")
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    implementation("androidx.viewpager2:viewpager2:1.1.0-beta01")
    implementation("androidx.annotation:annotation:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // PojavLauncher JitPack dependencies
    implementation("com.github.duanhong169:checkerboarddrawable:1.0.2")
    implementation("com.github.PojavLauncherTeam:portrait-sdp:ed33e89cbc")
    implementation("com.github.PojavLauncherTeam:portrait-ssp:6c02fd739b")
    implementation("com.github.Mathias-Boulay:ExtendedView:1.0.0")
    implementation("com.github.Mathias-Boulay:android_gamepad_remapper:2.0.3")
    implementation("com.github.Mathias-Boulay:virtual-joystick-android:1.14")

    // PojavLauncher misc dependencies
    implementation("org.tukaani:xz:1.8")
    implementation("net.sourceforge.htmlcleaner:htmlcleaner:2.6.1")
    implementation("com.bytedance:bytehook:1.0.9")

    // Missing PojavLauncher compile dependencies
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("commons-io:commons-io:2.11.0")
    implementation("net.objecthunter:exp4j:0.4.8")
}

// Exclude dalvik/annotation dir — package conflicts with java.base module in JDK 17+
// NOTE: Must be outside the android {} block so Kotlin DSL resolves exclude()
// on SourceDirectorySet, not Configuration.
afterEvaluate {
    android.sourceSets.getByName("main").java.exclude("**/dalvik/**")
}
