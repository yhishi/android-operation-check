package com.yhishi.android_operation_check

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val normalPreferences = context.getSharedPreferences(NORMAL_FILE_NAME, Context.MODE_PRIVATE)
    private val mainKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val encryptedPreferences = EncryptedSharedPreferences.create(
        ENCRYPTED_FILE_NAME,
        mainKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    suspend fun saveTestSettingToNormalPreferences() {
        withContext(Dispatchers.IO) {
            normalPreferences
                .edit()
                .putString("normal-preferences-key", "normal-preferences-value")
                .apply()
        }
    }

    suspend fun getTestSettingFromNormalPreferences(): String {
        return withContext(Dispatchers.IO) {
            normalPreferences.getString("normal-preferences-key", "Nothing") ?: ""
        }
    }

    suspend fun saveTestSettingToEncryptedPreferences() {
        withContext(Dispatchers.IO) {
            encryptedPreferences
                .edit()
                .putString("encrypted-preferences-key", "encrypted-preferences-value")
                .apply()
        }
    }

    suspend fun getTestSettingFromEncryptedNormalPreferences(): String {
        return withContext(Dispatchers.IO) {
            encryptedPreferences.getString("encrypted-preferences-key", "Nothing") ?: ""
        }
    }

    suspend fun saveTestSettingToPreferencesDataStore() {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { settings ->
                settings[PREFERENCES_DATA_STORE_KEY] = "preferences-dataStore-value"
            }
        }
    }

    suspend fun getTestSettingFromPreferencesDataStore(): Flow<String> {
        return withContext(Dispatchers.IO) {
            context.dataStore.data
                .catch { exception ->
                    // dataStore.data throws an IOException when an error is encountered when reading data
                    if (exception is IOException) {
                        Log.e("SettingRepository", "Error reading preferences.", exception)
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map { preferences ->
                    preferences[PREFERENCES_DATA_STORE_KEY] ?: ""
                }
        }
    }

    companion object {
        private const val NORMAL_FILE_NAME = "normal-preferences"
        private const val ENCRYPTED_FILE_NAME = "encrypted-preferences"
        private val PREFERENCES_DATA_STORE_KEY = stringPreferencesKey("preferences-dataStore-key")
    }
}
