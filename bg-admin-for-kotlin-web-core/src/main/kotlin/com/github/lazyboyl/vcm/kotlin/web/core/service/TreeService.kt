package com.github.lazyboyl.vcm.kotlin.web.core.service

import com.github.lazyboyl.vcm.kotlin.web.core.constant.SystemStaticConst
import com.github.lazyboyl.vcm.kotlin.web.core.constant.TreeStaticConstant
import com.github.lazyboyl.vcm.kotlin.web.core.dao.RoleTreeDao
import com.github.lazyboyl.vcm.kotlin.web.core.dao.TreeDao
import com.github.lazyboyl.vcm.kotlin.web.core.entity.Page
import com.github.lazyboyl.vcm.kotlin.web.core.entity.ReturnInfo
import com.github.lazyboyl.vcm.kotlin.web.core.entity.Tree
import com.github.lazyboyl.vcm.kotlin.web.core.mapper.TreeMapper
import com.github.lazyboyl.vcm.kotlin.web.core.util.PageUtil
import com.github.lazyboyl.vcm.kotlin.web.core.util.TreeInstall
import com.github.pagehelper.PageHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import java.util.*

/**
 * 类描述：菜单的service
 * @author linzf
 * @since 2019-09-04
 */
@Service
@Transactional(rollbackFor = [Exception::class])
class TreeService {

    /**
     * 菜单的dao
     */
    @Autowired
    lateinit var treeDao: TreeDao

    /**
     * 角色菜单关联的dao
     */
    @Autowired
    lateinit var roleTreeDao: RoleTreeDao

    /**
     * 菜单的实体转换
     */
    @Autowired
    lateinit var treeMapper: TreeMapper



    /**
     * 功能描述：根据菜单流水ID来更新菜单数据
     * @param treeId 菜单流水ID
     * @param treeCode 菜单编码
     * @param treeName 菜单名称
     * @param powerPath 权限集合
     * @return 返回更新结果
     */
    fun updateTree(treeId: Int?, treeCode: String, treeName: String, powerPath: String): ReturnInfo {
        return if (treeDao.updateTree(treeId, treeCode, treeName, powerPath) > 0) {
            ReturnInfo(SystemStaticConst.SUCCESS, "更新菜单节点成功")
        } else {
            ReturnInfo(SystemStaticConst.FAIL, "更新菜单节点失败")
        }
    }

    /**
     * 功能描述：根据菜单ID来获取菜单数据
     * @param treeId 菜单ID
     * @return 返回操作结果
     */
    fun getTreeByTreeId(treeId: Int): ReturnInfo {
        val tree = treeDao.selectByPrimaryKey(treeId) ?: return ReturnInfo(SystemStaticConst.FAIL, "查无此菜单数据")
        return ReturnInfo(SystemStaticConst.SUCCESS, "获取菜单节点成功", tree)
    }

    /**
     * 功能描述：删除菜单节点
     * @param treeId 菜单节点ID
     * @return 返回删除结果
     */
    fun deleteTree(treeId: Int?): ReturnInfo {
        if (treeDao.countTreeChildren(treeId) > 0) {
            return ReturnInfo(SystemStaticConst.FAIL, "当前菜单节点底下还有其他节点，请先删除底下的节点以后再来删除当前节点")
        }
        return if (treeDao.deleteByPrimaryKey(treeId) > 0) {
            roleTreeDao.deleteRoleTreeByTreeId(treeId)
            ReturnInfo(SystemStaticConst.SUCCESS, "删除菜单节点成功")
        } else {
            ReturnInfo(SystemStaticConst.FAIL, "删除菜单节点失败")
        }

    }


    /**
     * 功能描述：冻结/解冻按钮
     * @param treeId 节点ID
     * @param treeState 节点状态
     * @return 返回操作结果
     */
    fun operateButton(treeId: Int, treeState: String): ReturnInfo {
        return if (treeDao.operateButton(treeId, treeState) > 0) {
            ReturnInfo(SystemStaticConst.SUCCESS, "操作成功")
        } else {
            ReturnInfo(SystemStaticConst.FAIL, "操作失败")
        }
    }

