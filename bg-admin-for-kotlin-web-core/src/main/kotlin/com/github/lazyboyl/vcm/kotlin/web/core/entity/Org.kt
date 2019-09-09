package com.github.lazyboyl.vcm.kotlin.web.core.entity

import java.util.*
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

/**
 * 类描述： 组织架构的类
 * @author linzf
 * @since 2019-09-09
 */
@Table(name = "t_org")
class Org {

    /**
     * 流水ID
     */
    @Id
    @Column(name = "orgId")
    @GeneratedValue(generator = "JDBC")
    var orgId: Int? = null

    /**
     * 组织名字
     */
    @Column(name = "orgName")
    var orgName: String? = null

    /**
     * 组织编码
     */
    @Column(name = "orgCode")
    var orgCode: String? = null

    /**
     * 父流水ID
     */
    @Column(name = "parentOrgId")
    var parentOrgId: Int? = null

    /**
     * 父组织架构名字
     */
    @Column(name = "parentOrgName")
    var parentOrgName: String? = null

    /**
     * 创建时间
     */
    @Column(name = "crtDate")
    var crtDate: Date? = null

    /**
     * 菜单流水完整路径(1.2.3)
     */
    @Column(name = "fullPath")
    var fullPath: String? = null

}