package com.github.lazyboyl.vcm.kotlin.web.core.auth.impl

import com.github.lazyboyl.vcm.kotlin.web.core.auth.Auth
import com.github.lazyboyl.vcm.kotlin.web.core.auth.AuthToken
import com.github.lazyboyl.vcm.kotlin.web.core.constant.SystemStaticConst
import com.github.lazyboyl.vcm.kotlin.web.core.dao.RoleDao
import com.github.lazyboyl.vcm.kotlin.web.core.dao.TreeDao
import com.github.lazyboyl.vcm.kotlin.web.core.dao.UserDao
import com.github.lazyboyl.vcm.kotlin.web.core.entity.ReturnInfo
import com.github.lazyboyl.vcm.kotlin.web.core.entity.User
import com.github.lazyboyl.vcm.kotlin.web.core.util.RedisCache
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*


/**
 * @author linzf
 * @since 2019-09-05
 * 类描述：给予redis实现用户的鉴权
 */
@Component
class RedisAuthProvider : Auth {

    /**
     * redis工具类
     */
    @Autowired
    private lateinit var redisCache: RedisCache
    /**
     * 生成authToken和refreshToken的接口
     */
    @Autowired
    private lateinit var authToken: AuthToken
    /**
     * 用户的dao
     */
    @Autowired
    private lateinit var userDao: UserDao
    /**
     * 菜单的dao
     */
    @Autowired
    private lateinit var treeDao: TreeDao
    /**
     * 角色的dao
     */
    @Autowired
    private lateinit var roleDao: RoleDao
    /**
     * 鉴权的配置接口类的实现
     */
    @Autowired
    private lateinit var authConfigProvider: AuthConfigProvider

    override fun login(loginAccount: String, loginPassword: String): ReturnInfo {
        val user = userDao.login(loginAccount, loginPassword) ?: return ReturnInfo(SystemStaticConst.FAIL, "账号密码错误")
        val roleList = roleDao.getUserRoleListByUserId(user.userId ?: "")
        val userRoles = StringBuilder()
        for (role in roleList) {
            userRoles.append(":").append(role.roleCode)
        }
        /**
         * 登录成功以后加载用户的权限，同时使用用户的token作为key，使用权限数据作为value来进行数据的保存
         */
        val treeList = treeDao.getLoginUserPowerPath(user.userId ?: "")
        val powerPaths = StringBuilder()
        for (tree in treeList) {
            if (!tree.powerPath.isNullOrEmpty()) {
                powerPaths.append(":").append(tree.powerPath)
            }
        }
        val authTokenValue = authToken.authToken()
        val refreshTokenValue = authToken.refreshToken()
        /**
         * 更新用户表中的token的值
         */
        user.token = authTokenValue
        user.lastLoginDate = Date()
        userDao.updateByPrimaryKey(user)
        return ReturnInfo(SystemStaticConst.SUCCESS, "登陆成功", setRedisToken("", authTokenValue, refreshTokenValue, powerPaths.toString(), user, userRoles.toString()))
    }

    /**
     * 功能描述：实现重新刷新token
     *
     * @param refreshToken token的值
     * @return 返回刷新token的结果
     */
    override fun refreshToken(refreshToken: String): ReturnInfo {
        /**
         * 从redis中获取权限的数据
         */
        val powerPaths = redisCache.getString(refreshToken) ?: ""
        if ("" == powerPaths) {
            return ReturnInfo(SystemStaticConst.NOT_LOGIN, "无效的refreshToken")
        }
        /**
         * 从redis中获取当前登录的用户
         */
        /**
         * 从redis中获取当前登录的用户
         */
        val user = redisCache.getObject(refreshToken + "_USER", User::class.java)
                ?: return ReturnInfo(SystemStaticConst.NOT_LOGIN, "无效的refreshToken")
        /**
         * 从redis中获取当前登录的用户的角色
         */
        val userRoles = redisCache.getString(refreshToken + "_USER_ROLE") ?: ""
        if ("" == userRoles) {
            return ReturnInfo(SystemStaticConst.NOT_LOGIN, "无效的refreshToken")
        }
        redisCache.deleteKey(refreshToken)
        redisCache.deleteKey(refreshToken + "_USER")
        redisCache.deleteKey(refreshToken + "_USER_ROLE")
        val authTokenValue = authToken.authToken()
        val refreshTokenValue = authToken.refreshToken()
        return ReturnInfo(SystemStaticConst.SUCCESS, "token刷新成功", setRedisToken(refreshToken, authTokenValue, refreshTokenValue, powerPaths, user, userRoles))
    }

