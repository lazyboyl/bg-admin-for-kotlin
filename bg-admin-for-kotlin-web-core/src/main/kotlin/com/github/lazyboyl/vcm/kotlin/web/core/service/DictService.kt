package com.github.lazyboyl.vcm.kotlin.web.core.service

import com.github.lazyboyl.vcm.kotlin.web.core.dao.DictDao
import com.github.lazyboyl.vcm.kotlin.web.core.entity.Dict
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(rollbackFor = arrayOf(Exception::class))
class DictService {

    /**
     * 字典的dao
     */
    @Autowired
    private val dictDao: DictDao? = null

    /**
     * 功能描述： 保存数据字典的数据
     */
    fun save(dict: Dict) {
        dictDao?.insert(dict)
    }

}