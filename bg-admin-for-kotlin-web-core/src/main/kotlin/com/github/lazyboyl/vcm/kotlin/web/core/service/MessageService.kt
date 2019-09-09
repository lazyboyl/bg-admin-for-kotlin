package com.github.lazyboyl.vcm.kotlin.web.core.service

import com.github.lazyboyl.vcm.kotlin.web.core.config.socket.SocketIoService
import com.github.lazyboyl.vcm.kotlin.web.core.constant.MessageConstant
import com.github.lazyboyl.vcm.kotlin.web.core.constant.SystemStaticConst
import com.github.lazyboyl.vcm.kotlin.web.core.dao.MessageDao
import com.github.lazyboyl.vcm.kotlin.web.core.dao.TargetMessageDao
import com.github.lazyboyl.vcm.kotlin.web.core.entity.Message
import com.github.lazyboyl.vcm.kotlin.web.core.entity.Page
import com.github.lazyboyl.vcm.kotlin.web.core.entity.ReturnInfo
import com.github.lazyboyl.vcm.kotlin.web.core.entity.TargetMessage
import com.github.lazyboyl.vcm.kotlin.web.core.util.PageUtil
import com.github.lazyboyl.vcm.kotlin.web.core.util.RedisCache
import com.github.lazyboyl.vcm.kotlin.web.core.util.UserInfo
import com.github.pagehelper.PageHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * 类描述：消息的service
 * @author linzf
 * @since 2019-09-09
 */
@Service
@Transactional(rollbackFor = [Exception::class])
class MessageService {

    /**
     * 消息的dao
     */
    @Autowired
    lateinit var messageDao: MessageDao

    /**
     * socket 推送的bean
     */
    @Autowired
    lateinit var socketIoService: SocketIoService

    /**
     * 消息接收的dao
     */
    @Autowired
    lateinit var targetMessageDao: TargetMessageDao

    /**
     * redis的工具类
     */
    @Autowired
    lateinit var redisCache: RedisCache

    /**
     * 功能描述：实现消息的阅读
     *
     * @param targetMessageId 消息关联流水ID
     * @return 返回更新结果
     */
    fun readMsg(targetMessageId: String): ReturnInfo {
        return if (targetMessageDao.readMsg(MessageConstant.READE_STATE_READE, Date(), targetMessageId) == 0) {
            ReturnInfo(SystemStaticConst.FAIL, "消息阅读失败！")
        } else {
            ReturnInfo(SystemStaticConst.SUCCESS, "消息阅读成功！")
        }
    }

    /**
     * 功能描述：获取当前登录的用户的未读的消息
     *
     * @return 返回消息数据
     */
    fun queryUserMsg(): ReturnInfo {
        val user = UserInfo.getLoginUser(redisCache) ?: return ReturnInfo(SystemStaticConst.FAIL, "用户未登录！")
        return ReturnInfo(SystemStaticConst.SUCCESS, "获取用户消息成功！", messageDao.queryUserMsg(user.userId?:""))
    }

    /**
     * 功能描述：实现消息的发布
     *
     * @param title       消息标题
     * @param content     消息内容
     * @param targetUsers 接收用户ID
     * @return 返回发布结果
     */
    fun publishNews(title: String, content: String, targetUsers: Array<String>): ReturnInfo {
        val user = UserInfo.getLoginUser(redisCache) ?: return ReturnInfo(SystemStaticConst.FAIL, "用户未登录！")
        val message = Message()
        message.title = title
        message.content = content
        message.crtDate = Date()
        message.crtUserId = user.userId
        message.crtUserName = user.nickName
        message.type = MessageConstant.MESSAGE_TYPE_SYSTEM
        messageDao.insert(message)
        var targetUserSplit: Array<String>
        var targetMessage: TargetMessage
        // 插入接收者的数据
        for (targetUser in targetUsers) {
            targetUserSplit = targetUser.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            targetMessage = TargetMessage()
            targetMessage.messageId = message.messageId
            targetMessage.targetName = user.nickName
            targetMessage.removeState = MessageConstant.REMOVE_STATE_NORMAL
            targetMessage.userId= targetUserSplit[1]
            targetMessage.state = MessageConstant.READE_STATE_NOT_READE
            targetMessage.sendTime = Date()
            targetMessageDao.insert(targetMessage)
            socketIoService.pushMessage(targetUserSplit[0], content)
        }
        return ReturnInfo(SystemStaticConst.SUCCESS, "消息发布成功")
    }


    /**
     * 功能描述：获取消息列表
     *
     * @param search       模糊匹配消息的title和content
     * @param pageSize     每页显示的记录的条数
     * @param current      当前访问第几页
     * @param orderKey     排序字段
     * @param orderByValue 排序方式，降序还是升序
     * @return 返回查询结果
     */
    fun queryMessageList(search: String, pageSize: Int, current: Int, orderKey: String?, orderByValue: String?): ReturnInfo {
        PageHelper.startPage<Any>(current, if (pageSize in 0..500) pageSize else 20, if (!orderKey.isNullOrEmpty()) if (!orderByValue.isNullOrEmpty()) "$orderKey $orderByValue" else orderKey else "")
        val res = PageUtil.getResult(messageDao.queryMessageList(search))
        return ReturnInfo(SystemStaticConst.SUCCESS, "获取消息列表数据成功！", Page(pageSize, current, res.get("total") as Long, res.get("rows") as List<*>))
    }


}