package com.github.lazyboyl.vcm.kotlin.web.core.filter

import com.github.lazyboyl.vcm.kotlin.web.core.auth.ReleaseUrl
import com.github.lazyboyl.vcm.kotlin.web.core.constant.SystemStaticConst
import com.github.lazyboyl.vcm.kotlin.web.core.dao.BehaviorLogDao
import com.github.lazyboyl.vcm.kotlin.web.core.entity.BehaviorLog
import com.github.lazyboyl.vcm.kotlin.web.core.entity.User
import com.github.lazyboyl.vcm.kotlin.web.core.util.RedisCache
import com.github.lazyboyl.vcm.kotlin.web.core.util.WriteUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.util.*
import java.util.regex.Pattern
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthInterceptor : HandlerInterceptor {

    /**
     * 日志的dao
     */
    @Autowired
    lateinit var  behaviorLogDao: BehaviorLogDao

    /**
     * 操作redis的实现类
     */
    @Autowired
    lateinit var  redisCache: RedisCache
    /**
     * 获取放行的权限的接口
     */
    @Autowired
    lateinit var  releaseUrl: ReleaseUrl

    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (SystemStaticConst.ACTION_TYPE_OPTIONS.equals(request.method)) {
            return true
        }
        val token = request.getHeader("x-access-token")
        val actionUrl = request.servletPath
        val jsPattern = ".*.js"
        val cssPattern = ".*.css"
        val pngPattern = ".*.png"
        val gifPattern = ".*.gif"
        if (Pattern.matches(gifPattern, actionUrl) || Pattern.matches(jsPattern, actionUrl) || Pattern.matches(cssPattern, actionUrl) || Pattern.matches(pngPattern, actionUrl)) {
            return true
        }
        val behaviorLog = BehaviorLog()
        behaviorLog.actionDate = Date()
        behaviorLog.actionMethod = actionUrl

        /**
         * 判断当前的响应地址是否可以放行
         */
        val releasePath = releaseUrl.getReleaseUrl()
        if (releasePath.indexOf(actionUrl) == -1) {
            if (token == null || "" == token || "null" == token) {
                WriteUtil.write(response, SystemStaticConst.NOT_LOGIN, "用户未登录")
                return false
            }
            /**
             * 判断当前的用户是否有权限访问当前节点
             */
           println("token:$token")
            val powerPath = redisCache.getString(token)?:(return false)
            if ("" == powerPath) {
                WriteUtil.write(response, SystemStaticConst.AUTH_TOKEN_EXPIRE, "token过期")
                return false
            }
            if (powerPath.indexOf(actionUrl) == -1) {
                WriteUtil.write(response, SystemStaticConst.AUTH_FAIL, "用户无权限，请联系系统管理员")
                return false
            }
            /**
             * 根据token来获取当前登录的用户的信息
             */
            val user = redisCache.getObject(token + "_USER", User::class.java)
            if (user == null) {
                behaviorLog.actionUser = ""
                behaviorLog.actionUserId = ""
                behaviorLogDao.insert(behaviorLog)
                WriteUtil.write(response, SystemStaticConst.NOT_LOGIN, "用户未登录")
                return false
            } else {
                behaviorLog.actionUser = user.nickName
                behaviorLog.actionUserId = user.userId
                behaviorLogDao.insert(behaviorLog)
            }
        } else {
            behaviorLog.actionUser = ""
            behaviorLog.actionUserId = ""
            behaviorLogDao.insert(behaviorLog)
        }
        return true
    }

}