package com.github.lazyboyl.vcm.kotlin.web.core.dao

import com.github.lazyboyl.vcm.kotlin.web.core.entity.Role
import org.apache.ibatis.annotations.Param
import tk.mybatis.mapper.common.Mapper

/**
 * 类描述：角色的dao
 * @author linzf
 * @since 2019-09-05
 */
interface RoleDao : Mapper<Role> {

    /**
     * 功能描述：根据用户ID来获取该用户的相应的绑定的数据
     * @param userId 用户ID
     * @return 返回角色的集合
     */
    fun getUserRoleListByUserId(@Param("userId") userId: String): List<Role>

    /**
     * 功能描述：更新角色信息
     *
     * @param roleId   角色流水ID
     * @param roleName 角色名字
     * @param roleCode 角色编码
     * @return 返回操作结果
     */
    fun updateRole(@Param("roleId") roleId: String, @Param("roleName") roleName: String, @Param("roleCode") roleCode: String): Int

    /**
     * 功能描述：验证角色编码和角色名字是否重复
     *
     * @param roleId   角色ID
     * @param roleName 角色名字
     * @param roleCode 角色编码
     * @return 返回验证结果
     */
    fun checkRoleCodeAndName(@Param("roleId") roleId: String?, @Param("roleName") roleName: String?, @Param("roleCode") roleCode: String?): Int

    /**
     * 功能描述：获取角色列表数据
     *
     * @param search 模糊匹配角色的roleName和roleCode
     * @return 返回查询结果
     */
    fun queryRoleList(@Param("search") search: String): List<Role>

}