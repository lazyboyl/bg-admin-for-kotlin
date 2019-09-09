package com.github.lazyboyl.vcm.kotlin.web.core.entity

import com.github.lazyboyl.vcm.kotlin.web.core.util.UuidGenId
import tk.mybatis.mapper.annotation.KeySql
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.Table

/**
 * 类描述： 用户角色关联的实体类
 * @author linzf
 * @since 2019-09-09
 */
@Table(name = "t_user_role")
class UserRole {

    /**
     * 用户角色关联流水ID
     */
    @Id
    @Column(name = "userRoleId")
    @KeySql(genId = UuidGenId::class)
    var userRoleId: String? = null

    /**
     * 用户ID
     */
    @Column(name = "userId")
    var userId: String? = null

    /**
     * 角色ID
     */
    @Column(name = "roleId")
    var roleId: String? = null

}