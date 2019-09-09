package com.github.lazyboyl.vcm.kotlin.web.core.entity

import com.github.lazyboyl.vcm.kotlin.web.core.util.UuidGenId
import tk.mybatis.mapper.annotation.KeySql
import java.util.*
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Transient

@Table(name = "t_role")
class Role {

    /**
     * 角色流水ID
     */
    @Id
    @Column(name = "roleId")
    @KeySql(genId = UuidGenId::class)
    var roleId: String? = null

    /**
     * 角色名字
     */
    @Column(name = "roleName")
    var roleName: String? = null

    /**
     * 角色编码
     */
    @Column(name = "roleCode")
    var roleCode: String? = null

    /**
     * 创建时间
     */
    @Column(name = "crtDate")
    var crtDate: Date? = null


    @Transient
    var roleTrees: String? = null

}