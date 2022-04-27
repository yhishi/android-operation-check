package com.yhishi.android_operation_check

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val normalPreferencesValue = viewModel.getTestSettingFromNormalPreferences()
        Toast.makeText(this, "normalPreferencesValue = $normalPreferencesValue", Toast.LENGTH_LONG).show()

        val encryptedPreferencesValue = viewModel.getTestSettingFromEncryptedNormalPreferences()
        Toast.makeText(this, "encryptedPreferencesValue = $encryptedPreferencesValue", Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val ENCRYPTED_FILE_NAME = "encrypted-preferences"
    }
}
