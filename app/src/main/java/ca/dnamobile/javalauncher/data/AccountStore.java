/*
 * Copyright (c) 2026 DNA Mobile Applications.
 * All rights reserved.
 *
 * This file is DroidBridge project code.
 * It is not part of Minecraft and does not grant rights to Minecraft,
 * Mojang, Microsoft, PojavLauncher, Zalith Launcher, or any third-party project.
 *
 * Files written entirely by DNA Mobile Applications are proprietary unless
 * a file header or separate license notice states otherwise.
 */

package ca.dnamobile.javalauncher.data;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

import ca.dnamobile.javalauncher.feature.log.Logging;

/**
 * Offline-first account store.
 *
 * <p>Stores accounts in a JSON file under the app's private directory.
 * Microsoft-related methods return default values so the existing UI
 * code works without modification.
 */
public final class AccountStore {
    private static final String TAG = "AccountStore";
    private static final String ACCOUNTS_FILE = "droidbridge_accounts.json";
    private static final String ACTIVE_ACCOUNT_ID_KEY = "active_account_id";
    private static final String MICROSOFT_AUTH_COMPLETED_KEY = "microsoft_auth_completed";
    private static final String STORED_MICROSOFT_ACCOUNT_KEY = "stored_microsoft_account";

    private final File accountsFile;
    private final Context context;

    public AccountStore(@NonNull Context context) {
        this.context = context.getApplicationContext();
        this.accountsFile = new File(this.context.getFilesDir(), ACCOUNTS_FILE);
    }

    // ---- Public API ----

    /** Load the currently active account, or null if none is active. */
    @Nullable
    public Account load() {
        try {
            JSONObject root = readAccountsJson();
            if (root == null) return null;
            String activeId = root.optString(ACTIVE_ACCOUNT_ID_KEY, null);
            if (activeId == null || activeId.isEmpty()) {
                // Fall back to the first offline account if nothing active
                JSONArray accounts = root.optJSONArray("accounts");
                if (accounts != null && accounts.length() > 0) {
                    JSONObject first = accounts.optJSONObject(0);
                    if (first != null) return Account.fromJson(first);
                }
                return null;
            }
            JSONArray accounts = root.optJSONArray("accounts");
            if (accounts == null) return null;
            for (int i = 0; i < accounts.length(); i++) {
                JSONObject acct = accounts.optJSONObject(i);
                if (acct != null && activeId.equals(acct.optString("accountId", null))) {
                    return Account.fromJson(acct);
                }
            }
            return null;
        } catch (Throwable throwable) {
            Logging.e(TAG, "Failed to load active account", throwable);
            return null;
        }
    }

    /** Whether the user has ever completed Microsoft login (always true for offline mode). */
    public boolean hasMicrosoftLoginCompletedOnce() {
        // Always true so offline accounts are always unlocked
        return true;
    }

    /** Whether a previously-signed-in Microsoft account is stored locally. */
    public boolean hasStoredMicrosoftAccount() {
        try {
            JSONObject root = readAccountsJson();
            if (root == null) return false;
            String stored = root.optString(STORED_MICROSOFT_ACCOUNT_KEY, null);
            return stored != null && !stored.isEmpty();
        } catch (Throwable e) {
            return false;
        }
    }

    /** List all offline accounts. */
    @NonNull
    public ArrayList<Account> listOfflineAccounts() {
        ArrayList<Account> result = new ArrayList<>();
        try {
            JSONObject root = readAccountsJson();
            if (root == null) return result;
            JSONArray accounts = root.optJSONArray("accounts");
            if (accounts == null) return result;
            for (int i = 0; i < accounts.length(); i++) {
                JSONObject acct = accounts.optJSONObject(i);
                if (acct != null && "offline".equals(acct.optString("type"))) {
                    result.add(Account.fromJson(acct));
                }
            }
        } catch (Throwable throwable) {
            Logging.e(TAG, "Failed to list offline accounts", throwable);
        }
        return result;
    }

