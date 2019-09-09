package com.github.lazyboyl.vcm.kotlin.web.core.controller

import com.github.lazyboyl.vcm.kotlin.web.core.entity.ReturnInfo
import com.github.lazyboyl.vcm.kotlin.web.core.entity.Role
import com.github.lazyboyl.vcm.kotlin.web.core.service.RoleService
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * @author linzf
 * @since 2019/09/09
 * 类描述：角色管理的controller
 */
@RestController
@RequestMapping("/role")
class RoleController {

    @Autowired
    lateinit var roleService: RoleService

    /**
     * 功能描述：加载菜单节点的数据
     * @return
     */
    @ApiOperation(value = "加载菜单节点的数据")
    @PostMapping("loadTree")
    fun loadTree(): ReturnInfo {
        return roleService.loadTree()
    }

    /**
     * 功能描述：删除角色数据
     * @param roleId 角色流水ID
     * @return 返回删除结果
     */
    @ApiOperation(value = "删除角色数据")
    @PostMapping("deleteRole")
    fun deleteRole(@RequestParam(name = "roleId") roleId: String): ReturnInfo {
        return roleService.deleteRole(roleId)
    }

    /**
     * 功能描述：更新角色数据
     * @param roleId 角色流水ID
     * @param roleName 角色名字
     * @param roleCode 角色编码
     * @param roleTrees 角色关联的菜单数据
     * @return 返回操作结果
     */
    @ApiOperation(value = "更新角色数据")
    @PostMapping("updateRole")
    fun updateRole(@RequestParam(name = "roleId") roleId: String, @RequestParam(name = "roleName") roleName: String, @RequestParam(name = "roleCode") roleCode: String, @RequestParam(name = "roleTrees", required = false) roleTrees: String): ReturnInfo {
        return roleService.updateRole(roleId, roleName, roleCode, roleTrees)
    }

    /**
     * 功能描述：获取角色信息
     * @param roleId 角色流水ID
     * @return 返回操作结果
     */
    @ApiOperation(value = "获取角色信息")
    @PostMapping("getRoleByRoleId")
    fun getRoleByRoleId(@RequestParam(name = "roleId") roleId: String): ReturnInfo {
        return roleService.getRoleByRoleId(roleId)
    }

    /**
     * 功能描述：实现增加角色
     * @param role 角色实体数据
     * @return 返回操作结果
     */
    @ApiOperation(value = "增加角色")
    @PostMapping("addRole")
    fun addRole(role: Role): ReturnInfo {
        return roleService.addRole(role)
    }

    /**
     * 功能描述：验证角色编码和角色名字是否重复
     *
     * @param roleId   角色流水ID
     * @param roleName 角色名字
     * @param roleCode 角色编码
     * @return 返回处理结果
     */
    @ApiOperation(value = "验证角色编码和角色名字是否重复")
    @PostMapping("checkRoleCodeAndName")
    fun checkRoleCodeAndName(@RequestParam(name = "roleId", required = false) roleId: String?, @RequestParam(name = "roleName", required = false) roleName: String?, @RequestParam(name = "roleCode", required = false) roleCode: String?): ReturnInfo {
        return roleService.checkRoleCodeAndName(roleId, roleName, roleCode)
    }

    /**
     * 功能描述：获取角色列表数据
     *
     * @param search   模糊匹配角色的roleName和roleCode
     * @param pageSize 每页显示的记录的条数
     * @param current  当前访问第几页
     * @param orderKey 排序字段
     * @param orderByValue 排序方式，降序还是升序
     * @return 返回查询结果
     */
    @ApiOperation(value = "获取角色列表数据")
    @PostMapping("queryRoleList")
    fun queryRoleList(@RequestParam(name = "search", required = false) search: String, @RequestParam(name = "pageSize") pageSize: Int, @RequestParam(name = "current") current: Int, @RequestParam(name = "orderKey", required = false) orderKey: String, @RequestParam(name = "orderByValue", required = false) orderByValue: String): ReturnInfo {
        return roleService.queryRoleList(search, pageSize, current, orderKey, orderByValue)
    }

}