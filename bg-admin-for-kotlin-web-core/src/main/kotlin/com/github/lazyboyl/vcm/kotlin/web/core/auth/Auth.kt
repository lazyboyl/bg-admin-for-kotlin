package com.github.lazyboyl.vcm.kotlin.web.core.auth

import com.github.lazyboyl.vcm.kotlin.web.core.entity.ReturnInfo

/**
 * @author linzf
 * @since 2019-09-05
 * 类描述：鉴权的接口类
 */
interface Auth {

    /**
     * 功能描述：实现用户登陆
     * @param loginAccount 用户账号
     * @param loginPassword 用户密码
     * @return 返回登陆结果
     */
    fun login(loginAccount: String, loginPassword: String): ReturnInfo

    /**
     * 功能描述：实现重新刷新token
     * @param refreshToken token的值
     * @return 返回刷新结果
     */
    fun refreshToken(refreshToken: String): ReturnInfo

}