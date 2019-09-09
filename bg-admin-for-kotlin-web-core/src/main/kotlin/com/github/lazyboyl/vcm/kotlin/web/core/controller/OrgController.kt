package com.github.lazyboyl.vcm.kotlin.web.core.controller

import com.github.lazyboyl.vcm.kotlin.web.core.entity.ReturnInfo
import com.github.lazyboyl.vcm.kotlin.web.core.service.OrgService
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * @author linzf
 * @since 2019-09-09
 * 类描述：组织架构的controller
 */
@RestController
@RequestMapping("/org")
class OrgController {

    @Autowired
    lateinit var orgService: OrgService

    /**
     * 功能描述：获取组织架构信息
     * @param orgId 组织架构流水ID
     * @return 返回获取结果
     */
    @ApiOperation(value = "获取组织架构信息")
    @PostMapping("getOrgByOrgId")
    fun getOrgByOrgId(@RequestParam(name = "orgId") orgId: Int?): ReturnInfo {
        return orgService.getOrgByOrgId(orgId)
    }

    /**
     * 功能描述：删除组织架构
     *
     * @param orgId 组织架构流水ID
     * @return 返回删除结果
     */
    @ApiOperation(value = "删除组织架构")
    @PostMapping("deleteOrg")
    fun deleteOrg(@RequestParam(name = "orgId") orgId: Int?): ReturnInfo {
        return orgService.deleteOrg(orgId)
    }

    /**
     * 功能描述：更新组织架构的数据
     *
     * @param orgId   组织架构流水ID
     * @param orgName 组织架构名字
     * @param orgCode 组织架构编码
     * @return 返回更新结果
     */
    @ApiOperation(value = "更新组织架构的数据")
    @PostMapping("updateOrg")
    fun updateOrg(@RequestParam(name = "orgId") orgId: Int?,
                  @RequestParam(name = "orgName") orgName: String,
                  @RequestParam(name = "orgCode") orgCode: String): ReturnInfo {
        return orgService.updateOrg(orgId, orgName, orgCode)
    }

    /**
     * 功能描述：增加组织架构
     *
     * @param orgName     组织架构名称
     * @param orgCode     组织架构编码
     * @param parentOrgId 父节点ID
     * @return 返回增加组织架构的结果
     */
    @ApiOperation(value = "增加组织架构")
    @PostMapping("addOrg")
    fun addOrg(@RequestParam(name = "orgName") orgName: String,
               @RequestParam(name = "orgCode") orgCode: String,
               @RequestParam(name = "parentOrgId") parentOrgId: Int?): ReturnInfo {
        return orgService.addOrg(orgName, orgCode, parentOrgId)
    }

    /**
     * 功能描述：验证组织架构名字和编码是否存在
     *
     * @param orgId   组织架构ID
     * @param orgName 组织架构名称
     * @param orgCode 组织架构编码
     * @return 返回验证结果
     */
    @ApiOperation(value = "验证组织架构名字和编码是否存在")
    @PostMapping("checkOrgNameAndCode")
    fun checkOrgNameAndCode(@RequestParam(name = "orgId", required = false) orgId: Int?,
                            @RequestParam(name = "orgName", required = false) orgName: String,
                            @RequestParam(name = "orgCode", required = false) orgCode: String): ReturnInfo {
        return orgService.checkOrgNameAndCode(orgId, orgName, orgCode)
    }


    /**
     * 功能描述：获取组织架构树目录
     *
     * @return 返回获取的结果
     */
    @ApiOperation(value = "获取组织架构树目录")
    @PostMapping("getOrgTree")
    fun getOrgTree(): ReturnInfo {
        return orgService.getOrgTree()
    }


    /**
     * 功能描述：获取组织架构的Cascader的数据
     * @return 返回获取结果
     */
    @ApiOperation(value = "获取组织架构的Cascader的数据")
    @PostMapping("getOrgCascader")
    fun getOrgCascader(): ReturnInfo {
        return orgService.getOrgCascader()
    }

}