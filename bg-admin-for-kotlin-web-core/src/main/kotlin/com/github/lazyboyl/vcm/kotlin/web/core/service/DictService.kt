package com.github.lazyboyl.vcm.kotlin.web.core.service

import com.github.lazyboyl.vcm.kotlin.web.core.constant.SystemStaticConst
import com.github.lazyboyl.vcm.kotlin.web.core.dao.DictDao
import com.github.lazyboyl.vcm.kotlin.web.core.entity.Dict
import com.github.lazyboyl.vcm.kotlin.web.core.entity.ReturnInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(rollbackFor = arrayOf(Exception::class))
class DictService {

    /**
     * 字典的dao
     * 如果有bean需要注入则使用【lateinit var】来实现bean的注入
     */
    @Autowired
    lateinit var dictDao: DictDao

    /**
     * 功能描述： 保存数据字典的数据
     */
    fun save(dict: Dict): ReturnInfo {
        try {
            if (dictDao.checkTypeAndCode(dict.id, dict.dictType, dict.dictCode) > 0) {
                return ReturnInfo(SystemStaticConst.FAIL, "字典类型和字典编码已经存在，请修改以后再提交！")
            }
            dictDao.insert(dict)
        } catch (e: Exception) {
            return ReturnInfo(SystemStaticConst.FAIL, "增加字典数据失败，失败原因：" + e.message)
        }
        return ReturnInfo(SystemStaticConst.SUCCESS, "增加字典数据成功", dict)
    }

}