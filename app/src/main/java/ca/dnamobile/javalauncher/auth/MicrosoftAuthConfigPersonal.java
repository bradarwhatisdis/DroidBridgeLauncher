/*
 * Copyright (c) 2026 DNA Mobile Applications.
 * All rights reserved.
 */

package ca.dnamobile.javalauncher.auth;

/**
 * Stub configuration for Microsoft authentication.
 * In offline mode, Microsoft auth is always disabled.
 */
public final class MicrosoftAuthConfigPersonal {
    private MicrosoftAuthConfigPersonal() {
    }

    /**
     * Returns false since no Microsoft client ID is configured for offline builds.
     */
    public static boolean isConfigured() {
        return false;
    }
}
