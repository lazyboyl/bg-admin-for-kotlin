package com.github.lazyboyl.vcm.kotlin.web.core.auth

/**
 * @author linzf
 * @since 2019-09-05
 * 类描述：鉴权的配置接口类
 */
interface AuthConfig {

    /**
     * 功能描述：获取当前的登陆是否允许单点登陆
     * @return
     */
    fun isSingleLanding(): Boolean

}