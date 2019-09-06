package com.github.lazyboyl.vcm.kotlin.web.core.annotation.filter

import com.github.lazyboyl.vcm.kotlin.web.core.annotation.AuthController
import com.github.lazyboyl.vcm.kotlin.web.core.auth.ReleaseUrl
import com.github.lazyboyl.vcm.kotlin.web.core.constant.SystemStaticConst
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import java.util.regex.Pattern
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthControllerInterceptor : HandlerInterceptor {

    /**
     * 获取放行的权限的接口
     */
    @Autowired
    lateinit var releaseUrl: ReleaseUrl

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (SystemStaticConst.ACTION_TYPE_OPTIONS == request.method) {
            return true
        }
        val actionUrl = request.servletPath
        val jsPattern = ".*.js"
        val cssPattern = ".*.css"
        val pngPattern = ".*.png"
        val gifPattern = ".*.gif"
        if (Pattern.matches(gifPattern, actionUrl) || Pattern.matches(jsPattern, actionUrl) || Pattern.matches(cssPattern, actionUrl) || Pattern.matches(pngPattern, actionUrl)) {
            return true
        }
        /**
         * 判断当前的响应地址是否可以放行
         */
        val releasePath = releaseUrl.getReleaseUrl()
        if (releasePath.indexOf(actionUrl) == -1) {
            // 将handler强转为HandlerMethod, 前面已经证实这个handler就是HandlerMethod
            val handlerMethod = handler as HandlerMethod
            // 从方法处理器中获取出要调用的方法
            val method = handlerMethod.method
            // 获取出方法上的AuthController注解
            val authController = method.getAnnotation(AuthController::class.java) ?: return true
            println("$actionUrl----++-------$authController----")
            for(a in authController.authorities){
                println(a)
            }
        }
        return super.preHandle(request, response, handler)
    }
}