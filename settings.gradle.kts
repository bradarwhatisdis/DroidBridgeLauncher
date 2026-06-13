pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        // PojavLauncher / other snapshot dependencies, if needed:
        // maven("https://maven.pojavlaunch.com/snapshots")
    }
}

rootProject.name = "DroidBridgeLauncher"

include(":app")
include(":methods_injector_agent")
