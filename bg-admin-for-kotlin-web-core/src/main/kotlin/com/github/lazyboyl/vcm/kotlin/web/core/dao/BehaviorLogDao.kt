package com.github.lazyboyl.vcm.kotlin.web.core.dao

import com.github.lazyboyl.vcm.kotlin.web.core.entity.BehaviorLog
import org.apache.ibatis.annotations.Param
import tk.mybatis.mapper.common.Mapper

/**
 * 类描述：日志的dao
 * @author linzf
 * @since 2019-09-09
 */
interface BehaviorLogDao : Mapper<BehaviorLog> {

    /**
     * 功能描述：获取行为日志列表
     *
     * @param search 模糊匹配日志的用户名字和响应方法
     * @return 返回查询结果
     */
     fun queryBehaviorLogList(@Param("search") search: String): List<BehaviorLog>

}