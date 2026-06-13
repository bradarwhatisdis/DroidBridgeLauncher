/*
 * Copyright (c) 2026 DNA Mobile Applications.
 * All rights reserved.
 */

package ca.dnamobile.javalauncher.launcher;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Per-instance launch settings stored in SharedPreferences.
 * Supports renderer override, runtime override, custom JVM args, and RAM override.
 */
public final class InstanceLaunchSettings {
    private static final String PREFS_NAME = "per_instance_launch_settings_v2";
    private static final String KEY_RENDERER_IDENTIFIER = "renderer";
    private static final String KEY_RUNTIME_NAME = "runtime";
    private static final String KEY_CUSTOM_JVM_ARGS = "jvm_args";
    private static final String KEY_RAM_MB = "ram_mb";

    public static final String RENDERER_DEFAULT = "";
    public static final int RAM_DEFAULT = -1;

    private static final String RUNTIME_PREFIX_DEFAULT = "auto";
    private static final String RUNTIME_PREFIX_INTERNAL_8 = "Internal-8";
    private static final String RUNTIME_PREFIX_INTERNAL_17 = "Internal-17";
    private static final String RUNTIME_PREFIX_INTERNAL_21 = "Internal-21";

    private InstanceLaunchSettings() {
    }

    @NonNull
    public static Settings load(@NonNull Context context, @NonNull String instanceKey) {
        SharedPreferences prefs = getPrefs(context);
        String prefixedKey = prefixed(instanceKey);
        return new Settings(
                prefs.getString(prefixedKey + "_" + KEY_RENDERER_IDENTIFIER, RENDERER_DEFAULT),
                prefs.getString(prefixedKey + "_" + KEY_RUNTIME_NAME, RUNTIME_PREFIX_DEFAULT),
                prefs.getString(prefixedKey + "_" + KEY_CUSTOM_JVM_ARGS, null),
                prefs.getInt(prefixedKey + "_" + KEY_RAM_MB, RAM_DEFAULT)
        );
    }

    public static void save(@NonNull Context context, @NonNull String instanceKey, @NonNull Settings settings) {
        SharedPreferences prefs = getPrefs(context);
        String prefixedKey = prefixed(instanceKey);
        prefs.edit()
                .putString(prefixedKey + "_" + KEY_RENDERER_IDENTIFIER, settings.rendererIdentifier != null ? settings.rendererIdentifier : RENDERER_DEFAULT)
                .putString(prefixedKey + "_" + KEY_RUNTIME_NAME, settings.runtimeName != null ? settings.runtimeName : RUNTIME_PREFIX_DEFAULT)
                .putString(prefixedKey + "_" + KEY_CUSTOM_JVM_ARGS, settings.customJvmArgs)
                .putInt(prefixedKey + "_" + KEY_RAM_MB, settings.hasRamOverride() ? settings.ramMb : RAM_DEFAULT)
                .apply();
    }

    public static void clear(@NonNull Context context, @NonNull String instanceKey) {
        SharedPreferences prefs = getPrefs(context);
        String prefixedKey = prefixed(instanceKey);
        prefs.edit()
                .remove(prefixedKey + "_" + KEY_RENDERER_IDENTIFIER)
                .remove(prefixedKey + "_" + KEY_RUNTIME_NAME)
                .remove(prefixedKey + "_" + KEY_CUSTOM_JVM_ARGS)
                .remove(prefixedKey + "_" + KEY_RAM_MB)
                .apply();
    }

    @NonNull
    public static String[] getRuntimeDisplayLabels() {
        return new String[]{
                "Auto (recommended)",
                "Java 8 (Internal-8)",
                "Java 17 (Internal-17)",
                "Java 21 (Internal-21)"
        };
    }

    /**
     * Convert a runtime display label index to a runtime name.
     */
    @NonNull
    public static String runtimeNameForIndex(int index) {
        switch (index) {
            case 0: return RUNTIME_PREFIX_DEFAULT;
            case 1: return RUNTIME_PREFIX_INTERNAL_8;
            case 2: return RUNTIME_PREFIX_INTERNAL_17;
            case 3: return RUNTIME_PREFIX_INTERNAL_21;
            default: return RUNTIME_PREFIX_DEFAULT;
        }
    }

    /**
     * Convert a runtime name to a display label index.
     */
    public static int runtimeIndexForName(@Nullable String runtimeName) {
        if (runtimeName == null) return 0;
        switch (runtimeName) {
            case RUNTIME_PREFIX_INTERNAL_8: return 1;
            case RUNTIME_PREFIX_INTERNAL_17: return 2;
            case RUNTIME_PREFIX_INTERNAL_21: return 3;
            default: return 0;
        }
    }

    /**
     * Resolve an instance key, falling back to the raw value if empty.
     */
    @NonNull
    public static String resolveInstanceKey(@Nullable String raw, @NonNull String fallback) {
        if (raw == null || raw.trim().isEmpty()) return fallback;
        return raw.trim().toLowerCase(java.util.Locale.US).replaceAll("[^a-z0-9_-]", "_");
    }

    @NonNull
    private static SharedPreferences getPrefs(@NonNull Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @NonNull
    private static String prefixed(@NonNull String key) {
        return "inst_" + resolveInstanceKey(key, "default");
    }

    // ---- Settings inner class ----

    public static final class Settings {
        @Nullable
        public String rendererIdentifier;
        @Nullable
        public String runtimeName;
        @Nullable
        public String customJvmArgs;
        public int ramMb;

        public Settings() {
            this.rendererIdentifier = RENDERER_DEFAULT;
            this.runtimeName = RUNTIME_PREFIX_DEFAULT;
            this.customJvmArgs = null;
            this.ramMb = RAM_DEFAULT;
        }

        public Settings(
                @Nullable String rendererIdentifier,
                @Nullable String runtimeName,
                @Nullable String customJvmArgs,
                int ramMb
        ) {
            this.rendererIdentifier = rendererIdentifier;
            this.runtimeName = runtimeName;
            this.customJvmArgs = customJvmArgs;
            this.ramMb = ramMb;
        }

        public boolean hasRamOverride() {
            return ramMb > 0;
        }
    }
}
