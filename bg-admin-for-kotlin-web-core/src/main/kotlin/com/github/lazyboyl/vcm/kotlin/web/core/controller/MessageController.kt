package com.github.lazyboyl.vcm.kotlin.web.core.controller

import com.github.lazyboyl.vcm.kotlin.web.core.entity.ReturnInfo
import com.github.lazyboyl.vcm.kotlin.web.core.service.MessageService
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * @author linzf
 * @since 2019/09/09
 * 类描述：消息的controller
 */
@RestController
@RequestMapping("/msg")
class MessageController {

    @Autowired
    lateinit var messageService: MessageService

    /**
     * 功能描述：实现消息的阅读
     * @param targetMessageId 消息关联流水ID
     * @return 返回更新结果
     */
    @ApiOperation(value = "实现消息的阅读")
    @PostMapping("readMsg")
    fun readMsg(@RequestParam(name = "targetMessageId") targetMessageId: String): ReturnInfo {
        return messageService.readMsg(targetMessageId)
    }

    /**
     * 功能描述：获取当前登录的用户的未读的消息
     * @return 返回消息数据
     */
    @ApiOperation(value = "获取当前登录的用户的未读的消息")
    @PostMapping("queryUserMsg")
    fun queryUserMsg(): ReturnInfo {
        return messageService.queryUserMsg()
    }

    /**
     * 功能描述：实现消息的发布
     * @param title 消息标题
     * @param content 消息内容
     * @param targetUsers 接收用户ID
     * @return 返回发布结果
     */
    @ApiOperation(value = "消息的发布")
    @PostMapping("publishNews")
    fun publishNews(@RequestParam(name = "title") title: String,
                    @RequestParam(name = "content") content: String,
                    @RequestParam(name = "targetUsers") targetUsers: Array<String>): ReturnInfo {
        return messageService.publishNews(title, content, targetUsers)
    }

    /**
     * 功能描述：获取消息列表
     *
     * @param search   模糊匹配消息的title和content
     * @param pageSize 每页显示的记录的条数
     * @param current  当前访问第几页
     * @param orderKey     排序字段
     * @param orderByValue 排序方式，降序还是升序
     * @return 返回查询结果
     */
    @ApiOperation(value = "获取消息列表")
    @PostMapping("queryMessageList")
    fun queryMessageList(@RequestParam(name = "search", required = false) search: String,
                         @RequestParam(name = "pageSize") pageSize: Int,
                         @RequestParam(name = "current") current: Int,
                         @RequestParam(name = "orderKey", required = false) orderKey: String,
                         @RequestParam(name = "orderByValue", required = false) orderByValue: String): ReturnInfo {
        return messageService.queryMessageList(search, pageSize, current, orderKey, orderByValue)
    }


}