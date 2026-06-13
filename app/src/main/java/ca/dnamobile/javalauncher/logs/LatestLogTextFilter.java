/*
 * Copyright (c) 2026 DNA Mobile Applications.
 * All rights reserved.
 */

package ca.dnamobile.javalauncher.logs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Real-time log line filter used by the Logger class.
 * Strips ANSI codes, control characters, and known noise patterns
 * from raw pojavexec log output before dispatching to listeners.
 */
public final class LatestLogTextFilter {

    private LatestLogTextFilter() {
    }

    /**
     * Clean a raw log line for display. Returns null if the line should be
     * suppressed entirely, or the cleaned line otherwise.
     */
    @Nullable
    public static String cleanRealtimeLine(@Nullable String rawLine) {
        if (rawLine == null || rawLine.trim().isEmpty()) return null;

        String cleaned = rawLine;

        // Remove ANSI escape sequences
        cleaned = cleaned.replaceAll("\\u001B\\[[;\\d]*[ -/]*[@-~]", "");

        // Remove carriage returns
        cleaned = cleaned.replace("\r", "");

        // Remove non-printable control characters except tabs and newlines
        cleaned = cleaned.replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F]", "");

        cleaned = cleaned.trim();
        if (cleaned.isEmpty()) return null;

        return cleaned;
    }
}
