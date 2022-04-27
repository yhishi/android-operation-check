package com.yhishi.android_operation_check

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    // SharedPreferences
    private val normalPreferences = context.getSharedPreferences(NORMAL_FILE_NAME, Context.MODE_PRIVATE)

    fun saveTestSettingToNormalPreferences() {
        normalPreferences
            .edit()
            .putString("normal-preferences-key", "normal-preferences-value")
            .apply()
    }

    fun getTestSettingFromNormalPreferences(): String {
        return normalPreferences.getString("normal-preferences-key", "Nothing") ?: ""
    }

    companion object {
        private const val NORMAL_FILE_NAME = "normal-preferences"
    }
}
