package com.github.lazyboyl.vcm.kotlin.web.core.dao

import com.github.lazyboyl.vcm.kotlin.web.core.entity.Org
import org.apache.ibatis.annotations.Param
import tk.mybatis.mapper.common.Mapper

/**
 * 类描述： 组织的dao
 * @author linzf
 * @since 2019-09-09
 */
interface OrgDao : Mapper<Org> {

    /**
     * 功能描述：根据用户ID来获取关联的组织架构的数据
     *
     * @param userId 用户流水ID
     * @return 返回获取的结果
     */
    fun getOrgByUserId(@Param("userId") userId: String): Org

    /**
     * 功能描述：判断当前组织架构底下是否还有子节点
     *
     * @param orgId 组织架构ID
     * @return 返回统计结果
     */
    fun countOrgChildren(@Param("orgId") orgId: Int?): Int

    /**
     * 功能描述：更新节点的路径
     *
     * @param orgId    组织架构ID
     * @param fullPath 组织架构完整路径
     * @return 返回更新结果
     */
    fun updateFullPath(@Param("orgId") orgId: Int?, @Param("fullPath") fullPath: String): Int

    /**
     * 功能描述：验证组织架构名字和编码是否存在
     *
     * @param orgId   组织架构ID
     * @param orgName 组织架构名称
     * @param orgCode 组织架构编码
     * @return 返回验证结果
     */
    fun checkOrgNameAndCode(@Param("orgId") orgId: Int?, @Param("orgName") orgName: String, @Param("orgCode") orgCode: String): Int

}