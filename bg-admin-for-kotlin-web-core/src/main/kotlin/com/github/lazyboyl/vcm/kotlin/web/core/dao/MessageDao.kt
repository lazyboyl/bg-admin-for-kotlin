package com.github.lazyboyl.vcm.kotlin.web.core.dao

import com.github.lazyboyl.vcm.kotlin.web.core.entity.Message
import org.apache.ibatis.annotations.Param
import tk.mybatis.mapper.common.Mapper

/**
 * 类描述：消息的dao
 * @author linzf
 * @since 2019-09-09
 */
interface MessageDao : Mapper<Message> {

    /**
     * 功能描述：获取当前登录的用户的未读的消息
     * @param userId 用户ID
     * @return 返回消息数据
     */
     fun queryUserMsg(@Param("userId") userId: String): List<Message>

    /**
     * 功能描述：查询消息数据
     * @param search 匹配的内容
     * @return 返回查询的结果
     */
     fun queryMessageList(@Param("search") search: String): List<Message>


}