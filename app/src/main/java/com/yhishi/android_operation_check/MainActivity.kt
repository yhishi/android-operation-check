package com.yhishi.android_operation_check

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

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
    }

    companion object {
        private const val NORMAL_FILE_NAME = "normal-preferences"
    }
}