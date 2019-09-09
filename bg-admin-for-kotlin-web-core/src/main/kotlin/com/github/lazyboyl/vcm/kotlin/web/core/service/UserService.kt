package com.github.lazyboyl.vcm.kotlin.web.core.service

import com.github.lazyboyl.vcm.kotlin.web.core.auth.Auth
import com.github.lazyboyl.vcm.kotlin.web.core.constant.SystemStaticConst
import com.github.lazyboyl.vcm.kotlin.web.core.dao.*
import com.github.lazyboyl.vcm.kotlin.web.core.entity.*
import com.github.lazyboyl.vcm.kotlin.web.core.util.JsonUtils
import com.github.lazyboyl.vcm.kotlin.web.core.util.PageUtil
import com.github.lazyboyl.vcm.kotlin.web.core.util.RsaUtil
import com.github.pagehelper.PageHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

/**
 * 类描述：用户的service
 * @author linzf
 * @since 2019-09-09
 */
@Service
@Transactional(rollbackFor = [Exception::class])
class UserService {

    /**
     * 用户的dao
     */
    @Autowired
    lateinit var userDao: UserDao

    /**
     * 用户角色关联的dao
     */
    @Autowired
    lateinit var userRoleDao: UserRoleDao

    /**
     * 用户组织关联的dao
     */
    @Autowired
    lateinit var userOrgDao: UserOrgDao

    /**
     * 角色的dao
     */
    @Autowired
    lateinit var roleDao: RoleDao

    /**
     * 组织的dao
     */
    @Autowired
    lateinit var orgDao: OrgDao

    /**
     * 菜单的dao
     */
    @Autowired
    lateinit var treeDao: TreeDao

    /**
     * 权限的注入
     */
    @Autowired
    lateinit var authProvider: Auth

    /**
     * 私钥信息
     */
    @Value("\${auth.privateKey}")
    var privateKey: String = ""

    /**
     * 功能描述：根据token和旧的密码来更新新的密码
     *
     * @param token       登录的token
     * @param oldPassword 旧的密码
     * @param newPassword 新的密码
     * @return 返回更新结果
     */
    fun changePassword(token: String, oldPassword: String, newPassword: String): ReturnInfo {
        return if (userDao.changePassword(token, oldPassword, newPassword) > 0) {
            ReturnInfo(SystemStaticConst.SUCCESS, "更新用户密码成功")
        } else {
            ReturnInfo(SystemStaticConst.FAIL, "密码不正确，更新失败！")
        }
    }

    /**
     * 功能描述：删除用户
     *
     * @param userId 用户ID
     * @return 返回删除结果
     */
    fun deleteUser(userId: String): ReturnInfo {
        if (userDao.deleteByPrimaryKey(userId) > 0) {
            // 根据用户ID删除角色表和组织架构表的关联数据
            userRoleDao.deleteUserRoleByUserId(userId)
            userOrgDao.deleteUserOrgByUserId(userId)
            return ReturnInfo(SystemStaticConst.SUCCESS, "删除用户数据成功")
        } else {
            return ReturnInfo(SystemStaticConst.FAIL, "查无此用户数据")
        }
    }

    /**
     * 功能描述：更新用户
     *
     * @param userId       用户ID
     * @param nickName     真实姓名
     * @param headImg      头像地址
     * @param province     所在省
     * @param provinceName 所在省名称
     * @param city         所在市
     * @param cityName     所在市名称
     * @param area         所在区
     * @param areaName     所在区名称
     * @param address      地址
     * @param orgIds       组织架构ID
     * @param roles        角色集合
     * @return 返回数据新增的结果
     */
    fun updateUser(@RequestParam(name = "userId") userId: String,
                   @RequestParam(name = "nickName") nickName: String,
                   @RequestParam(name = "headImg") headImg: String,
                   @RequestParam(name = "province") province: String,
                   @RequestParam(name = "provinceName") provinceName: String,
                   @RequestParam(name = "city") city: String,
                   @RequestParam(name = "cityName") cityName: String,
                   @RequestParam(name = "area") area: String,
                   @RequestParam(name = "areaName") areaName: String,
                   @RequestParam(name = "address") address: String,
                   @RequestParam(name = "orgIds") orgIds: Array<String>,
                   @RequestParam(name = "roles") roles: Array<String>): ReturnInfo {
        val user = userDao.selectByPrimaryKey(userId) ?: return ReturnInfo(SystemStaticConst.FAIL, "查无此用户数据")
        user.nickName = nickName
        user.headImg = headImg
        user.crtDate = Date()
        user.province = province
        user.provinceName = provinceName
        user.city = city
        user.cityName = cityName
        user.area = area
        user.areaName = areaName
        user.address = address
        userDao.updateByPrimaryKey(user)
        // 根据用户ID删除角色表和组织架构表的关联数据
        userRoleDao.deleteUserRoleByUserId(userId)
        userOrgDao.deleteUserOrgByUserId(userId)
        // 增加用户和角色的关联的数据
        var userRole: UserRole
        for (role in roles) {
            userRole = UserRole()
            userRole.roleId = role
            userRole.userId = user.userId
            userRoleDao.insert(userRole)
        }
        val orgId = orgIds[orgIds.size - 1]
        val userOrg = UserOrg()
        userOrg.orgId = orgId.toInt()
        userOrg.userId = user.userId
        userOrgDao.insert(userOrg)
        return ReturnInfo(SystemStaticConst.SUCCESS, "用户更新成功")
    }


