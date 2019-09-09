package com.github.lazyboyl.vcm.kotlin.web.core.controller

import com.github.lazyboyl.vcm.kotlin.web.core.entity.ReturnInfo
import com.github.lazyboyl.vcm.kotlin.web.core.service.BehaviorLogService
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * @author linzf
 * @since 2019/09/09
 * 类描述：行为日志的controller
 */
@RestController
@RequestMapping("/behaviorLog")
class BehaviorLogController {

    @Autowired
    lateinit var behaviorLogService: BehaviorLogService

    /**
     * 功能描述：获取行为日志列表
     *
     * @param search   模糊匹配日志的用户名字和响应方法
     * @param pageSize 每页显示的记录的条数
     * @param current  当前访问第几页
     * @param orderKey     排序字段
     * @param orderByValue 排序方式，降序还是升序
     * @return 返回查询结果
     */
    @ApiOperation(value = "获取行为日志列表")
    @PostMapping("queryBehaviorLogList")
    private fun queryBehaviorLogList(@RequestParam(name = "search", required = false) search: String,
                                     @RequestParam(name = "pageSize") pageSize: Int,
                                     @RequestParam(name = "current") current: Int,
                                     @RequestParam(name = "orderKey", required = false) orderKey: String,
                                     @RequestParam(name = "orderByValue", required = false) orderByValue: String): ReturnInfo {
        return behaviorLogService!!.queryBehaviorLogList(search, pageSize, current, orderKey, orderByValue)
    }

}