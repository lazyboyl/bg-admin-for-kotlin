package com.github.lazyboyl.vcm.kotlin.web.core.util

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * 类描述： 序列化工具类
 * @author linzf
 * @since 2019-09-05
 */
class SerializeUtil {

    companion object {

        /**
         * 功能描述： 将对象序列化
         */
        fun serialize(`object`: Any): ByteArray {
            var oos: ObjectOutputStream?
            var baos: ByteArrayOutputStream?
            try {
                // 序列化
                baos = ByteArrayOutputStream()
                oos = ObjectOutputStream(baos)
                oos.writeObject(`object`)
                return baos.toByteArray()
            } catch (e: Exception) {
                throw RuntimeException(e.message, e)
            }

        }

        /**
         * 功能描述： 将对象反序列化
         */
        fun unserialize(bytes: ByteArray): Any {
            var bais: ByteArrayInputStream?
            try {
                // 反序列化
                bais = ByteArrayInputStream(bytes)
                val ois = ObjectInputStream(bais)
                return ois.readObject()
            } catch (e: Exception) {
                throw RuntimeException(e.message, e)
            }

        }
    }


}