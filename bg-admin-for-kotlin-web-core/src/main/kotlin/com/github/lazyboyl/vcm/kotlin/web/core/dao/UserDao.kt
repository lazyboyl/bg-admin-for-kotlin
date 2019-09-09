package com.github.lazyboyl.vcm.kotlin.web.core.dao

import com.github.lazyboyl.vcm.kotlin.web.core.entity.User
import org.apache.ibatis.annotations.Param
import tk.mybatis.mapper.common.Mapper

/**
 * 类描述： 用户的dao
 * @author linzf
 * @since 2019-09-05
 */
interface UserDao : Mapper<User> {

    /**
     * 功能描述：根据token和旧的密码来更新新的密码
     * @param token 登录的token
     * @param oldPassword 旧的密码
     * @param newPassword 新的密码
     * @return 返回更新结果
     */
    fun changePassword(@Param("token") token: String, @Param("oldPassword") oldPassword: String, @Param("newPassword") newPassword: String): Int

    /**
     * 功能描述：根据token来获取用户数据
     * @param token token的值
     * @return 返回获取的结果
     */
    fun getUserInfo(@Param("token") token: String): User?

    /**
     * 功能描述：实现用户的登陆
     * @param loginAccount 用户账号
     * @param loginPassword 用户密码
     * @return 返回登陆结果
     */
    fun login(@Param("loginAccount") loginAccount: String, @Param("loginPassword") loginPassword: String): User?

    /**
     * 功能描述：验证这个账户是否已经创建过了
     * @param loginAccount 用户账号
     * @return 返回验证结果
     */
    fun checkLoginAccount(@Param("loginAccount") loginAccount: String): Int

    /**
     * 功能描述：查询用户的数据
     * @param search 根据账号和名称来模糊查询
     * @param fullPath 父组织架构的匹配路径
     * @return 返回查询结果
     */
    fun queryUserList(@Param("search") search: String, @Param("fullPath") fullPath: String): List<User>

}