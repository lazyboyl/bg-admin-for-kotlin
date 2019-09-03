package com.github.lazyboyl.vcm.kotlin.web.core.util

import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import java.util.HashMap

class PageUtil {

    companion object {

        fun startPage(reqMap: Map<String, String>) {
            val page = Integer.parseInt((reqMap as java.util.Map<String, String>).getOrDefault("page", "1"))
            val size = Integer.parseInt((reqMap as java.util.Map<String, String>).getOrDefault("size", "20"))
            PageHelper.startPage<Any>(page, if (size > 0 && size <= 500) size else 20)
        }

        fun getResult(result: List<*>): HashMap<String, Any> {
            val pageInfo = PageInfo(result)
            val res = HashMap<String, Any>(4)
            res["page"] = pageInfo.pageNum
            res["size"] = pageInfo.pageSize
            res["total"] = pageInfo.total
            res["rows"] = pageInfo.list
            return res
        }

        fun startPageObject(reqMap: Map<String, Any>) {
            val page = Integer.parseInt((reqMap as java.util.Map<String, Any>).getOrDefault("page", "1").toString())
            val size = Integer.parseInt((reqMap as java.util.Map<String, Any>).getOrDefault("size", "20").toString())
            PageHelper.startPage<Any>(page, if (size > 0 && size <= 500) size else 20)
        }
    }



}