package com.github.lazyboyl.vcm.kotlin.web.core.util

import com.github.lazyboyl.vcm.kotlin.web.core.entity.User
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

class UserInfo {

    companion object {

        /**
         * 功能描述：获取当前登录的用户的信息
         * @param redisCache
         * @return
         */
        fun getLoginUser(redisCache: RedisCache): User? {
            val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
            val token = request.getHeader("x-access-token")
            return if ("" == token) {
                null
            } else redisCache.getObject(token + "_USER", User::class.java)
        }

    }

}