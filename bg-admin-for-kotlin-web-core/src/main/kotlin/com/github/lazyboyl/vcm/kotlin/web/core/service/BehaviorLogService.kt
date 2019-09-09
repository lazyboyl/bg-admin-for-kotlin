package com.github.lazyboyl.vcm.kotlin.web.core.service

import com.github.lazyboyl.vcm.kotlin.web.core.constant.SystemStaticConst
import com.github.lazyboyl.vcm.kotlin.web.core.dao.BehaviorLogDao
import com.github.lazyboyl.vcm.kotlin.web.core.entity.Page
import com.github.lazyboyl.vcm.kotlin.web.core.entity.ReturnInfo
import com.github.lazyboyl.vcm.kotlin.web.core.util.PageUtil
import com.github.pagehelper.PageHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 类描述：行为日志的service
 * @author linzf
 * @since 2019-09-09
 */
@Service
@Transactional(rollbackFor = [Exception::class])
class BehaviorLogService {

    /**
     * 行为日志的dao
     */
    @Autowired
    lateinit var behaviorLogDao: BehaviorLogDao

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
    fun queryBehaviorLogList(search: String, pageSize: Int, current: Int, orderKey: String?, orderByValue: String?): ReturnInfo {
        PageHelper.startPage<Any>(current, if (pageSize in 0..500) pageSize else 20, if (orderKey != null) if (orderByValue != null) "$orderKey $orderByValue" else orderKey else "")
        val res = PageUtil.getResult(behaviorLogDao.queryBehaviorLogList(search))
        return ReturnInfo(SystemStaticConst.SUCCESS, "获取日志列表数据成功！", Page(pageSize, current, res.get("total") as Long, res.get("rows") as List<*>))
    }


}