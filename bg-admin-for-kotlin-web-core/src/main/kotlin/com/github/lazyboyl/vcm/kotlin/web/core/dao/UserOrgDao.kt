package com.github.lazyboyl.vcm.kotlin.web.core.dao

import com.github.lazyboyl.vcm.kotlin.web.core.entity.UserOrg
import org.apache.ibatis.annotations.Param
import tk.mybatis.mapper.common.Mapper

/**
 * 类描述：用户组织关联的dao
 * @author linzf
 * @since 2019-09-09
 */
interface UserOrgDao : Mapper<UserOrg> {

    /**
     * 功能描述：根据用户ID来删除关联数据
     * @param userId 用户ID
     * @return 返回删除结果
     */
    abstract fun deleteUserOrgByUserId(@Param("userId") userId: String): Int

    /**
     * 功能描述：根据组织架构ID来统计用户和组织架构的数量
     * @param orgId 组织架构流水ID
     * @return 返回删除结果
     */
    abstract fun countUserOrgByOrgId(@Param("orgId") orgId: Int?): Int

    /**
     * 功能描述：根据组织架构ID来删除关联关系的数据
     * @param orgId 组织架构流水ID
     * @return 返回删除结果
     */
    abstract fun deleteUserOrgByOrgId(@Param("orgId") orgId: Int?): Int


}