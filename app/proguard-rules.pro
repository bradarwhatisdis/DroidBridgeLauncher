# DroidBridge Launcher ProGuard rules
# Add project-specific ProGuard rules here.

# Keep PojavLauncher / Boardwalk classes
-keep class net.kdt.pojavlaunch.** { *; }
-keep class com.oracle.dalvik.** { *; }

# Keep DroidBridge agent classes
-keep class ca.dnamobile.javalauncher.agent.** { *; }
