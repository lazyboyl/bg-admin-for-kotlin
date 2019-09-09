package com.github.lazyboyl.vcm.kotlin.web.core.config.socket

/**
 * 类描述： 消息推送的实体
 */
class PushMessage(socketToken: String, content: String) {

    /**
     * 登录socket的socketToken
     */
    var socketToken: String? = socketToken

    /**
     * 推送的内容
     */
    var content: String? = content

}