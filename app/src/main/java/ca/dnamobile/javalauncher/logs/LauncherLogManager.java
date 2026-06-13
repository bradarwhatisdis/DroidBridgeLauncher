/*
 * Copyright (c) 2026 DNA Mobile Applications.
 * All rights reserved.
 */

package ca.dnamobile.javalauncher.logs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

import java.io.File;

import ca.dnamobile.javalauncher.feature.log.Logging;
import ca.dnamobile.javalauncher.utils.path.PathManager;

/**
 * Manages launcher log file history and sharing.
 */
public final class LauncherLogManager {
    private static final String TAG = "LauncherLogManager";
    private static final String PREFS_NAME = "launcher_log_prefs";
    private static final String KEEP_LOG_HISTORY_KEY = "keep_log_history";

    private static final String LATEST_LOG_NAME = "latestlog.txt";

    private LauncherLogManager() {
    }

    @NonNull
    public static File getLatestLogFile(@NonNull Context context) {
        File dir = new File(context.getFilesDir(), "logs");
        if (!dir.isDirectory()) dir.mkdirs();
        return new File(dir, LATEST_LOG_NAME);
    }

    public static boolean isKeepLogHistoryEnabled(@NonNull Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getBoolean(KEEP_LOG_HISTORY_KEY, false);
    }

    public static void setKeepLogHistoryEnabled(@NonNull Context context, boolean enabled) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(KEEP_LOG_HISTORY_KEY, enabled)
                .apply();
    }

    /**
     * Share the latest log file via Android's standard share sheet.
     */
    public static void shareLatestLog(@NonNull Context context) {
        File logFile = getLatestLogFile(context);
        if (!logFile.isFile()) {
            Logging.w(TAG, "No latestlog.txt to share");
            return;
        }

        try {
            Uri uri = androidx.core.content.FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".fileprovider",
                    logFile
            );

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(Intent.createChooser(shareIntent, "Share latest log"));
        } catch (Throwable throwable) {
            Logging.e(TAG, "Failed to share latest log", throwable);
        }
    }
}
