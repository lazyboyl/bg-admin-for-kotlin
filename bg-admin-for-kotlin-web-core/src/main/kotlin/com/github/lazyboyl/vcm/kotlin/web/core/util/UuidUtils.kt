package com.github.lazyboyl.vcm.kotlin.web.core.util

import java.util.*

class UuidUtils {

    companion object {
        /**
         * 获取主键uuid
         * @return uuid
         */
        fun getUUID(): String {
            return UUID.randomUUID().toString().replace("-".toRegex(), "")
        }

        /**
         * 获取随机的UUID字符串
         * @return uuid
         */
        fun getRandomUuid(): String {
            val uuid = UUID.randomUUID()
            return uuid.toString().toLowerCase()
        }
    }


}