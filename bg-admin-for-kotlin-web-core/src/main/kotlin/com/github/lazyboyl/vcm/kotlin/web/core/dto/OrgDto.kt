package com.github.lazyboyl.vcm.kotlin.web.core.dto

class OrgDto {

    /**
     * 流水ID
     */
    var orgId: Int? = null

    /**
     * 菜单名字
     */
    var title: String? = null

    /**
     * 组织编码
     */
    var orgCode: String? = null

    /**
     * 父流水ID
     */
    var parentOrgId: Int? = null

    /**
     * 父组织架构名字
     */
    var parentOrgName: String? = null

    /**
     * 菜单流水完整路径(1.2.3)
     */
    var fullPath: String? = null

    /**
     * 默认子节点都是展开的
     */
    var expand = true
    /**
     * 默认节点是没有选中的
     */
    var checked = false

    /**
     * 子节点数据
     */
    var children: List<OrgDto>? = null

}