package com.github.lazyboyl.vcm.kotlin.web.core.util

import tk.mybatis.mapper.genid.GenId
import java.util.*

/**
 * @author linzf
 * @since 2019-09-03
 * 类描述：生成唯一主键的工具库
 */
class UuidGenId : GenId<String> {

    override fun genId(table: String?, column: String?): String = UUID.randomUUID().toString().replace("-".toRegex(), "")


}