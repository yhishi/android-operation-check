package com.yhishi.android_operation_check

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.GeneralSecurityException
import java.security.KeyPairGenerator
import java.security.KeyStore
import javax.crypto.Cipher
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Android Keystoreを用いた公開鍵暗号方式での暗号化・復号化
 */
@Singleton
class KeyStore @Inject constructor() {
    private var keyStore: KeyStore = KeyStore.getInstance(KEY_PROVIDER)

    init {
        keyStore.load(null)
        if (!keyStore.containsAlias(kEY_STORE_ALIAS)) {
            val keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, KEY_PROVIDER)
            keyPairGenerator.initialize(
                KeyGenParameterSpec.Builder(
                    kEY_STORE_ALIAS,
                    KeyProperties.PURPOSE_DECRYPT
                )
                    .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                    .build()
            )
            keyPairGenerator.generateKeyPair()
        }
    }

    fun encrypt(value: String): String {
        return try {
            val publicKey = keyStore.getCertificate(kEY_STORE_ALIAS)?.publicKey
            val cipher = Cipher.getInstance(TRANSFORMATION)

            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            Base64.encodeToString(cipher.doFinal(value.toByteArray(Charsets.UTF_8)), Base64.NO_WRAP)
        } catch (e: GeneralSecurityException) {
            ""
        }
    }

    fun decrypt(value: String): String {
        return try {
            val privateKey = keyStore.getKey(kEY_STORE_ALIAS, null)
            val cipher = Cipher.getInstance(TRANSFORMATION)
            val decoded = Base64.decode(value, Base64.NO_WRAP)

            cipher.init(Cipher.DECRYPT_MODE, privateKey)
            cipher.doFinal(decoded).toString(Charsets.UTF_8)
        } catch (e: GeneralSecurityException) {
            ""
        }
    }

    companion object {
        private const val KEY_PROVIDER = "AndroidKeyStore"
        private const val kEY_STORE_ALIAS = "operation-check-alias"
        private const val TRANSFORMATION = "RSA/ECB/PKCS1Padding"
    }
}
