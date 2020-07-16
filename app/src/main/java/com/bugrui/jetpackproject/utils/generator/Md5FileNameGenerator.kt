package com.bugrui.jetpackproject.utils.generator

import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * @Author: BugRui
 * @CreateDate: 2019/8/27 11:38
 * @Description: 图片文件名的MD5哈希图片URL
 */
class Md5FileNameGenerator : FileNameGenerator {


    override fun generate(fileUrl: String): String {
        val md5 = getMD5(fileUrl.toByteArray())
        val bi = BigInteger(md5).abs()
        return bi.toString(RADIX)
    }

    override fun generate(fileUrl: String, ending: String): String {
        return generate(fileUrl) + "." + ending
    }

    private fun getMD5(data: ByteArray): ByteArray? {
        var hash: ByteArray? = null
        try {
            val digest = MessageDigest.getInstance(HASH_ALGORITHM)
            digest.update(data)
            hash = digest.digest()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return hash
    }

    companion object {

        private const val HASH_ALGORITHM = "MD5"
        private const val RADIX = 10 + 26   //10位数字+26个字母
    }

}
