package com.github.lazyboyl.vcm.kotlin.web.core.dao

import com.github.lazyboyl.vcm.kotlin.web.core.entity.UserRole
import org.apache.ibatis.annotations.Param
import tk.mybatis.mapper.common.Mapper

/**
 * 类描述： 用户角色关联的dao
 * @author linzf
 * @since 2019-09-09
 */
interface UserRoleDao : Mapper<UserRole> {

    /**
     * 功能描述：根据用户ID来删除关联数据
     * @param userId 用户ID
     * @return 返回删除结果
     */
    fun deleteUserRoleByUserId(@Param("userId") userId: String): Int

    /**
     * 功能描述：根据用户ID来获取用户和角色的关联数据
     *
     * @param userId 用户ID
     * @return 返回查询结果
     */
    fun getUserRoleByUserId(@Param("userId") userId: String): List<UserRole>

}