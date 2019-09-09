package com.github.lazyboyl.vcm.kotlin.web.core.service

import com.github.lazyboyl.vcm.kotlin.web.core.constant.RoleStaticConstant
import com.github.lazyboyl.vcm.kotlin.web.core.constant.SystemStaticConst
import com.github.lazyboyl.vcm.kotlin.web.core.constant.TreeStaticConstant
import com.github.lazyboyl.vcm.kotlin.web.core.dao.RoleDao
import com.github.lazyboyl.vcm.kotlin.web.core.dao.RoleTreeDao
import com.github.lazyboyl.vcm.kotlin.web.core.dao.TreeDao
import com.github.lazyboyl.vcm.kotlin.web.core.dto.TreeDto
import com.github.lazyboyl.vcm.kotlin.web.core.entity.*
import com.github.lazyboyl.vcm.kotlin.web.core.mapper.TreeMapper
import com.github.lazyboyl.vcm.kotlin.web.core.util.JsonUtils
import com.github.lazyboyl.vcm.kotlin.web.core.util.PageUtil
import com.github.lazyboyl.vcm.kotlin.web.core.util.TreeInstall
import com.github.pagehelper.PageHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(rollbackFor = [Exception::class])
class RoleService {

    /**
     * 角色的dao
     */
    @Autowired
    lateinit var roleDao: RoleDao

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
     * tree的转换mapper
     */
    @Autowired
    lateinit var treeMapper: TreeMapper

    /**
     * 功能描述：加载菜单节点的数据
     * @return 返回加载结果
     */
    fun loadTree(): ReturnInfo {
        val tree = Tree()
        tree.treeState = TreeStaticConstant.TREE_STATE_NORMAL
        return ReturnInfo(SystemStaticConst.SUCCESS, "加载菜单数据成功", TreeInstall.installTree(treeMapper.treesToTreeDTO(treeDao.select(tree))))
    }

    /**
     * 功能描述：删除角色数据
     *
     * @param roleId 角色流水ID
     * @return 返回删除结果
     */
    fun deleteRole(roleId: String): ReturnInfo {
        try {
            if (roleDao.deleteByPrimaryKey(roleId) > 0) {
                // 删除角色的关联数据
                roleTreeDao.deleteRoleTreeByRoleId(roleId)
                return ReturnInfo(SystemStaticConst.SUCCESS, "删除角色数据成功")
            }
            return ReturnInfo(SystemStaticConst.FAIL, "删除角色数据失败！失败原因：该角色数据不存在")
        } catch (e: Exception) {
            return ReturnInfo(SystemStaticConst.FAIL, "删除角色数据失败！失败原因：" + e.message)
        }

    }

    /**
     * 功能描述：更新角色数据
     *
     * @param roleId   角色流水ID
     * @param roleName 角色名字
     * @param roleCode 角色编码
     * @param roleTrees 角色菜单关联的数据
     * @return 返回操作结果
     */
    fun updateRole(roleId: String, roleName: String, roleCode: String, roleTrees: String?): ReturnInfo {
        try {
            if (roleDao.checkRoleCodeAndName(roleId, roleName, "") > 0) {
                return ReturnInfo(SystemStaticConst.FAIL, "角色名字已经存在，请修改以后再提交！")
            }
            if (roleDao.checkRoleCodeAndName(roleId, "", roleCode) > 0) {
                return ReturnInfo(SystemStaticConst.FAIL, "角色编码已经存在，请修改以后再提交！")
            }
            if (roleDao.updateRole(roleId, roleName, roleCode) > 0) {
                if (roleTrees != null) {
                    // 删除关联表的数据
                    roleTreeDao.deleteRoleTreeByRoleId(roleId)
                    // 重新插入新的关联数据
                    saveRoleAssociateTree(JsonUtils.jsonToList(roleTrees, TreeDto::class.java), Role(roleId))
                }
                return ReturnInfo(SystemStaticConst.SUCCESS, "更新角色数据成功")
            }
            return ReturnInfo(SystemStaticConst.FAIL, "更新角色数据失败！失败原因：查无此角色数据")
        } catch (e: Exception) {
            return ReturnInfo(SystemStaticConst.FAIL, "更新角色数据失败！失败原因：" + e.message)
        }

    }

