package com.github.lazyboyl.vcm.kotlin.web.core.config.socket

import com.corundumstudio.socketio.SocketConfig
import com.corundumstudio.socketio.SocketIOServer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SocketIoConfig {

    @Value("\${socketio.host}")
    var host: String = ""

    @Value("\${socketio.port}")
    var port: Int = 0

    @Value("\${socketio.bossCount}")
    var bossCount: Int = 0

    @Value("\${socketio.workCount}")
    var workCount: Int = 0

    @Value("\${socketio.allowCustomRequests}")
    var allowCustomRequests: Boolean = false

    @Value("\${socketio.upgradeTimeout}")
    var upgradeTimeout: Int = 0

    @Value("\${socketio.pingTimeout}")
    var pingTimeout: Int = 0

    @Value("\${socketio.pingInterval}")
    var pingInterval: Int = 0

    /**
     * 以下配置在上面的application.yml中已经注明
     * @return 实例化socketIo的服务对象
     */
    @Bean
    fun socketIOServer(): SocketIOServer {
        val socketConfig = SocketConfig()
        socketConfig.isTcpNoDelay = true
        socketConfig.soLinger = 0
        val config: com.corundumstudio.socketio.Configuration = com.corundumstudio.socketio.Configuration()
        config.socketConfig = socketConfig
        config.hostname = host
        config.port = port
        config.bossThreads = bossCount
        config.workerThreads = workCount
        config.isAllowCustomRequests = allowCustomRequests
        config.upgradeTimeout = upgradeTimeout
        config.pingTimeout = pingTimeout
        config.pingInterval = pingInterval
        return SocketIOServer(config)
    }

}