package com.github.lazyboyl.vcm.kotlin.web.core.dao

import com.github.lazyboyl.vcm.kotlin.web.core.entity.TargetMessage
import org.apache.ibatis.annotations.Param
import tk.mybatis.mapper.common.Mapper
import java.util.*

/**
 * 类描述： 消息接收者的dao
 * @since 2019-09-09
 * @author linzf
 */
interface TargetMessageDao : Mapper<TargetMessage> {

    /**
     * 功能描述：实现消息的阅读
     * @param state 消息的状态
     * @param readeTime 阅读时间
     * @param targetMessageId 消息关联流水ID
     * @return 返回更新结果
     */
    fun readMsg(@Param("state") state: String,
                @Param("readeTime") readeTime: Date,
                @Param("targetMessageId") targetMessageId: String): Int


}