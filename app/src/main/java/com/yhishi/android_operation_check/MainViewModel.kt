package com.yhishi.android_operation_check

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingRepository: SettingRepository,
) : ViewModel() {
    private val _normalPreferencesValue = MutableStateFlow("")
    val normalPreferencesValue: StateFlow<String>
        get() = _normalPreferencesValue

    private val _encryptedPreferencesValue = MutableStateFlow("")
    val encryptedPreferencesValue: StateFlow<String>
        get() = _encryptedPreferencesValue

    init {
        viewModelScope.launch {
            settingRepository.saveTestSettingToNormalPreferences()
            settingRepository.saveTestSettingToEncryptedPreferences()

            val normalValue = settingRepository.getTestSettingFromNormalPreferences()
            _normalPreferencesValue.value = normalValue

            val encryptedValue = settingRepository.getTestSettingFromEncryptedNormalPreferences()
            _encryptedPreferencesValue.value = encryptedValue
        }
    }
}
