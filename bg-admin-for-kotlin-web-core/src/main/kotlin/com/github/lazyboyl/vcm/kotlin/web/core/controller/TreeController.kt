package com.github.lazyboyl.vcm.kotlin.web.core.controller

import com.github.lazyboyl.vcm.kotlin.web.core.entity.ReturnInfo
import com.github.lazyboyl.vcm.kotlin.web.core.service.TreeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tree")
class TreeController {

    /**
     * 菜单的service
     */
    @Autowired
    lateinit var treeService:TreeService

    /**
     * 功能描述：获取菜单树目录
     *
     * @return 返回菜单目录数据
     */
    @PostMapping("getTreeList")
    fun getTreeList(): ReturnInfo {
        return treeService.getTreeList()
    }

}