    /**
     * 功能描述：实现redis中设置权限的相关数据
     *
     * @param oldRefreshTokenValue 旧的刷新的token
     * @param authTokenValue       鉴权的token
     * @param refreshTokenValue    刷新鉴权的token
     * @param powerPaths           权限集合
     * @param userRoles            角色的集合
     * @return 返回相应的值
     */
    private fun setRedisToken(oldRefreshTokenValue: String, authTokenValue: String, refreshTokenValue: String, powerPaths: String, user: User, userRoles: String): Map<String, Any> {
        var loginToken = ""
        /**
         * 当前系统是单点登录
         */
        if (!authConfigProvider.isSingleLanding()) {
            loginToken = redisCache.getString(user.loginAccount ?: "") ?: ""
            redisCache.setString(user.loginAccount
                    ?: "", "$authTokenValue:$refreshTokenValue", authToken.refreshTokenExpire())
        } else if ("" != oldRefreshTokenValue) {
            loginToken = redisCache.getString(user.loginAccount ?: "") + "_" + oldRefreshTokenValue
            redisCache.setString(user.loginAccount + "_" + refreshTokenValue, "$authTokenValue:$refreshTokenValue", authToken.refreshTokenExpire())
        } else {
            redisCache.setString(user.loginAccount + "_" + refreshTokenValue, "$authTokenValue:$refreshTokenValue", authToken.refreshTokenExpire())
        }
        if ("" != loginToken) {
            // 删除另外登陆的账号的token信息
            val loginAuthTokenValue = loginToken.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            redisCache.deleteKey(loginAuthTokenValue)
            redisCache.deleteKey(loginAuthTokenValue + "_USER")
            redisCache.deleteKey(loginAuthTokenValue + "_USER_ROLE")
            val loginRefreshTokenValue = loginToken.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            redisCache.deleteKey(loginRefreshTokenValue)
            redisCache.deleteKey(loginRefreshTokenValue + "_USER")
            redisCache.deleteKey(loginRefreshTokenValue + "_USER_ROLE")
        }
        /**
         * 将authToken作为key,powerPaths作为值保存到redis中
         */
        redisCache.setString(authTokenValue, powerPaths, authToken.authTokenExpire())
        /**
         * 将authToken作为key,user作为值保存到redis中
         */
        redisCache.setObject(authTokenValue + "_USER", user, authToken.authTokenExpire())
        /**
         * 将authToken作为key,userRoles作为值保存到redis中
         */
        redisCache.setString(authTokenValue + "_USER_ROLE", userRoles, authToken.authTokenExpire())
        /**
         * 将refreshToken作为key,powerPaths作为值保存到redis中
         */
        redisCache.setString(refreshTokenValue, powerPaths, authToken.refreshTokenExpire())
        /**
         * 将refreshToken作为key,user作为值保存到redis中
         */
        redisCache.setObject(refreshTokenValue + "_USER", user, authToken.refreshTokenExpire())
        /**
         * 将refreshToken作为key,userRoles作为值保存到redis中
         */
        redisCache.setString(refreshTokenValue + "_USER_ROLE", userRoles, authToken.refreshTokenExpire())
        /**
         * 刷新token的时候，socket重新设置映射关系
         */
        if ("" != oldRefreshTokenValue) {
            val socketToken = redisCache.getString(oldRefreshTokenValue + "_SOCKET")
            if (socketToken != null && "" != socketToken) {
                redisCache.setString(refreshTokenValue + "_SOCKET", socketToken)
                redisCache.deleteKey(oldRefreshTokenValue + "_SOCKET")
            }
        }
        val result = HashMap<String, Any>(1)
        result["token"] = authTokenValue
        result["refreshToken"] = refreshTokenValue
        result["expiresIn"] = redisCache.getExpire(authTokenValue)
        return result
    }
}