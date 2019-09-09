package com.github.lazyboyl.vcm.kotlin.web.core.entity

import com.github.lazyboyl.vcm.kotlin.web.core.util.UuidGenId
import tk.mybatis.mapper.annotation.KeySql
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.Table

/**
 * 类描述： 用户组织关联的实体类
 * @author linzf
 * @since 2019-09-09
 */
@Table(name = "t_user_org")
class UserOrg {

    /**
     * 用户组织关联流水ID
     */
    @Id
    @Column(name = "userOrgId")
    @KeySql(genId = UuidGenId::class)
    var userOrgId: String? = null

    /**
     * 用户流水ID
     */
    @Column(name = "userId")
    var userId: String? = null

    /**
     * 组织流水ID
     */
    @Column(name = "orgId")
    var orgId: Int? = null

}