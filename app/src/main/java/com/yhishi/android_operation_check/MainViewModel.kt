package com.yhishi.android_operation_check

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.security.MessageDigest
import javax.inject.Inject
import kotlin.text.Charsets.UTF_8


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

    private val _preferencesDataStoreValue = MutableStateFlow("")
    val preferencesDataStoreValue: StateFlow<String>
        get() = _preferencesDataStoreValue

    init {
        viewModelScope.launch {
            settingRepository.saveTestSettingToNormalPreferences()
            settingRepository.saveTestSettingToEncryptedPreferences()
            settingRepository.saveTestSettingToPreferencesDataStore()

            val normalValue = settingRepository.getTestSettingFromNormalPreferences()
            _normalPreferencesValue.value = normalValue

            val encryptedValue = settingRepository.getTestSettingFromEncryptedNormalPreferences()
            _encryptedPreferencesValue.value = encryptedValue

            settingRepository.getTestSettingFromPreferencesDataStore()
                .collect {
                    _preferencesDataStoreValue.value = it
                }
        }
    }

    fun onClickHashButton(input: String) {
        val toMd5 = calculateHash(input = input, algorithm = ALGORITHM_MD5)
        val toSha = calculateHash(input = input, algorithm = ALGORITHM_SHA_256)
    }

    private fun calculateHash(input: String, algorithm: String): String {
        // MD5ハッシュ計算
        val byteArray = MessageDigest.getInstance(algorithm).digest(input.toByteArray(UTF_8))

        // バイト配列を16進数の文字列に変換
        return byteArray.joinToString(separator = "") { byte -> "%02x".format(byte) }
    }

    companion object {
        private const val ALGORITHM_MD5 = "MD5"
        private const val ALGORITHM_SHA_256 = "SHA-256"
    }
}
