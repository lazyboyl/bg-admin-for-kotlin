package com.github.lazyboyl.vcm.kotlin.web.core.entity

import com.github.lazyboyl.vcm.kotlin.web.core.util.UuidGenId
import tk.mybatis.mapper.annotation.KeySql
import java.util.*
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Transient

/**
 * 类描述： 消息的实体
 * @author linzf
 * @since 2019-09-09
 */
@Table(name = "t_message")
class Message {

    /**
     * 消息流水ID
     */
    @Id
    @Column(name = "messageId")
    @KeySql(genId = UuidGenId::class)
    var messageId: String? = null

    /**
     * 消息标题
     */
    @Column(name = "title")
    var title: String? = null

    /**
     * 消息内容
     */
    @Column(name = "content")
    var content: String? = null

    /**
     * 创建事件
     */
    @Column(name = "crtDate")
    var crtDate: Date? = null

    /**
     * 创建用户ID
     */
    @Column(name = "crtUserId")
    var crtUserId: String? = null

    /**
     * 创建用户名称
     */
    @Column(name = "crtUserName")
    var crtUserName: String? = null

    /**
     * 消息类型【1：系统消息；2：其他消息】
     */
    @Column(name = "type")
    var type: String? = null

    /**
     * 消息的接受者集合
     */
    @Transient
    var targetMessageList: List<TargetMessage>? = null

}