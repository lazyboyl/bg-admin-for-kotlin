package com.github.lazyboyl.vcm.kotlin.web.core.entity

class Page constructor(pageSize: Int, current: Int, total: Long, rows: List<*>) {

    /**
     * 每页显示多少条数据
     */
    var pageSize: Int = pageSize
    /**
     * 当前第几页
     */
    var current: Int = current
    /**
     * 总记录数
     */
    var total: Long = total
    /**
     * 集合数据
     */
    var rows: List<*>? = rows

}