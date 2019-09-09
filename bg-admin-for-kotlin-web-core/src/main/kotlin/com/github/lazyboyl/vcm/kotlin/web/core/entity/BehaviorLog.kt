package com.github.lazyboyl.vcm.kotlin.web.core.entity

import java.util.*
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

/**
 * 类描述： 操作日志的类
 * @author linzf
 * @since 2019-09-09
 */
@Table(name = "t_behavior_log")
class BehaviorLog {

    /**
     * 行为日志流水ID
     */
    @Id
    @Column(name = "behaviorLogId")
    @GeneratedValue(generator = "JDBC")
    var behaviorLogId: Long? = null

    /**
     * 响应方法
     */
    @Column(name = "actionMethod")
    var actionMethod: String? = null

    /**
     * 请求时间
     */
    @Column(name = "actionDate")
    var actionDate: Date? = null

    /**
     * 请求人
     */
    @Column(name = "actionUser")
    var actionUser: String? = null

    /**
     * 请求人ID
     */
    @Column(name = "actionUserId")
    var actionUserId: String? = null

}