    /**
     * 功能描述：增加按钮
     * @param treeCode 按钮编码
     * @param treeName 按钮名称
     * @param powerPath 权限集合
     * @param parentTreeId 父菜单节点ID
     * @param treeType 节点类型
     * @return 返回增加结果
     */
    fun addButton(treeCode: String, treeName: String, powerPath: String, parentTreeId: Int, treeType: String): ReturnInfo {
        val fullPath = StringBuilder()
        val tree = Tree(treeCode = treeCode, treeName = treeName, treeType = treeType, powerPath = powerPath, treeState = TreeStaticConstant.TREE_STATE_NORMAL, parentTreeId = parentTreeId, crtDate = Date(), parentTreeName = "")
        if (parentTreeId == 0) {
            tree.parentTreeName = ""
        } else {
            val parentTree = treeDao.selectByPrimaryKey(parentTreeId)
                    ?: return ReturnInfo(SystemStaticConst.FAIL, "增加按钮失败，不存在当前父菜单")
            tree.parentTreeName = parentTree.treeName
            fullPath.append(parentTree.fullPath)
        }
        return if (treeDao.insert(tree) > 0) {
            fullPath.append(".").append(tree.treeId)
            treeDao.updateFullPath(fullPath.toString(), tree.treeId)
            ReturnInfo(SystemStaticConst.SUCCESS, "增加按钮成功", tree)
        } else {
            ReturnInfo(SystemStaticConst.FAIL, "增加按钮失败")
        }
    }

    /**
     * 功能描述：验证菜单节点是否已经增加过
     * @param treeId 菜单流水ID
     * @param treeCode 菜单编码
     * @return 返回验证结果
     */
    fun checkTreeCode(treeId: Int?, treeCode: String): ReturnInfo {
        return if (treeDao.checkTreeCode(treeCode, treeId) > 0) {
            ReturnInfo(SystemStaticConst.SUCCESS, "该编码已经存在了，请修改以后再提交！", mutableMapOf(TreeStaticConstant.CHECK_TREE_TIP to TreeStaticConstant.CHECK_TREE_UN_PASS))
        } else {
            ReturnInfo(SystemStaticConst.SUCCESS, "验证通过！", mutableMapOf(TreeStaticConstant.CHECK_TREE_TIP to TreeStaticConstant.CHECK_TREE_PASS))
        }
    }

    /**
     * 功能描述：更新按钮节点
     * @param treeId 按钮节点ID
     * @param treeName 按钮名字
     * @param treeCode 按钮编码
     * @param powerPath 权限集合
     * @return 返回更新操作结果
     */
    fun updateButton(treeId: Int, treeName: String, treeCode: String, powerPath: String?): ReturnInfo {
        if (treeDao.checkTreeCode(treeCode, treeId) > 0) {
            return ReturnInfo(SystemStaticConst.FAIL, "该编码已经存在了，请修改以后再提交！")
        }
        return if (treeDao.updateButton(treeCode, treeName, treeId, powerPath) > 0) {
            ReturnInfo(SystemStaticConst.SUCCESS, "更新按钮节点成功！")
        } else {
            ReturnInfo(SystemStaticConst.FAIL, "更新按钮节点失败！")
        }
    }

    /**
     * 功能描述：删除按钮节点
     * @param treeId 按钮的ID
     * @return 返回删除结果
     */
    fun deleteButton(treeId: Int): ReturnInfo {
        return if (treeDao.deleteByPrimaryKey(treeId) > 0) {
            roleTreeDao.deleteRoleTreeByTreeId(treeId)
            ReturnInfo(SystemStaticConst.SUCCESS, "删除按钮节点成功")
        } else {
            ReturnInfo(SystemStaticConst.FAIL, "删除按钮节点失败")
        }
    }

    /**
     * 功能描述：获取菜单树目录
     * @return 返回菜单目录数据
     */
    @PostMapping("getTreeList")
    fun getTreeList(): ReturnInfo {
        return ReturnInfo(SystemStaticConst.SUCCESS, "加载菜单数据成功",  TreeInstall.installTree(treeMapper.treesToTreeDTO(treeDao.select(Tree(TreeStaticConstant.TREE_STATE_NORMAL,TreeStaticConstant.TREE_TYPE_MENU)))))
    }

    /**
     * 功能描述：获取菜单的按钮的列表
     *
     * @param search   模糊匹配菜单的treeName，treeCode 允许为空
     * @param pageSize 每页显示的记录的条数
     * @param parentTreeId 父节点id
     * @param current  当前访问第几页
     * @param orderKey     排序字段
     * @param orderByValue 排序方式，降序还是升序
     * @return 返回查询结果
     */
    fun queryTreeButtonList(search: String, parentTreeId: Int?, pageSize: Int, current: Int, orderKey: String?, orderByValue: String?): ReturnInfo {
        PageHelper.startPage<Any>(current, if (pageSize in 0..500) pageSize else 20, if (orderKey != null) if (orderByValue != null) "$orderKey $orderByValue" else orderKey else "")
        val res = PageUtil.getResult(treeDao.queryTreeButtonList(search, parentTreeId))
        return ReturnInfo(SystemStaticConst.SUCCESS, "获取按钮列表数据成功！", Page(pageSize = pageSize, current = current, total = res["total"] as Long, rows = res["rows"] as List<*>))
    }


}