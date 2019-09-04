package com.github.lazyboyl.vcm.kotlin.web.core.dto

data class TreeDto(var treeId: Int?,
                   var title: String?,
                   var treeCode: String?,
                   var treeState: String?,
                   var treeType: String?,
                   var parentTreeId: Int?,
                   var parentTreeName: String?,
                   var expand: Boolean = true,
                   var checked: Boolean = false,
                   var children: List<TreeDto>?) {

    constructor() : this(null, null, null, null, null, null, null, true, false, null)

}