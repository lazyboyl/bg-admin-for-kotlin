package com.github.lazyboyl.vcm.kotlin.web.core.entity

import com.github.lazyboyl.vcm.kotlin.web.core.util.UuidGenId
import tk.mybatis.mapper.annotation.KeySql
import java.util.*
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.Table
import java.io.Serializable

/**
 * 类描述： 用户的实体类
 * @since 2019-09-05
 * @since 2019-09-05
 */
@Table(name = "t_user")
class User : Serializable {

    private val serialVersionUID = -5809782578282943998L

    /**
     * 用户流水ID
     */
    @Id
    @Column(name = "userId")
    @KeySql(genId = UuidGenId::class)
    var userId: String? = null

    /**
     * 用户登录账号
     */
    @Column(name = "loginAccount")
    var loginAccount: String? = null

    /**
     * 用户登录密码
     */
    @Column(name = "loginPassword")
    var loginPassword: String? = null

    /**
     * 创建时间
     */
    @Column(name = "crtDate")
    var crtDate: Date? = null

    /**
     * 真实姓名
     */
    @Column(name = "nickName")
    var nickName: String? = null

    /**
     * 最后登录时间
     */
    @Column(name = "lastLoginDate")
    var lastLoginDate: Date? = null

    /**
     * 登录的token
     */
    @Column(name = "token")
    var token: String? = null

    /**
     * 用户头像图片地址
     */
    @Column(name = "headImg")
    var headImg: String? = null

    /**
     * 用户所在省
     */
    @Column(name = "province")
    var province: String? = null

    /**
     * 用户所在省名称
     */
    @Column(name = "provinceName")
    var provinceName: String? = null

    /**
     * 用户所在市
     */
    @Column(name = "city")
    var city: String? = null

    /**
     * 用户所在市名称
     */
    @Column(name = "cityName")
    var cityName: String? = null

    /**
     * 用户所在区
     */
    @Column(name = "area")
    var area: String? = null

    /**
     * 用户所在区名称
     */
    @Column(name = "areaName")
    var areaName: String? = null

    /**
     * 具体地址
     */
    @Column(name = "address")
    var address: String? = null

}