package com.github.lazyboyl.vcm.kotlin.web.core.entity

import com.github.lazyboyl.vcm.kotlin.web.core.util.UuidGenId
import tk.mybatis.mapper.annotation.KeySql
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.Table

/**
 * @author linzf
 * @since 2019-09-03
 * 类描述： 数据字典的实体类
 */
@Table(name = "t_dict")
class Dict {

    /**
     * 字典流水ID
     */
    @Id
    @Column(name = "id")
    @KeySql(genId = UuidGenId::class)
    var id: String? = null

    /**
     * 字典类型
     */
    @Column(name = "dictType")
    var dictType: String? = null

    /**
     * 字典编码
     */
    @Column(name = "dictCode")
    var dictCode: String? = null

    /**
     * 字典文本
     */
    @Column(name = "dictText")
    var dictText: String? = null

    /**
     * 字典值
     */
    @Column(name = "dictValue")
    var dictValue: String? = null

}