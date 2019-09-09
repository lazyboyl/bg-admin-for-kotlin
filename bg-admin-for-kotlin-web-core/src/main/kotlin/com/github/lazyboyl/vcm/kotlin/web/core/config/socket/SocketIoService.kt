package com.github.lazyboyl.vcm.kotlin.web.core.config.socket

interface SocketIoService {

    /**
     * 推送的事件
     */
    val PUSH_EVENT: String

    /**
     * 启动服务
     *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun start()

    /**
     * 停止服务
     */
    fun stop()

    /**
     * 功能描述：实现消息的发送
     *
     * @param loginAccount 需要发送到的账号
     * @param content      需要发送的内容
     */
    fun pushMessage(loginAccount: String, content: String)


}