    /**
     * 功能描述：根据用户流水ID来获取用户数据
     *
     * @param userId 用户流水ID
     * @return 返回获取结果
     */
    fun getUserByUserId(userId: String): ReturnInfo {
        val user = userDao.selectByPrimaryKey(userId) ?: return ReturnInfo(SystemStaticConst.SUCCESS, "查无此用户数据")
        val result = JsonUtils.objToMap(user)
        val userRoleList = userRoleDao.getUserRoleByUserId(userId)
        val roleIds = arrayOfNulls<String>(userRoleList.size)
        for (i in userRoleList.indices) {
            roleIds[i] = userRoleList[i].roleId
        }
        result["roles"] = roleIds
        // 获取组织架构的数据数据
        result["orgIds"] = orgDao.getOrgByUserId(userId).fullPath!!.split("\\.")
        return ReturnInfo(SystemStaticConst.SUCCESS, "加载用户信息成功", result)
    }

    /**
     * 功能描述：加载所有的角色数据
     *
     * @return 返回加载结果
     */
    fun loadAllRole(): ReturnInfo {
        return ReturnInfo(SystemStaticConst.SUCCESS, "加载所有的角色成功", roleDao.selectAll())
    }

    /**
     * 功能描述：创建用户
     *
     * @param loginAccount 用户账号
     * @param nickName     真实姓名
     * @param headImg      头像地址
     * @param province     所在省
     * @param provinceName 所在省名称
     * @param city         所在市
     * @param cityName     所在市名称
     * @param area         所在区
     * @param areaName     所在区名称
     * @param address      地址
     * @param orgIds       组织架构ID
     * @param roles        角色集合
     * @return 返回数据新增的结果
     */
    fun createUser(loginAccount: String, nickName: String,
                   headImg: String, province: String,
                   provinceName: String, city: String,
                   cityName: String, area: String,
                   areaName: String, address: String,
                   orgIds: Array<String>, roles: Array<String>): ReturnInfo {
        if (userDao.checkLoginAccount(loginAccount) > 0) {
            return ReturnInfo(SystemStaticConst.SUCCESS, "账号已经存在，请修改以后再进行提交！")
        }
        val user = User()
        user.loginAccount = loginAccount
        user.nickName = nickName
        user.headImg = headImg
        user.loginPassword = "123456"
        user.crtDate = Date()
        user.province = province
        user.provinceName = provinceName
        user.city = city
        user.cityName = cityName
        user.area = area
        user.areaName = areaName
        user.address = address
        userDao.insert(user)
        // 增加用户和角色的关联的数据
        var userRole: UserRole
        for (role in roles) {
            userRole = UserRole()
            userRole.roleId = role
            userRole.userId = user.userId
            userRoleDao.insert(userRole)
        }
        val orgId = orgIds[orgIds.size - 1]
        val userOrg = UserOrg()
        userOrg.orgId = orgId.toInt()
        userOrg.userId = user.userId
        userOrgDao.insert(userOrg)
        return ReturnInfo(SystemStaticConst.SUCCESS, "用户创建成功")
    }

    /**
     * 功能描述：验证这个账户是否已经创建过了
     *
     * @param loginAccount 用户账号
     * @return 返回验证结果
     */
    fun checkLoginAccount(loginAccount: String): ReturnInfo {
        val result = HashMap<String, Any>(1)
        if (userDao.checkLoginAccount(loginAccount) > 0) {
            result["success"] = "unPass"
        } else {
            result["success"] = "pass"
        }
        return ReturnInfo(SystemStaticConst.SUCCESS, "验证请求发送成功", result)
    }

    /**
     * 功能描述：获取用户列表
     *
     * @param fullPath     父组织架构的匹配路径
     * @param search       根据账号和名称来模糊查询
     * @param pageSize     每页显示的记录的条数
     * @param current      当前访问第几页
     * @param orderKey     排序字段
     * @param orderByValue 排序方式，降序还是升序
     * @return 返回查询结果
     */
    fun queryUserList(fullPath: String, search: String, pageSize: Int, current: Int, orderKey: String?, orderByValue: String?): ReturnInfo {
        PageHelper.startPage<Any>(current, if (pageSize in 0..500) pageSize else 20, if (orderKey != null) if (orderByValue != null) "$orderKey $orderByValue" else orderKey else "")
        val res = PageUtil.getResult(userDao.queryUserList(search, fullPath))
        return ReturnInfo(SystemStaticConst.SUCCESS, "获取用户列表数据成功！", Page(pageSize, current, res.get("total") as Long, res.get("rows") as List<*>))
    }

    /**
     * 功能描述：实现重新刷新token
     *
     * @param refreshToken token的值
     * @return 返回刷新结果
     */
    fun refreshToken(refreshToken: String): ReturnInfo {
        return authProvider.refreshToken(refreshToken)
    }


    /**
     * 功能描述：实现用户登陆
     *
     * @param loginAccount  用户账号
     * @param loginPassword 用户密码
     * @return 返回登陆结果
     */
    fun login(loginAccount: String, loginPassword: String): ReturnInfo {
        return authProvider.login(loginAccount, RsaUtil.decrypt(loginPassword, privateKey))
    }

    /**
     * 功能描述：根据token来获取用户数据
     *
     * @param token token的值
     * @return 返回获取的结果
     */
    fun getUserInfo(token: String): ReturnInfo {
        val user = userDao.getUserInfo(token) ?: return ReturnInfo(SystemStaticConst.FAIL, "获取账号信息错误")
        user.loginPassword = null
        val treeList = treeDao.getLoginUserTree(user.userId)
        val access = arrayOfNulls<String>(treeList.size)
        for (i in treeList.indices) {
            access[i] = treeList[i].treeCode
        }
        val result = JsonUtils.objToMap(user)
        result.put("access", access)
        return ReturnInfo(SystemStaticConst.SUCCESS, "登陆成功", result)
    }

}