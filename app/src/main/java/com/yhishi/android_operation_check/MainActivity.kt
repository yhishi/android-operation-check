package com.yhishi.android_operation_check

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            viewModel.normalPreferencesValue
                .filter {
                    it.isNotEmpty()
                }
                .collect { normalPreferencesValue ->
                    Toast.makeText(this@MainActivity, "normalPreferencesValue = $normalPreferencesValue", Toast.LENGTH_LONG).show()
                }
        }

        lifecycleScope.launch {
            viewModel.encryptedPreferencesValue
                .filter {
                    it.isNotEmpty()
                }
                .collect { encryptedPreferencesValue ->
                    Toast.makeText(this@MainActivity, "encryptedPreferencesValue = $encryptedPreferencesValue", Toast.LENGTH_LONG).show()
                }
        }

        lifecycleScope.launch {
            viewModel.preferencesDataStoreValue
                .filter {
                    it.isNotEmpty()
                }
                .collect { preferencesDataStoreValue ->
                    Toast.makeText(this@MainActivity, "preferencesDataStoreValue = $preferencesDataStoreValue", Toast.LENGTH_LONG).show()
                }
        }
    }
}
