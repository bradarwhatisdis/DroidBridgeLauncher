plugins {
    id("com.android.application")
}

android {
    namespace = "ca.dnamobile.javalauncher"
    compileSdk = 35

    defaultConfig {
        applicationId = "ca.dnamobile.javalauncher"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        debug {
            isDebuggable = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        // Avoid duplicate META-INF files from PojavLauncher dependencies
        resources {
            excludes += setOf(
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt"
            )
        }
    }
}

dependencies {
    // AndroidX
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.activity:activity:1.9.3")
    implementation("androidx.documentfile:documentfile:1.0.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Material Design 3
    implementation("com.google.android.material:material:1.12.0")

    // ----------------------------------------------------------------
    // PojavLauncher / Boardwalk — REQUIRED for compilation.
    // These provide:
    //   net.kdt.pojavlaunch.*        (Tools, Logger, Architecture, etc.)
    //   com.oracle.dalvik.VMLauncher (Boardwalk VM launcher)
    //
    // Option A — local AAR files (place in app/libs/):
    //   implementation(fileTree("libs") { include("*.aar") })
    //
    // Option B — published Maven artifacts:
    //   implementation("net.kdt.pojavlaunch:pojavlaunch:VERSION")
    //   implementation("com.oracle.dalvik:boardwalk:VERSION")
    //
    // Option C — local module dependency:
    //   implementation(project(":pojavlaunch"))
    // ----------------------------------------------------------------
    // implementation(fileTree("libs") { include("*.aar") })
}
