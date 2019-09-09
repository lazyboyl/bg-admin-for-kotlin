package com.github.lazyboyl.vcm.kotlin.web.core.service

import com.github.lazyboyl.vcm.kotlin.web.core.constant.SystemStaticConst
import com.github.lazyboyl.vcm.kotlin.web.core.dao.OrgDao
import com.github.lazyboyl.vcm.kotlin.web.core.dao.UserOrgDao
import com.github.lazyboyl.vcm.kotlin.web.core.entity.Org
import com.github.lazyboyl.vcm.kotlin.web.core.entity.ReturnInfo
import com.github.lazyboyl.vcm.kotlin.web.core.mapper.OrgMapper
import com.github.lazyboyl.vcm.kotlin.web.core.util.OrgInstall
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * 类描述：数据字典的service
 * @author linzf
 * @since 2019-09-09
 */
@Service
@Transactional(rollbackFor = [Exception::class])
class OrgService {

    /**
     * 组织的dao
     */
    @Autowired
    lateinit var orgDao: OrgDao

    /**
     * 用户组织关联的dao
     */
    @Autowired
    lateinit var userOrgDao: UserOrgDao

    /**
     * 组织的mapper
     */
    @Autowired
    lateinit var orgMapper: OrgMapper

    /**
     * 功能描述：获取组织架构信息
     * @param orgId 组织架构流水ID
     * @return 返回获取结果
     */
    fun getOrgByOrgId(orgId: Int?): ReturnInfo {
        val org = orgDao.selectByPrimaryKey(orgId) ?: return ReturnInfo(SystemStaticConst.FAIL, "查无此组织架构数据，请重新刷新页面！")
        return ReturnInfo(SystemStaticConst.SUCCESS, "获取组织架构数据成功", org)
    }

    /**
     * 功能描述：删除组织架构
     *
     * @param orgId 组织架构流水ID
     * @return 返回删除结果
     */
    fun deleteOrg(orgId: Int?): ReturnInfo {
        orgDao.selectByPrimaryKey(orgId) ?: return ReturnInfo(SystemStaticConst.FAIL, "删除组织架构数据失败，查无此组织架构数据！")
        if (orgDao.countOrgChildren(orgId) > 0) {
            return ReturnInfo(SystemStaticConst.FAIL, "删除组织架构数据失败，当前组织架构底下还有子组织，请先删除子组织！")
        }
        if (userOrgDao.countUserOrgByOrgId(orgId) > 0) {
            return ReturnInfo(SystemStaticConst.FAIL, "删除组织架构数据失败，当前组织架构底下还有用户，请先删除该组织架构底下的用户！")
        }
        if (orgDao.deleteByPrimaryKey(orgId) > 0) {
            userOrgDao.deleteUserOrgByOrgId(orgId)
            return ReturnInfo(SystemStaticConst.SUCCESS, "删除组织架构成功！")
        } else {
            return ReturnInfo(SystemStaticConst.FAIL, "删除组织架构数据失败，查无此组织架构数据！")
        }
    }

    /**
     * 功能描述：更新组织架构的数据
     *
     * @param orgId   组织架构流水ID
     * @param orgName 组织架构名字
     * @param orgCode 组织架构编码
     * @return 返回更新结果
     */
    fun updateOrg(orgId: Int?, orgName: String, orgCode: String): ReturnInfo {
        if (orgDao.checkOrgNameAndCode(orgId, orgName, "") > 0) {
            return ReturnInfo(SystemStaticConst.FAIL, "当前组织架构名称已经存在，请重新修改以后再提交！")
        }
        if (orgDao.checkOrgNameAndCode(orgId, "", orgCode) > 0) {
            return ReturnInfo(SystemStaticConst.FAIL, "当前组织架构编码已经存在，请重新修改以后再提交！")
        }
        val org = orgDao.selectByPrimaryKey(orgId) ?: return ReturnInfo(SystemStaticConst.FAIL, "查无此组织架构数据，请重新刷新页面！")
        org.orgName = orgName
        org.orgCode = orgCode
        return if (orgDao.updateByPrimaryKey(org) > 0) {
            ReturnInfo(SystemStaticConst.SUCCESS, "更新组织架构数据成功")
        } else {
            ReturnInfo(SystemStaticConst.FAIL, "更新组织架构失败")
        }
    }

    /**
     * 功能描述：增加组织架构
     *
     * @param orgName     组织架构名称
     * @param orgCode     组织架构编码
     * @param parentOrgId 父节点ID
     * @return
     */
    fun addOrg(orgName: String, orgCode: String, parentOrgId: Int?): ReturnInfo {
        val fullPath = StringBuilder()
        val org = Org()
        if (parentOrgId!!.toInt() == 0) {
            org.parentOrgName = ""
        } else {
            val parent = orgDao.selectByPrimaryKey(parentOrgId)
                    ?: return ReturnInfo(SystemStaticConst.FAIL, "查无此父组织架构数据，请重新选择父组织架构！")
            org.parentOrgName = parent.orgName
            fullPath.append(parent.fullPath)
        }
        org.orgName = orgName
        org.orgCode = orgCode
        org.crtDate = Date()
        org.parentOrgId = parentOrgId
        if (orgDao.insert(org) > 0) {
            fullPath.append(".").append(org.orgId)
            orgDao.updateFullPath(org.orgId, fullPath.toString())
            return ReturnInfo(SystemStaticConst.SUCCESS, "增加组织架构成功", org)
        } else {
            return ReturnInfo(SystemStaticConst.FAIL, "增加组织架构失败")
        }

    }

    /**
     * 功能描述：验证组织架构名字和编码是否存在
     *
     * @param orgId   组织架构ID
     * @param orgName 组织架构名称
     * @param orgCode 组织架构编码
     * @return 返回验证结果
     */
    fun checkOrgNameAndCode(orgId: Int?, orgName: String, orgCode: String): ReturnInfo {
        val result = HashMap<String, Any>(1)
        try {
            if (orgDao.checkOrgNameAndCode(orgId, orgName, orgCode) > 0) {
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
     * 功能描述：获取组织架构树目录
     *
     * @return 返回获取的结果
     */
    fun getOrgTree(): ReturnInfo {
        return ReturnInfo(SystemStaticConst.SUCCESS, "加载组织架构数据成功", OrgInstall.installOrg(orgMapper.orgsToOrgDto(orgDao.selectAll())))
    }

    /**
     * 功能描述：获取组织架构的Cascader的数据
     * @return 返回获取结果
     */
    fun getOrgCascader(): ReturnInfo {
        return ReturnInfo(SystemStaticConst.SUCCESS, "加载级联菜单的组织架构数据成功", OrgInstall.installOrgCascader(orgDao.selectAll()))
    }


}