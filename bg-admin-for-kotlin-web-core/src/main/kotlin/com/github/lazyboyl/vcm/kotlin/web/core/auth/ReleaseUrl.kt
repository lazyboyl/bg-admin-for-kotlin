package com.github.lazyboyl.vcm.kotlin.web.core.auth

/**
 * @author linzf
 * @since 2019-09-05
 * 类描述：放行的用户权限的地址
 */
interface ReleaseUrl {

    /**
     * 功能描述：获取放行的权限的数据
     * @return 返回放行的权限的数据
     */
    fun getReleaseUrl(): String

}