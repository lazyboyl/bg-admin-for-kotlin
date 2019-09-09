package com.github.lazyboyl.vcm.kotlin.web.core.config

import com.github.lazyboyl.vcm.kotlin.web.core.annotation.filter.AuthControllerInterceptor
import com.github.lazyboyl.vcm.kotlin.web.core.filter.AuthInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * 类描述： 设置mvc的拦截器
 * @author linzf
 * @since 2019-09-06
 */
@Component
class InterceptorConfig : WebMvcConfigurer {

    @Autowired
    lateinit var authInterceptor: AuthInterceptor

    @Autowired
    lateinit var authControllerInterceptor: AuthControllerInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/**")
        registry.addInterceptor(authControllerInterceptor).addPathPatterns("/**")
    }


}