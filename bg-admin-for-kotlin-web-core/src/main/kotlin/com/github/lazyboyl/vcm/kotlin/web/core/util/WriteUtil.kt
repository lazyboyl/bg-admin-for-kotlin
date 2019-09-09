package com.github.lazyboyl.vcm.kotlin.web.core.util

import com.github.lazyboyl.vcm.kotlin.web.core.entity.ReturnInfo
import java.io.IOException
import java.io.PrintWriter
import javax.servlet.http.HttpServletResponse

class WriteUtil {

    companion object {
        /**
         * 功能描述：异常信息处理
         *
         * @param httpServletResponse
         * @param failCode            失败码
         * @param msg                 失败原因
         */
        fun write(httpServletResponse: HttpServletResponse?, failCode: Int, msg: String) {
            var writer: PrintWriter? = null
            try {
                /**
                 * 设置允许跨域请求和返回页面的编码
                 */
                httpServletResponse!!.characterEncoding = "UTF-8"
                httpServletResponse.setHeader("Access-Control-Allow-Origin", "*")
                httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE")
                writer = httpServletResponse.writer
                val result = JsonUtils.objToJson(ReturnInfo(failCode, msg))
                writer!!.print(result)
            } catch (e: IOException) {
                println("response error:$e")
            } finally {
                writer?.close()
            }
        }
    }


}