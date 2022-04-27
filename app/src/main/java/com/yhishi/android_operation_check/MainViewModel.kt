package com.yhishi.android_operation_check

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingRepository: SettingRepository,
) : ViewModel() {
    init {
        settingRepository.saveTestSettingToNormalPreferences()
    }

    fun getTestSettingFromNormalPreferences(): String {
        return settingRepository.getTestSettingFromNormalPreferences()
    }
}