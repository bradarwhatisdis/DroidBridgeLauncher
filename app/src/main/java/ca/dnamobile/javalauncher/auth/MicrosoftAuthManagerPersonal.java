/*
 * Copyright (c) 2026 DNA Mobile Applications.
 * All rights reserved.
 */

package ca.dnamobile.javalauncher.auth;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ca.dnamobile.javalauncher.data.AccountStore;

/**
 * Stub Microsoft authentication manager.
 * In offline mode, all Microsoft auth operations show an error or are no-ops.
 */
public final class MicrosoftAuthManagerPersonal {
    @Nullable private Listener listener;
    @NonNull private final Context context;
    @NonNull private final AccountStore accountStore;

    public MicrosoftAuthManagerPersonal(@NonNull Context context, @NonNull AccountStore accountStore) {
        this.context = context.getApplicationContext();
        this.accountStore = accountStore;
    }

    public void setListener(@Nullable Listener listener) {
        this.listener = listener;
    }

    /**
     * Attempt Microsoft sign-in. In offline mode this notifies an error.
     */
    public void signIn() {
        if (listener != null) {
            listener.onError("Microsoft sign-in is not available in offline mode.");
        }
    }

    /**
     * Sign out any stored Microsoft account.
     */
    public void signOut() {
        // No-op in offline mode
    }

    /**
     * Refresh the current Microsoft account and skin data.
     */
    public void refreshMicrosoftAccount() {
        if (listener != null) {
            listener.onError("Microsoft account refresh is not available in offline mode.");
        }
    }

    /**
     * Release any held resources.
     */
    public void dispose() {
        listener = null;
    }

    public interface Listener {
        void onSignedIn(@NonNull AccountStore.Account account);
        void onError(@NonNull String message);
    }
}
