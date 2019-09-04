package com.github.lazyboyl.vcm.kotlin.web.core.dao

import com.github.lazyboyl.vcm.kotlin.web.core.entity.RoleTree
import org.apache.ibatis.annotations.Param
import tk.mybatis.mapper.common.Mapper

/**
 * 类描述： 角色菜单关联的dao
 * @author linzf
 * @since 2019-09-04
 */
interface RoleTreeDao : Mapper<RoleTree> {

    /**
     * 功能描述：根据菜单ID删除关联关系
     * @param treeId 菜单ID
     * @return 返回删除结果
     */
    fun deleteRoleTreeByTreeId(@Param("treeId") treeId: Int?): Int

    /**
     * 功能描述：根据角色ID来删除角色菜单关联数据
     * @param roleId 角色ID
     * @return 返回删除结果
     */
    fun deleteRoleTreeByRoleId(@Param("roleId") roleId: String): Int

}