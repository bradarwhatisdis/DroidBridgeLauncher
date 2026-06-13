/*
 * Copyright (c) 2026 DNA Mobile Applications.
 * All rights reserved.
 */

package ca.dnamobile.javalauncher.launcher;

import androidx.annotation.NonNull;

import java.io.File;

/**
 * Runtime compatibility utilities.
 * Used by UnpackJreTask to validate installed Java runtimes.
 */
public final class RuntimeCompat {
    public static final String PATCH_ID = "droidbridge-offline-2026";

    private RuntimeCompat() {
    }

    /**
     * Determine the Java major version from a runtime name.
     * E.g., "Internal-8" -> 8, "Internal-17" -> 17.
     */
    public static int javaMajorForRuntimeName(@NonNull String jreName) {
        if (jreName == null) return 8;
        String cleaned = jreName.replaceAll("[^0-9]", "");
        if (cleaned.isEmpty()) return 8;
        try {
            return Integer.parseInt(cleaned);
        } catch (NumberFormatException e) {
            return 8;
        }
    }

    /**
     * Check if a runtime is installed and usable for the given Java version.
     */
    public static boolean isRuntimeInstalledForJava(
            @NonNull String jreName,
            @NonNull File runtimeHome,
            int javaMajor
    ) {
        if (!runtimeHome.isDirectory()) return false;

        // Basic check: verify libjvm.so exists
        File libDir = new File(runtimeHome, "lib");
        if (!libDir.isDirectory()) return false;

        File serverDir = new File(libDir, "server");
        if (serverDir.isDirectory()) {
            File libjvm = new File(serverDir, "libjvm.so");
            if (libjvm.isFile()) return true;
        }

        // Also check alternative paths
        File jvmLib = new File(libDir, "jvm.so");
        return jvmLib.isFile();
    }

    /**
     * Describe the current state of a runtime installation.
     */
    @NonNull
    public static String describeRuntimeState(
            @NonNull String jreName,
            @NonNull File runtimeHome
    ) {
        if (!runtimeHome.isDirectory()) {
            return jreName + ": directory missing at " + runtimeHome.getAbsolutePath();
        }

        File libDir = new File(runtimeHome, "lib");
        if (!libDir.isDirectory()) {
            return jreName + ": lib directory missing";
        }

        File serverDir = new File(libDir, "server");
        File libjvm = new File(serverDir, "libjvm.so");

        if (libjvm.isFile()) {
            return jreName + ": OK (libjvm.so found, size=" + libjvm.length() + ")";
        }

        return jreName + ": incomplete (libjvm.so not found)";
    }
}
