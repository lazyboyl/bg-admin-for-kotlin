package com.github.lazyboyl.vcm.kotlin.web.core.auth.impl

import com.github.lazyboyl.vcm.kotlin.web.core.auth.AuthToken
import com.github.lazyboyl.vcm.kotlin.web.core.util.UuidUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * @author linzf
 * @since 2019-09-05
 * 类描述：生成token和refresh token的实现类
 */
@Component
class AuthTokenProvider : AuthToken {

    /**
     * token过期时间
     */
    @Value("\${auth.authTokenExpire}")
    private var authTokenExpire: Int = 0

    /**
     * refreshToken过期时间
     */
    @Value("\${auth.refreshTokenExpire}")
    private var refreshTokenExpire: Int = 0

    /**
     * 功能描述：生成authToken的值
     * @return 返回authToken的值
     */
    override fun authToken(): String {
        return UuidUtils.getUUID()
    }

    /**
     * 功能描述：生成refreshToken的值
     * @return 返回refreshToken的值
     */
    override fun refreshToken(): String {
        return UuidUtils.getUUID()
    }

    /**
     * 功能描述：authToken的过期时间
     * @return 返回过期时间
     */
    override fun authTokenExpire(): Int {
        return if (authTokenExpire > 0) {
            authTokenExpire
        } else {
            3600
        }
    }

    /**
     * 功能描述：refreshToken的过期时间
     * @return 返回过期时间
     */
    override fun refreshTokenExpire(): Int {
        return if (refreshTokenExpire > 0) {
            refreshTokenExpire
        } else {
            4800
        }
    }
}