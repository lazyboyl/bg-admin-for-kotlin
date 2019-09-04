package com.github.lazyboyl.vcm.kotlin.web.core.dao

import com.github.lazyboyl.vcm.kotlin.web.core.entity.Tree
import org.apache.ibatis.annotations.Param
import tk.mybatis.mapper.common.Mapper

/**
 * 类描述： 菜单的dao
 * @author linzf
 * @since 2019-09-04
 */
interface TreeDao : Mapper<Tree> {
    /**
     * 功能描述：根据用户登录的ID来获取权限数据的集合
     * @param userId 用户ID
     * @return 返回数据集合
     */
    fun getLoginUserPowerPath(@Param("userId") userId: String): List<Tree>

    /**
     * 功能描述：根据用户登录的ID来获取菜单节点的数据
     * @param userId 用户ID
     * @return 返回数据集合
     */
    fun getLoginUserTree(@Param("userId") userId: String): List<Tree>

    /**
     * 功能描述：根据菜单流水ID来更新菜单数据
     * @param treeId 菜单流水ID
     * @param treeCode 菜单编码
     * @param treeName 菜单名称
     * @param powerPath 权限集合
     * @return 返回更新结果
     */
    fun updateTree(@Param("treeId") treeId: Int?,
                   @Param("treeCode") treeCode: String,
                   @Param("treeName") treeName: String,
                   @Param("powerPath") powerPath: String): Int

    /**
     * 功能描述：统计当前菜单节点底下是否还有子节点
     * @param treeId 菜单流水ID
     * @return 返回统计结果
     */
    fun countTreeChildren(@Param("treeId") treeId: Int?): Int

    /**
     * 功能描述：冻结/解冻按钮
     * @param treeId 节点ID
     * @param treeState 节点状态
     * @return 返回操作结果
     */
    fun operateButton(@Param("treeId") treeId: Int?, @Param("treeState") treeState: String): Int

    /**
     * 功能描述：更新节点的路径
     * @param fullPath 节点路径
     * @param treeId  节点ID
     * @return 返回更新节点路径结果
     */
    fun updateFullPath(@Param("fullPath") fullPath: String, @Param("treeId") treeId: Int?): Int

    /**
     * 功能描述：更新按钮节点
     * @param treeCode 节点编码
     * @param treeName 节点名称
     * @param treeId 节点ID
     * @param powerPath 权限集合
     * @return 返回更新结果
     */
    fun updateButton(@Param("treeCode") treeCode: String,
                     @Param("treeName") treeName: String,
                     @Param("treeId") treeId: Int,
                     @Param("powerPath") powerPath: String?): Int

    /**
     * 功能描述：验证菜单节点或者按钮节点的编码是否已经存在了
     * @param treeCode 节点编码
     * @param treeId 节点ID
     * @return 返回验证结果
     */
    fun checkTreeCode(@Param("treeCode") treeCode: String, @Param("treeId") treeId: Int?): Int

    /**
     * 功能描述：获取菜单的按钮的列表
     * @param search 模糊查询treeName和treeCode的数据
     * @param parentTreeId 父节点ID
     * @return 返回该父节点底下的当级的按钮的数据
     */
    fun queryTreeButtonList(@Param("search") search: String, @Param("parentTreeId") parentTreeId: Int?): List<Tree>

    /**
     * 功能描述：根据角色ID来查询该角色底下的所有的关联的菜单的数据
     *
     * @param roleId 角色ID
     * @return 返回查询结果
     */
    fun queryTreeByRoleId(@Param("roleId") roleId: String): List<Tree>
}