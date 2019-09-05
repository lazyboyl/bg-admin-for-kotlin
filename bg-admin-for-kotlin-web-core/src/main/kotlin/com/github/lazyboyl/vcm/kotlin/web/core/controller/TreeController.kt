package com.github.lazyboyl.vcm.kotlin.web.core.controller

import com.github.lazyboyl.vcm.kotlin.web.core.constant.TreeStaticConstant
import com.github.lazyboyl.vcm.kotlin.web.core.entity.ReturnInfo
import com.github.lazyboyl.vcm.kotlin.web.core.entity.User
import com.github.lazyboyl.vcm.kotlin.web.core.service.TreeService
import com.github.lazyboyl.vcm.kotlin.web.core.util.RedisCache
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tree")
class TreeController {

    /**
     * 菜单的service
     */
    @Autowired
    lateinit var treeService: TreeService


    @Autowired
    lateinit var redisCache: RedisCache

    @ApiOperation(value = "测试redis")
    @PostMapping("testRedis")
    fun testRedis(): String {
        redisCache.setString("testRedis", "中文啊实打实")
        println("-------" + redisCache.getString("testRedis"))
        val user = User();
        user.address = "阿打算"
        user.nickName = "大大"
        redisCache.setObject("testObject", user)
        val u = redisCache.getObject("testObject", User::class.java)
        println(u!!.address)
        return "1212"
    }

    /**
     * 功能描述：根据菜单流水ID来更新菜单数据
     *
     * @param treeId   菜单流水ID
     * @param treeCode 菜单编码
     * @param treeName 菜单名称
     * @param powerPath 权限集合
     * @return 返回更新结果
     */
    @ApiOperation(value = "根据菜单流水ID来更新菜单数据")
    @PostMapping("updateTree")
    fun updateTree(@RequestParam(name = "treeId") treeId: Int?,
                   @RequestParam(name = "treeCode") treeCode: String,
                   @RequestParam(name = "treeName") treeName: String,
                   @RequestParam(name = "powerPath") powerPath: String): ReturnInfo {
        return treeService.updateTree(treeId, treeCode, treeName, powerPath)
    }


    /**
     * 功能描述：根据菜单ID来获取菜单数据
     *
     * @param treeId 菜单ID
     * @return 返回操作结果
     */
    @ApiOperation(value = "根据菜单ID来获取菜单数据")
    @PostMapping("getTreeByTreeId")
    fun getTreeByTreeId(@RequestParam(name = "treeId") treeId: Int): ReturnInfo {
        return treeService.getTreeByTreeId(treeId)
    }

    /**
     * 功能描述：删除菜单节点
     *
     * @param treeId 菜单节点ID
     * @return 返回删除结果
     */
    @ApiOperation(value = "删除菜单")
    @PostMapping("deleteTree")
    fun deleteTree(@RequestParam(name = "treeId") treeId: Int?): ReturnInfo {
        return treeService.deleteTree(treeId)
    }

    /**
     * 功能描述：增加菜单
     *
     * @param treeCode     菜单编码
     * @param treeName     菜单名称
     * @param parentTreeId 父菜单节点ID
     * @param powerPath    权限集合
     * @return 返回增加结果
     */
    @ApiOperation(value = "增加菜单")
    @PostMapping("addTree")
    fun addTree(@RequestParam(name = "treeCode") treeCode: String,
                @RequestParam(name = "treeName") treeName: String,
                @RequestParam(name = "powerPath") powerPath: String,
                @RequestParam(name = "parentTreeId") parentTreeId: Int): ReturnInfo {
        return treeService.addButton(treeCode, treeName, powerPath, parentTreeId, TreeStaticConstant.TREE_TYPE_MENU)
    }

    /**
     * 功能描述：冻结/解冻按钮
     *
     * @param treeId    节点ID
     * @param treeState 节点状态
     * @return 返回操作结果
     */
    @ApiOperation(value = "冻结/解冻按钮")
    @PostMapping("operateButton")
    fun operateButton(@RequestParam(name = "treeId") treeId: Int, @RequestParam(name = "treeState") treeState: String): ReturnInfo {
        return treeService.operateButton(treeId, treeState)
    }

    /**
     * 功能描述：增加按钮
     *
     * @param treeCode     按钮编码
     * @param treeName     按钮名称
     * @param parentTreeId 父菜单节点ID
     * @return 返回增加结果
     */
    @ApiOperation(value = "增加按钮")
    @PostMapping("addButton")
    fun addButton(@RequestParam(name = "treeCode") treeCode: String,
                  @RequestParam(name = "treeName") treeName: String,
                  @RequestParam(name = "powerPath") powerPath: String,
                  @RequestParam(name = "parentTreeId") parentTreeId: Int): ReturnInfo {
        return treeService.addButton(treeCode, treeName, powerPath, parentTreeId, TreeStaticConstant.TREE_TYPE_BUTTON)
    }

    /**
     * 功能描述：验证菜单节点是否已经增加过
     *
     * @param treeId   菜单流水ID
     * @param treeCode 菜单编码
     * @return 返回验证结果
     */
    @ApiOperation(value = "验证菜单节点是否已经增加过")
    @PostMapping("checkTreeCode")
    fun checkTreeCode(@RequestParam(name = "treeId", required = false) treeId: Int?, @RequestParam(name = "treeCode") treeCode: String): ReturnInfo {
        return treeService.checkTreeCode(treeId, treeCode)
    }

    /**
     * 功能描述：更新按钮节点
     *
     * @param treeId   按钮节点ID
     * @param treeName 按钮名字
     * @param treeCode 按钮编码
     * @param powerPath 权限集合
     * @return
     */
    @ApiOperation(value = "更新按钮节点")
    @PostMapping("updateButton")
    fun updateButton(@RequestParam(name = "treeId") treeId: Int,
                     @RequestParam(name = "treeName") treeName: String,
                     @RequestParam(name = "treeCode") treeCode: String,
                     @RequestParam(name = "powerPath") powerPath: String): ReturnInfo {
        return treeService.updateButton(treeId, treeName, treeCode, powerPath)
    }

    /**
     * 功能描述：删除按钮节点
     *
     * @param treeId 按钮的ID
     * @return 返回删除结果
     */
    @ApiOperation(value = "删除按钮节点")
    @PostMapping("deleteButton")
    fun deleteButton(@RequestParam(name = "treeId") treeId: Int): ReturnInfo {
        return treeService.deleteButton(treeId)
    }

    /**
     * 功能描述：获取菜单的按钮的列表
     *
     * @param search       模糊匹配菜单的treeName，treeCode 允许为空
     * @param pageSize     每页显示的记录的条数
     * @param current      当前访问第几页
     * @param orderKey     排序字段
     * @param orderByValue 排序方式，降序还是升序
     * @return 返回查询结果
     */
    @ApiOperation(value = "获取菜单的按钮的列表")
    @PostMapping("queryTreeButtonList")
    fun queryTreeButtonList(@RequestParam(name = "search", required = false) search: String, @RequestParam(name = "parentTreeId") parentTreeId: Int, @RequestParam(name = "pageSize") pageSize: Int, @RequestParam(name = "current") current: Int, @RequestParam(name = "orderKey", required = false) orderKey: String, @RequestParam(name = "orderByValue", required = false) orderByValue: String): ReturnInfo {
        return treeService.queryTreeButtonList(search = search, parentTreeId = parentTreeId, pageSize = pageSize, current = current, orderKey = orderKey, orderByValue = orderByValue)
    }

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