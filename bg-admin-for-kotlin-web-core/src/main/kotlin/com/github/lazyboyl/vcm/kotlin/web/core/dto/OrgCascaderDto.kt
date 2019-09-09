package com.github.lazyboyl.vcm.kotlin.web.core.dto

class OrgCascaderDto {

    /**
     * 流水ID
     */
    var value: String? = null

    /**
     * 组织架构名称
     */
    var label: String? = null

    /**
     * 子节点数据
     */
    var children: List<OrgCascaderDto>? = null

}