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
        maven("https://jitpack.io")
    }
}

rootProject.name = "DroidBridgeLauncher"

include(":app")
include(":methods_injector_agent")
include(":libraries:pojavlaunch_shared")
