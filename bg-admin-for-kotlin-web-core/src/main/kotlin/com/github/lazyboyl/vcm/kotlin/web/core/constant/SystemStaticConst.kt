package com.github.lazyboyl.vcm.kotlin.web.core.constant

/**
 * @author linzf
 * @since 2019/09/03
 * 类描述：系统静态实体类
 */
class SystemStaticConst {

    companion object {
        /**
         * 系统返回的编码的key
         */
        const val RESULT = "code"

        /**
         * 系统返回的消息的key
         */
        const val MSG = "msg"

        /**
         * 请求操作成功的标志
         */
        const val SUCCESS = 200

        /**
         * 请求操作失败的标志
         */
        const val FAIL = 400

        /**
         * 当前TOKEN过期
         */
        const val AUTH_TOKEN_EXPIRE = 409

        /**
         * 当前用户没有权限
         */
        const val AUTH_FAIL = 403

        /**
         * 当前用户没有登录
         */
        const val NOT_LOGIN = 401

        /**
         * 登陆的地址
         */
        const val LOGIN_URL = "/user/login"

        /**
         * 功能描述：刷新token的地址
         */
        const val REFRESH_URL = "/user/refreshToken"

        /**
         * 获取用户信息地址
         */
        const val USER_INFO_URL = "/user/getUserInfo"


        /**
         * 请求的方式
         */
        const val ACTION_TYPE_OPTIONS = "OPTIONS"
    }


}