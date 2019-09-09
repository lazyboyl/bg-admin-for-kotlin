package com.github.lazyboyl.vcm.kotlin.web.core.util

import org.apache.tomcat.util.codec.binary.Base64
import java.io.UnsupportedEncodingException
import java.security.*
import java.security.interfaces.RSAPrivateKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.util.HashMap
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException

class RsaUtil {

    companion object {
        private const val DEFAULT_RSA_KEY_SIZE = 2048

        private const val KEY_ALGORITHM = "RSA"

        /**
         * 生成RSA 公私钥,可选长度为1025,2048位.
         */
        fun generateRsaKey(keySize: Int): Map<String, String> {
            val result = HashMap<String, String>(2)
            try {
                val keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM)

                // 初始化密钥对生成器，密钥大小为1024 2048位
                keyPairGen.initialize(keySize, SecureRandom())
                // 生成一个密钥对，保存在keyPair中
                val keyPair = keyPairGen.generateKeyPair()
                // 得到公钥字符串
                result["publicKey"] = String(Base64.encodeBase64(keyPair.public.encoded))
                // 得到私钥字符串
                result["privateKey"] = String(Base64.encodeBase64(keyPair.private.encoded))
            } catch (e: GeneralSecurityException) {
                e.printStackTrace()
            }

            return result
        }

        /**
         * RSA私钥解密
         *
         * @param str        加密字符串
         * @param privateKey 私钥
         * @return 铭文
         * @throws Exception 解密过程中的异常信息
         */
        fun decrypt(str: String, privateKey: String): String {
            //64位解码加密后的字符串
            val inputByte: ByteArray
            var outStr = ""
            try {
                inputByte = Base64.decodeBase64(str.toByteArray(charset("UTF-8")))
                //base64编码的私钥
                val decoded = Base64.decodeBase64(privateKey)
                val priKey = KeyFactory.getInstance("RSA").generatePrivate(PKCS8EncodedKeySpec(decoded)) as RSAPrivateKey
                //RSA解密
                val cipher = Cipher.getInstance("RSA")
                cipher.init(Cipher.DECRYPT_MODE, priKey)
                outStr = String(cipher.doFinal(inputByte))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            } catch (e: NoSuchPaddingException) {
                e.printStackTrace()
            } catch (e: InvalidKeyException) {
                e.printStackTrace()
            } catch (e: IllegalBlockSizeException) {
                e.printStackTrace()
            } catch (e: BadPaddingException) {
                e.printStackTrace()
            } catch (e: InvalidKeySpecException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }

            return outStr
        }
    }

}