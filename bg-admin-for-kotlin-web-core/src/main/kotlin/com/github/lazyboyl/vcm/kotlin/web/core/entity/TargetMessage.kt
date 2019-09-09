package com.github.lazyboyl.vcm.kotlin.web.core.entity

import com.github.lazyboyl.vcm.kotlin.web.core.util.UuidGenId
import tk.mybatis.mapper.annotation.KeySql
import java.util.*
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.Table

/**
 * 类描述： 消息接收的实体类
 * @author linzf
 * @since 2019-09-09
 */
@Table(name = "t_target_message")
class TargetMessage {

    /**
     * 流水ID
     */
    @Id
    @Column(name = "targetMessageId")
    @KeySql(genId = UuidGenId::class)
    var targetMessageId: String? = null

    /**
     * 消息流水ID
     */
    @Column(name = "messageId")
    var messageId: String? = null

    /**
     * 消息状态【1：未读；2：已读】
     */
    @Column(name = "state")
    var state: String? = null

    /**
     * 用户ID
     */
    @Column(name = "userId")
    var userId: String? = null

    /**
     * 发送时间
     */
    @Column(name = "sendTime")
    var sendTime: Date? = null

    /**
     * 接收人姓名
     */
    @Column(name = "targetName")
    var targetName: String? = null

    /**
     * 阅读时间
     */
    @Column(name = "readeTime")
    var readeTime: Date? = null

    /**
     * 删除状态【0：已删除；1：正常】
     */
    @Column(name = "removeState")
    var removeState: String? = null

}