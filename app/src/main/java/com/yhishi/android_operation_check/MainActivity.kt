package com.yhishi.android_operation_check

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // SharedPreferences
        val normalPreferences = getSharedPreferences(NORMAL_FILE_NAME, Context.MODE_PRIVATE)

        normalPreferences
            .edit()
            .putString("normal-preferences-key", "normal-preferences-value")
            .apply()

        val normalPreferencesValue = normalPreferences.getString("normal-preferences-key", "Nothing")
        Toast.makeText(this, "normalPreferencesValue = $normalPreferencesValue", Toast.LENGTH_LONG).show()

        // EncryptedSharedPreferences
        val mainKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val encryptedPreferences = EncryptedSharedPreferences.create(
            ENCRYPTED_FILE_NAME,
            mainKeyAlias,
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        encryptedPreferences
            .edit()
            .putString("encrypted-preferences-key", "encrypted-preferences-value")
            .apply()

        val encryptedPreferencesValue = encryptedPreferences.getString("encrypted-preferences-key", "Nothing")
        Toast.makeText(this, "encryptedPreferencesValue = $encryptedPreferencesValue", Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val NORMAL_FILE_NAME = "normal-preferences"
        private const val ENCRYPTED_FILE_NAME = "encrypted-preferences"
    }
}