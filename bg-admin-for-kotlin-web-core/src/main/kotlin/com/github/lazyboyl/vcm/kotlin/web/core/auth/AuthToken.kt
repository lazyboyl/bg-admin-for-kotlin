package com.github.lazyboyl.vcm.kotlin.web.core.auth

/**
 * @author linzf
 * @since 2019-09-05
 * 类描述：生成token和refresh token的接口类
 */
interface AuthToken {

    /**
     * 功能描述：生成authToken的值
     * @return 返回authToken的值
     */
    fun authToken(): String

    /**
     * 功能描述：生成refreshToken的值
     * @return 返回refreshToken的值
     */
    fun refreshToken(): String

    /**
     * 功能描述：authToken的过期时间
     * @return 返回过期时间
     */
    fun authTokenExpire(): Int

    /**
     * 功能描述：refreshToken的过期时间
     * @return 返回过期时间
     */
    fun refreshTokenExpire(): Int

}