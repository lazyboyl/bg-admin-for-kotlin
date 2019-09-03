package com.github.lazyboyl.vcm.kotlin.web.core.dao

import com.github.lazyboyl.vcm.kotlin.web.core.entity.Dict
import org.apache.ibatis.annotations.Param
import tk.mybatis.mapper.common.Mapper

/**
 * @author linzf
 * @since 2019-09-03
 * 类描述： 数据字典的dao
 */
interface DictDao : Mapper<Dict> {

    /**
     * 功能描述：更新字典数据
     * @param dictCode 字典编码
     * @param dictText 字典文本
     * @param dictValue 字典值
     * @param id 字典流水id
     * @return 返回更新结果
     */
    fun updateDict(@Param("dictCode") dictCode: String, @Param("dictText") dictText: String, @Param("dictValue") dictValue: String, @Param("id") id: String): Int

    /**
     * 功能描述：验证字典的类型和编码是否重复
     * @param id 字典流水ID
     * @param dictType 字典类型
     * @param dictCode 字典编码
     * @return 返回验证结果
     */
    fun checkTypeAndCode(@Param("id") id: String?, @Param("dictType") dictType: String?, @Param("dictCode") dictCode: String?): Int

    /**
     * 功能描述：获取数据字典列表
     * @param search 模糊匹配数据字典的dictType、dictText、dictValue、dictCode
     * @param dictCodeArray 分段模糊查询dictCode
     * @return 返回查询结果
     */
    fun queryDictList(@Param("search") search: String, @Param("dictCodeArray") dictCodeArray: Array<String>?): List<Dict>

}