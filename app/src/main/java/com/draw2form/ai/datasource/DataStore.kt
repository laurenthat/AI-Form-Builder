package com.draw2form.ai.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

/**
 * key value database
 * Currently used for userId and access token of logged in user.
 */
class DataStore(private val context: Context) {


    /**
     * Getter for logged in user id
     */

    val getUserId: Flow<UUID?> = context.dataStore.data
        .map { it[USERID_KEY] }
        .map { it?.let { UUID.fromString(it) } }

    /**
     * Getter for checking if welcome screen is seen by user
     */
    val getWelcomeScreenSeen: Flow<Boolean> = context.dataStore.data
        .map { it[WELCOMESCREENSEEN_KEY]?.equals("true") ?: false }


    /**
     * Getter for access token
     */
    val getAccessToken: Flow<String?> = context.dataStore.data
        .map { it[ACCESSTOKEN_KEY] }

    /**
     * Getter for access token but with prepended "Bearer $token"
     */
    val getAuthorizationHeaderValue: Flow<String?> = getAccessToken.map {
        if (it != null) "Bearer $it" else null
    }

    /**
     * Getter for access token expires at
     */
    val getAccessTokenExpiresAt: Flow<String?> = context.dataStore.data
        .map { it[ACCESSTOKEN_EXPIRES_KEY] }

    /**
     * Setter for logged in user id
     */
    private suspend fun setUserId(value: UUID) {
        context.dataStore.edit { it[USERID_KEY] = value.toString() }
    }

    /**
     * Setter for checking if welcome screen is seen by user
     */
    suspend fun setWelcomeScreenSeen() {
        context.dataStore.edit { it[WELCOMESCREENSEEN_KEY] = true.toString() }
    }

    /**
     * Setter for access token string
     */
    private suspend fun setAccessToken(value: String) {
        context.dataStore.edit { it[ACCESSTOKEN_KEY] = value }
    }

    /**
     * Setter for access token string
     */
    private suspend fun setAccessTokenExpiresAt(value: String) {
        context.dataStore.edit { it[ACCESSTOKEN_EXPIRES_KEY] = value }
    }

    /**
     * Setter for login data when logging in
     */
    suspend fun saveLoginData(accessToken: String, expiresAt: String, userId: UUID) {
        setAccessToken(accessToken)
        setAccessTokenExpiresAt(expiresAt)
        setUserId(userId)
    }

    /**
     * Setter for login data when logging out
     */
    suspend fun deleteLoginData() {
        context.dataStore.edit {
            it.remove(USERID_KEY)
            it.remove(ACCESSTOKEN_KEY)
            it.remove(ACCESSTOKEN_EXPIRES_KEY)
        }
    }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userId")
        val USERID_KEY = stringPreferencesKey("user_id")
        val ACCESSTOKEN_KEY = stringPreferencesKey("access_token")
        val ACCESSTOKEN_EXPIRES_KEY = stringPreferencesKey("access_token_expires")
        val WELCOMESCREENSEEN_KEY = stringPreferencesKey("welcome_screen_seen")
    }
}