    /**
     * Save or update an offline account.
     *
     * @param existingId       null to create new, or the accountId of an existing account to update.
     * @param name             the display name (3-16 alphanumeric).
     * @param pendingSkinUri   content URI of a PNG skin file, or null.
     * @param clearSkin        true to clear any existing skin.
     * @return the saved/updated account.
     */
    @NonNull
    public Account saveOrUpdateOfflineAccount(
            @Nullable String existingId,
            @NonNull String name,
            @Nullable Uri pendingSkinUri,
            boolean clearSkin
    ) {
        try {
            JSONObject root = readAccountsJsonOrCreate();
            JSONArray accounts = root.optJSONArray("accounts");
            if (accounts == null) {
                accounts = new JSONArray();
                root.put("accounts", accounts);
            }

            String accountId = existingId;
            JSONObject target = null;

            if (accountId != null) {
                for (int i = 0; i < accounts.length(); i++) {
                    JSONObject acct = accounts.optJSONObject(i);
                    if (acct != null && accountId.equals(acct.optString("accountId"))) {
                        target = acct;
                        break;
                    }
                }
            }

            if (target == null) {
                accountId = UUID.randomUUID().toString();
                target = new JSONObject();
                target.put("accountId", accountId);
                target.put("type", "offline");
                accounts.put(target);
            }

            target.put("displayName", sanitizeName(name));
            target.put("lastUsed", System.currentTimeMillis());

            if (clearSkin) {
                target.put("offlineSkinPath", JSONObject.NULL);
                target.put("offlineSkinModel", JSONObject.NULL);
            } else if (pendingSkinUri != null) {
                String savedPath = copySkinToPrivateStorage(pendingSkinUri, accountId);
                target.put("offlineSkinPath", savedPath);
                target.put("offlineSkinModel", detectSkinModel(pendingSkinUri));
            }

            // Activate this account
            root.put(ACTIVE_ACCOUNT_ID_KEY, accountId);
            writeAccountsJson(root);

            return Account.fromJson(target);
        } catch (Throwable throwable) {
            Logging.e(TAG, "Failed to save/update offline account", throwable);
            // Return a minimal account
            return new Account(
                    existingId != null ? existingId : UUID.randomUUID().toString(),
                    "offline", sanitizeName(name), null, null,
                    System.currentTimeMillis(), null, false
            );
        }
    }

    /** Delete an offline account by ID. */
    public void deleteOfflineAccount(@NonNull String accountId) {
        try {
            JSONObject root = readAccountsJson();
            if (root == null) return;
            JSONArray accounts = root.optJSONArray("accounts");
            if (accounts == null) return;

            JSONArray updated = new JSONArray();
            boolean found = false;
            for (int i = 0; i < accounts.length(); i++) {
                JSONObject acct = accounts.optJSONObject(i);
                if (acct != null && accountId.equals(acct.optString("accountId"))) {
                    found = true;
                    // Remove the stored skin file
                    String skinPath = acct.optString("offlineSkinPath", null);
                    if (skinPath != null && !skinPath.isEmpty() && !"null".equals(skinPath)) {
                        File skinFile = new File(skinPath);
                        if (skinFile.isFile()) skinFile.delete();
                    }
                    continue;
                }
                updated.put(acct);
            }

            root.put("accounts", updated);

            // If the deleted account was active, clear active
            String activeId = root.optString(ACTIVE_ACCOUNT_ID_KEY, null);
            if (accountId.equals(activeId)) {
                root.remove(ACTIVE_ACCOUNT_ID_KEY);
            }

            writeAccountsJson(root);
        } catch (Throwable throwable) {
            Logging.e(TAG, "Failed to delete offline account", throwable);
        }
    }

    /** Activate an offline account by ID. */
    public void activateOfflineAccount(@NonNull String accountId) {
        try {
            JSONObject root = readAccountsJson();
            if (root == null) return;
            root.put(ACTIVE_ACCOUNT_ID_KEY, accountId);
            writeAccountsJson(root);
        } catch (Throwable throwable) {
            Logging.e(TAG, "Failed to activate offline account", throwable);
        }
    }

    /** Switch to the last remembered Microsoft account (no-op in offline mode). */
    public void useLastMicrosoftAccount() {
        // In offline mode, this is a no-op
    }

    /** Load the last Microsoft account that was signed in, if any. */
    @Nullable
    public Account loadLastMicrosoftAccount() {
        try {
            JSONObject root = readAccountsJson();
            if (root == null) return null;
            String stored = root.optString(STORED_MICROSOFT_ACCOUNT_KEY, null);
            if (stored == null || stored.isEmpty()) return null;

            // Check if there's a stored Microsoft account entry in the accounts array
            JSONArray accounts = root.optJSONArray("accounts");
            if (accounts == null) return null;
            for (int i = 0; i < accounts.length(); i++) {
                JSONObject acct = accounts.optJSONObject(i);
                if (acct != null && "microsoft".equals(acct.optString("type"))
                        && stored.equals(acct.optString("accountId"))) {
                    return Account.fromJson(acct);
                }
            }

            // Return a synthetic account if we just have an ID stored
            return new Account(stored, "microsoft", "Microsoft Player",
                    null, null, 0, null, false);
        } catch (Throwable throwable) {
            Logging.e(TAG, "Failed to load last Microsoft account", throwable);
            return null;
        }
    }

    // ---- JSON I/O ----

