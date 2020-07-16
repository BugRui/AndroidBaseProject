package com.bugrui.jetpackproject.utils

import android.util.Base64
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.nio.ByteBuffer
import java.security.*
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException

/**
 * @Author:            BugRui
 * @CreateDate:        2019/12/9 17:29
 * @Description:       java类作用描述
 */
object RSAUtil {
    //构建Cipher实例时所传入的的字符串，默认为"RSA/NONE/PKCS1Padding"
    private var sTransform = "RSA/NONE/PKCS1Padding"
    //进行Base64转码时的flag设置，默认为Base64.DEFAULT
    private var sBase64Mode = Base64.DEFAULT
    private const val STR_UTF8 = "UTF-8"
    //初始化方法，设置参数
    fun init(transform: String, base64Mode: Int) {
        sTransform = transform
        sBase64Mode = base64Mode
    }

    /**
     * 产生密钥对
     *
     * @param keyLength 密钥长度，小于1024长度的密钥已经被证实是不安全的，通常设置为1024或者2048，建议2048
     */
    fun generateRSAKeyPair(keyLength: Int): KeyPair? {
        var keyPair: KeyPair? = null
        try {
            val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
            //设置密钥长度
            keyPairGenerator.initialize(keyLength)
            //产生密钥对
            keyPair = keyPairGenerator.generateKeyPair()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return keyPair
    }

    /**
     * 载入公钥
     *
     * @param publicKeyStr
     * @return
     */
    @Throws(Exception::class)
    private fun loadPublicKey(publicKeyStr: String?): RSAPublicKey {
        val buffer = Base64.decode(publicKeyStr, Base64.DEFAULT)
        val keyFactory = KeyFactory.getInstance("RSA")
        val keySpec = X509EncodedKeySpec(buffer)
        return keyFactory.generatePublic(keySpec) as RSAPublicKey
    }

    /**
     * 加载私钥
     *
     * @param privateKeyStr
     * @return
     */
    @Throws(Exception::class)
    private fun loadPrivateKey(privateKeyStr: String): RSAPrivateKey {
        val buffer = Base64.decode(privateKeyStr, Base64.DEFAULT)
        val keySpec = PKCS8EncodedKeySpec(buffer)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(keySpec) as RSAPrivateKey
    }

    /**
     * 加密数据方法
     *
     * @param srcData 待处理的数据
     * @param key     公钥或者私钥
     * @param mode    指定是加密还是解密，值为Cipher.ENCRYPT_MODE或者Cipher.DECRYPT_MODE
     */
    private fun processData(
        srcData: ByteArray,
        key: Key,
        mode: Int
    ): ByteArray? { //用来保存处理结果
        var resultBytes: ByteArray? = null
        try { //获取Cipher实例
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding") //android对服务端后台情况
            //            Cipher cipher = Cipher.getInstance(sTransform, Security.getProvider("BC"));
//初始化Cipher，mode指定是加密还是解密，key为公钥或私钥
            cipher.init(mode, key)
            val out = ByteArrayOutputStream(srcData.size)
            val BLOCK_SIZE = 117
            var i = 0
            while (i < srcData.size) {
                val leftBytes = srcData.size - i
                val length = if (leftBytes <= BLOCK_SIZE) leftBytes else BLOCK_SIZE
                out.write(cipher.doFinal(srcData, i, length))
                i += BLOCK_SIZE
            }
            out.close()
            //处理数据
            resultBytes = out.toByteArray()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return resultBytes
    }

    /**
     * 执行加密
     *
     * @param string 需要加密的字符串
     * @return
     */
    fun encode(string: String,encodeKey:String): String {
        return try {
            val jsonObj = JSONObject()
            val str = encryptDataByPublicKey(
                string,
                encodeKey
            )
            jsonObj.put("android", str)
            jsonObj.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
            string
        }
    }

    /**
     * 数据解密
     *
     * @param string 需要解密的字符串
     * @return
     */
    fun decode(string: String?,decodeKey:String ): String? {
        if (string.isNullOrEmpty()) return null
        return try {
            val jsonObj = JSONObject(string)
            val data = jsonObj.getString("data")
            decryptedToStrByPublicKey(data, decodeKey)
        } catch (e: JSONException) {
            e.printStackTrace()
            string
        }
    }

    /**
     * 使用公钥加密数据，结果用Base64转码,指定utf-8编码
     *
     * @param srcData
     * @param publicKey
     * @return
     */
    fun encryptDataByPublicKey(srcData: String, publicKey: String?): String? {
        try {
            val rawText = srcData.toByteArray(charset(STR_UTF8))
            val key = loadPublicKey(publicKey)
            val encode =
                Base64.encode(encryptDataByPublicKey(rawText, key), Base64.DEFAULT)
            return String(encode)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 使用公钥加密数据
     *
     * @param rawText
     * @param publicKey
     * @return
     */
    fun encryptDataByPublicKey(
        rawText: ByteArray,
        publicKey: RSAPublicKey?
    ): ByteArray? {
        try {
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            val bslist: MutableList<ByteArray> =
                ArrayList()
            var totallength = 0
            val BLOCK_SIZE = 117
            run {
                var i = 0
                while (i < rawText.size) {
                    val leftBytes = rawText.size - i
                    val length = if (leftBytes <= BLOCK_SIZE) leftBytes else BLOCK_SIZE
                    val bs = cipher.doFinal(rawText, i, length)
                    totallength += bs.size
                    bslist.add(bs)
                    i += BLOCK_SIZE
                }
            }
            val bf = ByteBuffer.allocate(totallength)
            for (i in bslist.indices) {
                bf.put(bslist[i])
            }
            return bf.array()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    /**
     * 使用私钥解密，解密数据转换为字符串，并指定字符集
     *
     * @param encryptedData
     * @param privateKey
     * @param charset
     * @return
     */
    /**
     * 使用私钥进行解密，解密数据转换为字符串，使用utf-8编码格式
     *
     * @param encryptedData
     * @param privateKey
     * @return
     */
    @JvmOverloads
    fun decryptedToStrByPrivate(
        encryptedData: String?,
        privateKey: String
    ): String? {
        try {
            val key = loadPrivateKey(privateKey)
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.DECRYPT_MODE, key)
            val encryptData = Base64.decode(encryptedData, Base64.DEFAULT)
            val out = ByteArrayOutputStream(encryptData.size)
            val BLOCK_SIZE = 128
            var i = 0
            while (i < encryptData.size) {
                val leftBytes = encryptData.size - i
                val length = if (leftBytes <= BLOCK_SIZE) leftBytes else BLOCK_SIZE
                out.write(cipher.doFinal(encryptData, i, length))
                i += BLOCK_SIZE
            }
            return String(out.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 使用私钥加密，结果用Base64转码
     *
     * @param srcData
     * @param privateKey
     * @return
     */
    fun encryptDataByPrivateKey(srcData: ByteArray, privateKey: PrivateKey): String {
        val resultBytes =
            processData(srcData, privateKey, Cipher.ENCRYPT_MODE)
        return Base64.encodeToString(resultBytes, sBase64Mode)
    }


    /**
     * 使用公钥解密，结果转换为字符串，使用指定字符集
     *
     * @param encryptedData
     * @param publicKey
     * @param charset
     * @return
     */
    fun decryptedToStrByPublicKey(
        encryptedData: String,
        publicKey: String?
    ): String? {
        try {
            val key = loadPublicKey(publicKey)
            //            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            val cipher = Cipher.getInstance("RSA/None/PKCS1Padding")
            cipher.init(Cipher.DECRYPT_MODE, key)
            val encryptData = Base64.decode(encryptedData, Base64.DEFAULT)
            val out = ByteArrayOutputStream(encryptData.size)
            val BLOCK_SIZE = 128
            var i = 0
            while (i < encryptData.size) {
                val leftBytes = encryptData.size - i
                val length = if (leftBytes <= BLOCK_SIZE) leftBytes else BLOCK_SIZE
                out.write(cipher.doFinal(encryptData, i, length))
                i += BLOCK_SIZE
            }
            return String(out.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


}