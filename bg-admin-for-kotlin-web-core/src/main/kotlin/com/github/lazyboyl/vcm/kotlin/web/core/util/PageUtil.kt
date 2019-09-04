package com.github.lazyboyl.vcm.kotlin.web.core.util

import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo

class PageUtil {

    companion object {

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
            val size:Int = reqMap["size"] as Int
            PageHelper.startPage<Any>(reqMap["page"] as Int, if (size in 0..500) size else 20)
        }
    }


}