    /**
     * 功能描述：获取角色信息
     *
     * @param roleId 角色流水ID
     * @return 返回操作结果
     */
    fun getRoleByRoleId(roleId: String): ReturnInfo {
        try {
            val role = roleDao.selectByPrimaryKey(roleId)
            if (role != null) {
                val treeList = treeDao.queryTreeByRoleId(role.roleId!!)
                val allTree = treeDao.selectAll()
                val treeMap = HashMap<String, Any>(3)
                for (tree in treeList) {
                    treeMap[tree.treeId.toString()] = tree
                }
                val result = JsonUtils.objToMap(role)
                result.put(RoleStaticConstant.ROLE_TREE_LIST_NAME, TreeInstall.installCheckTree(treeMapper.treesToTreeDTO(allTree), treeMap))
                return ReturnInfo(SystemStaticConst.SUCCESS, "获取角色数据成功", result)
            }
        } catch (e: Exception) {
            return ReturnInfo(SystemStaticConst.FAIL, "获取角色数据失败！失败原因：" + e.message)
        }

        return ReturnInfo(SystemStaticConst.FAIL, "获取角色数据失败！失败原因：查无此角色数据")
    }

    /**
     * 功能描述：实现增加角色数据
     *
     * @param role 角色实体数据
     * @return 返回操作结果
     */
    fun addRole(role: Role): ReturnInfo {
        role.crtDate = Date()
        try {
            if (roleDao.checkRoleCodeAndName("", role.roleName!!, "") > 0) {
                return ReturnInfo(SystemStaticConst.FAIL, "角色名字已经存在，请修改以后再提交！")
            }
            if (roleDao.checkRoleCodeAndName("", "", role.roleCode!!) > 0) {
                return ReturnInfo(SystemStaticConst.FAIL, "角色编码已经存在，请修改以后再提交！")
            }
            roleDao.insert(role)
            saveRoleAssociateTree(JsonUtils.jsonToList(role.roleTrees!!, TreeDto::class.java), role)
        } catch (e: Exception) {
            return ReturnInfo(SystemStaticConst.FAIL, "增加角色失败，失败原因：" + e.message)
        }
        return ReturnInfo(SystemStaticConst.SUCCESS, "增加加角色成功", role)
    }

    /**
     * 功能描述：验证角色编码和角色名字是否重复
     *
     * @param roleId   角色流水ID
     * @param roleName 角色名字
     * @param roleCode 角色编码
     * @return 返回处理结果
     */
    fun checkRoleCodeAndName(roleId: String, roleName: String, roleCode: String): ReturnInfo {
        val result = HashMap<String, Any>(1)
        try {
            // 查询的结果大于0表示数据库已经存在该数据了，反之则不存在
            if (roleDao.checkRoleCodeAndName(roleId, roleName, roleCode) > 0) {
                result["success"] = "unPass"
            } else {
                result["success"] = "pass"
            }
        } catch (e: Exception) {
            return ReturnInfo(SystemStaticConst.FAIL, "验证请求处理失败，失败原因：" + e.message)
        }
        return ReturnInfo(SystemStaticConst.SUCCESS, "验证请求处理成功", result)
    }

    /**
     * 功能描述：获取角色列表数据
     *
     * @param search       模糊匹配角色的roleName和roleCode
     * @param pageSize     每页显示的记录的条数
     * @param current      当前访问第几页
     * @param orderKey     排序字段
     * @param orderByValue 排序方式，降序还是升序
     * @return 返回查询结果
     */
    fun queryRoleList(search: String, pageSize: Int, current: Int, orderKey: String?, orderByValue: String?): ReturnInfo {
        PageHelper.startPage<Any>(current, if (pageSize in 0..500) pageSize else 20, if (orderKey != null) if (orderByValue != null) "$orderKey $orderByValue" else orderKey else "")
        val res = PageUtil.getResult(roleDao.queryRoleList(search))
        return ReturnInfo(SystemStaticConst.SUCCESS, "获取角色列表数据成功！", Page(pageSize, current, res["total"] as Long, res["rows"] as List<*>))
    }

    /**
     * 功能描述：保存角色和菜单的关联关系的数据
     * @param treeDtoList 菜单节点数据
     * @param entity 角色实体
     * @return 返回是否有子节点被选中
     */
    private fun saveRoleAssociateTree(treeDtoList: List<TreeDto>, entity: Role): Boolean {
        var hasChildrenChecked = false
        for (treeDto in treeDtoList) {
            if (treeDto.children!!.isNotEmpty()) {
                if (saveRoleAssociateTree(treeDto.children!!, entity) || treeDto.checked) {
                    roleTreeDao.insert(RoleTree(entity.roleId!!, treeDto.treeId!!))
                    hasChildrenChecked = true
                }
            } else {
                if (treeDto.checked) {
                    roleTreeDao.insert(RoleTree(entity.roleId!!, treeDto.treeId!!))
                    hasChildrenChecked = true
                }
            }
        }
        return hasChildrenChecked
    }

}