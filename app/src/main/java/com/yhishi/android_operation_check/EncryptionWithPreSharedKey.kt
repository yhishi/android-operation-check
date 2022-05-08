package com.yhishi.android_operation_check

import android.util.Base64
import android.util.Log
import java.lang.Exception
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Singleton


/**
 * 共通鍵暗号方式を使った暗号化・復号化
 */
@Singleton
class EncryptionWithPreSharedKey {
    private var mIV: ByteArray? = null

    fun encrypt(
        value: String,
        key: String,
    ): String {
        var encrypted: ByteArray? = null
        val cipher = Cipher.getInstance(TRANSFORMATION)

        val secretKey = generateKey(key.toByteArray(Charsets.UTF_8))
        if (secretKey != null) {
            try {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey)
                mIV = cipher.iv
                encrypted = cipher.doFinal(value.toByteArray(Charsets.UTF_8))
            } catch (e: Exception) {
                Log.d("EncryptionWithPreSharedKey encrypt", "$e")
            }
        }

        return Base64.encode(encrypted, Base64.NO_WRAP).toString(Charsets.UTF_8)
    }

    fun decrypt(
        value: String,
        key: String,
    ): String {
        var decrypted: ByteArray? = null

        try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            val secretKey = generateKey(key.toByteArray(Charsets.UTF_8))
            if (secretKey != null) {
                val ivParameterSpec = IvParameterSpec(mIV)
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec)
                decrypted = cipher.doFinal(value.toByteArray(Charsets.UTF_8))
            }
        } catch (e: Exception) {
            Log.d("EncryptionWithPreSharedKey decrypt", "$e")
        }
        return decrypted?.toString(Charsets.UTF_8) ?: ""
    }

    private fun generateKey(key: ByteArray): SecretKey? {
        var secretKey: SecretKey? = null
        try {
            secretKey = SecretKeySpec(key, KEY_ALGORITHM)
        } catch (e: IllegalArgumentException) {
            Log.d("EncryptionWithPreSharedKey generateKey", "$e")
        }
        return secretKey
    }

    companion object {
        private const val TRANSFORMATION = "AES/CBC/PKCS7Padding"
        private const val KEY_ALGORITHM = "AES"
    }
}
