package com.github.lazyboyl.vcm.kotlin.web.core.config.socket.impl

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import com.github.lazyboyl.vcm.kotlin.web.core.config.socket.PushMessage
import com.github.lazyboyl.vcm.kotlin.web.core.config.socket.SocketIoService
import com.github.lazyboyl.vcm.kotlin.web.core.util.RedisCache
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Service(value = "socketIOService")
class SocketIoServiceImpl : SocketIoService {

    /**
     * 用来存已连接的客户端
     */
    private val clientMap = ConcurrentHashMap<String, SocketIOClient>()

    /**
     * 注入socketIOServer
     */
    @Autowired
    lateinit var socketIOServer: SocketIOServer

    /**
     * 注入redisCache
     */
    @Autowired
    lateinit var redisCache: RedisCache


    /**
     * Spring IoC容器创建之后，在加载SocketIOServiceImpl Bean之后启动
     *
     * @throws Exception
     */
    @PostConstruct
    @Throws(Exception::class)
    fun autoStartup() {
        start()
    }

    /**
     * Spring IoC容器在销毁SocketIOServiceImpl Bean之前关闭,避免重启项目服务端口占用问题
     *
     * @throws Exception
     */
    @PreDestroy
    @Throws(Exception::class)
    fun autoStop() {
        stop()
    }

    override fun start() {
        // 监听客户端连接
        socketIOServer.addConnectListener { client ->
            val socketTokens = getParamsByClient(client)["socketToken"] ?: return@addConnectListener
            if (socketTokens.isEmpty()) {
                println("socket连接失败，失败原因：socketToken的值不能为空！")
                return@addConnectListener
            }
            val refreshTokens = getParamsByClient(client)["refreshToken"] ?: return@addConnectListener
            if (refreshTokens.isEmpty()) {
                println("socket连接失败，失败原因：refreshToken的值不能为空！")
                return@addConnectListener
            }
            val refreshToken = refreshTokens[0]
            val powerPath = redisCache.getString(refreshToken)
            if (powerPath.isNullOrEmpty()) {
                println("socket连接失败，失败原因：无此token的用户权限信息！")
                return@addConnectListener
            }
            val socketToken = socketTokens[0]
            if (socketToken.isNotEmpty()) {
                clientMap.put(socketToken, client)
                /**
                 * 防止不断刷新页面导致不断的增加socket的连接
                 */
                val socketTokenOld = redisCache.getString(refreshToken + "_SOCKET")
                if (socketTokenOld != null && "" != socketTokenOld) {
                    val socketIOClient = clientMap[socketTokenOld]
                    if (socketIOClient != null) {
                        clientMap.get(socketTokenOld)!!.disconnect()
                        clientMap.remove(socketTokenOld)
                    }
                }
                println("当前保持连接的socket的数为$clientMap.size")
                /**
                 * 与socket连接成功，将用户和socket的绑定关系保存到redis中
                 */
                redisCache.setString(refreshToken + "_SOCKET", socketToken)
            }
        }

        // 监听客户端断开连接
        socketIOServer.addDisconnectListener { client ->
            val socketTokens = getParamsByClient(client).get("socketToken") ?: return@addDisconnectListener
            if (socketTokens.isEmpty()) {
                return@addDisconnectListener
            }
            val socketToken = socketTokens[0]
            if (socketToken .isNotEmpty()){
                clientMap.remove(socketToken)
                client.disconnect()
            }
        }

        // 处理自定义的事件，与连接监听类似
        socketIOServer.addEventListener(PUSH_EVENT, PushMessage::class.java) { client, data, ackSender ->
            // TODO do something
        }
        socketIOServer.start()
    }

    override fun stop() {
        if (socketIOServer != null) {
            socketIOServer.stop()
        }
    }

    override fun pushMessage(loginAccount: String, content: String) {
        /**
         * 功能描述：获取当前在线的用户的token和refreshToken
         */
        val loginTokens = redisCache.queryKeys(loginAccount)
        for (loginToken in loginTokens) {
            if ("" != loginToken) {
                // 获取当前的refreshToken的值
                val loginRefreshTokenValue = loginToken.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                // 通过refreshToken来获取socketToken的值
                val socketToken = redisCache.getString(loginRefreshTokenValue + "_SOCKET") ?: return
                if (socketToken.isNotEmpty()) {
                    val client = clientMap[socketToken]
                    client?.sendEvent(PUSH_EVENT, PushMessage(socketToken, content))
                }
            }
        }
    }

    override val PUSH_EVENT: String = "push_event"

    /**
     * 此方法为获取client连接中的参数，可根据需求更改
     *
     * @param client
     * @return
     */
    private fun getParamsByClient(client: SocketIOClient): Map<String, List<String>> {
        // 从请求的连接中拿出参数
        return client.handshakeData.urlParams
    }

}