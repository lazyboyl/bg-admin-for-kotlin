package com.github.lazyboyl.vcm.kotlin.web.core.controller

import com.github.lazyboyl.vcm.kotlin.web.core.entity.ReturnInfo
import com.github.lazyboyl.vcm.kotlin.web.core.service.UserService
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * @author linzf
 * @since 2019/09/09
 * 类描述：
 */
@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    lateinit var userService: UserService

    /**
     * 功能描述：根据token和旧的密码来更新新的密码
     *
     * @param oldPassword 旧的密码
     * @param newPassword 新的密码
     * @return 返回更新结果
     */
    @ApiOperation(value = "根据token和旧的密码来更新新的密码")
    @PostMapping("changePassword")
    fun changePassword(@RequestParam(name = "oldPassword") oldPassword: String,
                       @RequestParam(name = "newPassword") newPassword: String): ReturnInfo {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        val token = request.getHeader("x-access-token")
        return userService.changePassword(token, oldPassword, newPassword)
    }

    /**
     * 功能描述：删除用户
     *
     * @param userId 用户ID
     * @return 返回删除结果
     */
    @ApiOperation(value = "删除用户")
    @PostMapping("deleteUser")
    fun deleteUser(@RequestParam(name = "userId") userId: String): ReturnInfo {
        return userService.deleteUser(userId)
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
    @ApiOperation(value = "更新用户")
    @PostMapping("updateUser")
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
        return userService.updateUser(userId, nickName, headImg, province, provinceName, city, cityName, area, areaName, address, orgIds, roles)
    }

    /**
     * 功能描述：根据用户流水ID来获取用户数据
     *
     * @param userId 用户流水ID
     * @return 返回获取结果
     */
    @ApiOperation(value = "根据用户流水ID来获取用户数据")
    @PostMapping("getUserByUserId")
    fun getUserByUserId(@RequestParam(name = "userId") userId: String): ReturnInfo {
        return userService.getUserByUserId(userId)
    }

    /**
     * 功能描述：加载所有的角色数据
     *
     * @return 返回加载结果
     */
    @ApiOperation(value = "加载所有的角色数据")
    @PostMapping("loadAllRole")
    fun loadAllRole(): ReturnInfo {
        return userService.loadAllRole()
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
    @ApiOperation(value = "创建用户")
    @PostMapping("createUser")
    fun createUser(@RequestParam(name = "loginAccount") loginAccount: String,
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
        return userService.createUser(loginAccount, nickName, headImg, province, provinceName, city, cityName, area, areaName, address, orgIds, roles)
    }

    /**
     * 功能描述：验证这个账户是否已经创建过了
     *
     * @param loginAccount 用户账号
     * @return 返回验证结果
     */
    @ApiOperation(value = "验证这个账户是否已经创建过了")
    @PostMapping("checkLoginAccount")
    fun checkLoginAccount(@RequestParam(name = "loginAccount") loginAccount: String): ReturnInfo {
        return userService.checkLoginAccount(loginAccount)
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
    @ApiOperation(value = "获取用户列表")
    @PostMapping("queryUserList")
    fun queryUserList(@RequestParam(name = "fullPath") fullPath: String, @RequestParam(name = "search") search: String,
                      @RequestParam(name = "pageSize") pageSize: Int, @RequestParam(name = "current") current: Int,
                      @RequestParam(name = "orderKey") orderKey: String, @RequestParam(name = "orderByValue") orderByValue: String): ReturnInfo {
        return userService.queryUserList(fullPath, search, pageSize, current, orderKey, orderByValue)
    }

    /**
     * 功能描述：实现重新刷新token
     *
     * @param refreshToken token的值
     * @return 返回刷新结果
     */
    @ApiOperation(value = "实现刷新用户的token")
    @PostMapping("refreshToken")
    fun refreshToken(@RequestParam(name = "refreshToken") refreshToken: String): ReturnInfo {
        return userService.refreshToken(refreshToken)
    }

    /**
     * 功能描述：实现用户登陆
     *
     * @param loginAccount  用户账号
     * @param loginPassword 用户密码
     * @return 返回登陆结果
     */
    @ApiOperation(value = "实现用户登陆")
    @PostMapping("login")
    fun login(@RequestParam(name = "loginAccount") loginAccount: String, @RequestParam(name = "loginPassword") loginPassword: String): ReturnInfo {
        return userService.login(loginAccount, loginPassword)
    }


    /**
     * 功能描述：根据token来获取用户数据
     *
     * @param token token的值
     * @return 返回获取的结果
     */
    @ApiOperation(value = "根据token来获取用户数据")
    @PostMapping("getUserInfo")
    fun getUserInfo(@RequestParam(name = "token") token: String): ReturnInfo {
        return userService.getUserInfo(token)
    }


}