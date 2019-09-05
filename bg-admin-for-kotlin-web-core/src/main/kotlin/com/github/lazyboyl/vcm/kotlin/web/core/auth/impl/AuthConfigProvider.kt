package com.github.lazyboyl.vcm.kotlin.web.core.auth.impl

import com.github.lazyboyl.vcm.kotlin.web.core.auth.AuthConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * @author linzf
 * @since 2019-09-05
 * 类描述：鉴权的配置的实现
 */
@Component
class AuthConfigProvider : AuthConfig {

    @Value("\${auth.singleLanding}")
    private var singleLanding: Boolean = false

    /**
     * 功能描述：获取当前的登陆是否允许单点登陆
     * @return 返回当前的单点登录的情况
     */
    override fun isSingleLanding(): Boolean {
        return singleLanding
    }
}
