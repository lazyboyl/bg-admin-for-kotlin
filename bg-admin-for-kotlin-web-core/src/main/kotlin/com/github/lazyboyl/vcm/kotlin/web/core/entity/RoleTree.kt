package com.github.lazyboyl.vcm.kotlin.web.core.entity

import com.github.lazyboyl.vcm.kotlin.web.core.util.UuidGenId
import tk.mybatis.mapper.annotation.KeySql
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.Table

/**
 * 类描述： 角色菜单关联的实体类
 * @author linzf
 * @since 2019-09-04
 */
@Table(name = "t_role_tree")
class RoleTree(roleTreeId: String, treeId: Int) {

    /**
     * 角色菜单关联流水ID
     */
    @Id
    @Column(name = "roleTreeId")
    @KeySql(genId = UuidGenId::class)
    var roleTreeId: String? = roleTreeId

    /**
     * 菜单ID
     */
    @Column(name = "treeId")
    var treeId: Int? = treeId

    /**
     * 角色ID
     */
    @Column(name = "roleId")
    var roleId: String? = null

}