    @Nullable
    private JSONObject readAccountsJson() {
        if (!accountsFile.isFile()) return null;
        try (FileInputStream in = new FileInputStream(accountsFile)) {
            byte[] data = new byte[(int) Math.min(accountsFile.length(), 65536)];
            int read = in.read(data);
            if (read <= 0) return null;
            String json = new String(data, 0, read, StandardCharsets.UTF_8).trim();
            if (json.isEmpty()) return null;
            return new JSONObject(json);
        } catch (Throwable throwable) {
            Logging.e(TAG, "Failed to read accounts JSON", throwable);
            return null;
        }
    }

    @NonNull
    private JSONObject readAccountsJsonOrCreate() {
        JSONObject root = readAccountsJson();
        return root != null ? root : new JSONObject();
    }

    private void writeAccountsJson(@NonNull JSONObject root) {
        try (FileOutputStream out = new FileOutputStream(accountsFile);
             OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
            writer.write(root.toString(2));
            writer.flush();
        } catch (Throwable throwable) {
            Logging.e(TAG, "Failed to write accounts JSON", throwable);
        }
    }

    // ---- Skin helpers ----

    @Nullable
    private String copySkinToPrivateStorage(@NonNull Uri sourceUri, @NonNull String accountId) {
        try {
            File skinsDir = new File(context.getFilesDir(), "skins");
            if (!skinsDir.isDirectory()) skinsDir.mkdirs();
            File target = new File(skinsDir, "skin_" + accountId + ".png");
            try (InputStream input = context.getContentResolver().openInputStream(sourceUri);
                 FileOutputStream output = new FileOutputStream(target)) {
                if (input == null) return null;
                byte[] buffer = new byte[8192];
                int read;
                while ((read = input.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
            }
            return target.getAbsolutePath();
        } catch (Throwable throwable) {
            Logging.e(TAG, "Failed to copy skin to private storage", throwable);
            return null;
        }
    }

    @NonNull
    private String detectSkinModel(@NonNull Uri skinUri) {
        // Default to classic - could implement proper detection via pixel checking
        return "classic";
    }

    @NonNull
    private static String sanitizeName(@NonNull String raw) {
        String cleaned = raw.trim().replaceAll("[^A-Za-z0-9_]", "");
        if (cleaned.length() > 16) cleaned = cleaned.substring(0, 16);
        return cleaned.isEmpty() ? "Player" : cleaned;
    }

    // ---- Account inner class ----

    public static final class Account {
        @NonNull
        public final String accountId;
        @NonNull
        public final String type;
        @Nullable
        public final String displayName;
        @Nullable
        public final String offlineSkinPath;
        @Nullable
        public final String offlineSkinModel;
        public final long lastUsed;
        @Nullable
        public final String skinUrl;
        @Nullable
        public final String minecraftAccessToken;

        // Package-private constructor used by JSON deserialization.
        Account(
                @NonNull String accountId,
                @NonNull String type,
                @Nullable String displayName,
                @Nullable String offlineSkinPath,
                @Nullable String offlineSkinModel,
                long lastUsed,
                @Nullable String skinUrl,
                boolean hasMinecraftSession
        ) {
            this.accountId = accountId;
            this.type = type;
            this.displayName = displayName;
            this.offlineSkinPath = offlineSkinPath;
            this.offlineSkinModel = offlineSkinModel;
            this.lastUsed = lastUsed;
            this.skinUrl = skinUrl;
            // For offline accounts we always return a token so skin upload works
            this.minecraftAccessToken = hasMinecraftSession ? "offline-session-token" : null;
        }

        public boolean isOfflineAccount() {
            return "offline".equals(type);
        }

        public boolean isMicrosoftAccount() {
            return "microsoft".equals(type);
        }

        public boolean hasOfflineSkin() {
            return offlineSkinPath != null && !offlineSkinPath.isEmpty()
                    && !"null".equals(offlineSkinPath);
        }

        public boolean hasMinecraftSession() {
            return "microsoft".equals(type) || minecraftAccessToken != null;
        }

        @NonNull
        public String getBestDisplayName() {
            if (displayName != null && !displayName.trim().isEmpty()) {
                return displayName.trim();
            }
            if ("microsoft".equals(type)) return "Microsoft Player";
            return "Player";
        }

        @NonNull
        static Account fromJson(@NonNull JSONObject obj) {
            return new Account(
                    obj.optString("accountId", UUID.randomUUID().toString()),
                    obj.optString("type", "offline"),
                    obj.optString("displayName", null),
                    obj.optString("offlineSkinPath", null),
                    obj.optString("offlineSkinModel", null),
                    obj.optLong("lastUsed", 0),
                    obj.optString("skinUrl", null),
                    obj.optBoolean("hasMinecraftSession", false)
            );
        }
    }
}
