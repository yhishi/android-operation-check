package com.yhishi.android_operation_check

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingRepository @Inject constructor(
    @ApplicationContext context: Context
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

    companion object {
        private const val NORMAL_FILE_NAME = "normal-preferences"
        private const val ENCRYPTED_FILE_NAME = "encrypted-preferences"
    }
}
