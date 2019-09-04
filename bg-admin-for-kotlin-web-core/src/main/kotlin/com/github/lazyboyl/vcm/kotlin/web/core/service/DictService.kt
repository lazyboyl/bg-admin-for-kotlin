package com.github.lazyboyl.vcm.kotlin.web.core.service

import com.github.lazyboyl.vcm.kotlin.web.core.constant.SystemStaticConst
import com.github.lazyboyl.vcm.kotlin.web.core.dao.DictDao
import com.github.lazyboyl.vcm.kotlin.web.core.entity.Dict
import com.github.lazyboyl.vcm.kotlin.web.core.entity.Page
import com.github.lazyboyl.vcm.kotlin.web.core.entity.ReturnInfo
import com.github.lazyboyl.vcm.kotlin.web.core.util.PageUtil
import com.github.pagehelper.PageHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.HashMap

@Service
@Transactional(rollbackFor = [Exception::class])
class DictService {

    /**
     * 字典的dao
     * 如果有bean需要注入则使用【lateinit var】来实现bean的注入
     */
    @Autowired
    lateinit var dictDao: DictDao

    /**
     * 功能描述：加载全部的字典数据
     * @return 返回操作结果
     */
    fun loadAll(): ReturnInfo {
        try {
            return ReturnInfo(SystemStaticConst.SUCCESS, "加载全部的字典数据成功", dictDao.selectAll())
        } catch (e: Exception) {
            return ReturnInfo(SystemStaticConst.FAIL, "加载全部的字典数据失败！失败原因：" + e.message)
        }
    }

    /**
     * 功能描述：更新字典数据
     * @param dictCode 字典编码
     * @param dictText 字典文本
     * @param dictValue 字典值
     * @param id 字典流水id
     * @return 返回更新结果
     */
    fun updateDict(dictCode: String, dictText: String, dictValue: String, id: String): ReturnInfo {
        try {
            val dict = dictDao.selectByPrimaryKey(id)
            if (dictDao.checkTypeAndCode(id = dict?.id, dictType = dict?.dictType, dictCode = dictCode) > 0) {
                return ReturnInfo(SystemStaticConst.FAIL, "字典类型和字典编码已经存在，请修改以后再提交！")
            }
            when (dictDao.updateDict(dictCode = dictCode, dictText = dictText, dictValue = dictValue, id = id) > 0) {
                true -> return ReturnInfo(SystemStaticConst.SUCCESS, "更新字典数据成功")
                false -> return ReturnInfo(SystemStaticConst.FAIL, "更新字典数据失败！失败原因：查无此字典数据")
            }
        } catch (e: Exception) {
            return ReturnInfo(SystemStaticConst.FAIL, "更新字典数据失败！失败原因：" + e.message)
        }


    }

    /**
     * 功能描述：根据字典流水来获取字典数据
     * @param id 字典流水ID
     * @return 返回操作结果
     */
    fun getDict(id: String): ReturnInfo {
        val dict = dictDao.selectByPrimaryKey(id)?:return ReturnInfo(SystemStaticConst.FAIL, "获取字典数据失败！失败原因：查无此字典数据")
        return ReturnInfo(SystemStaticConst.SUCCESS, "获取字典数据成功", dict)
    }

    /**
     * 功能描述： 保存数据字典的数据
     */
    fun save(dict: Dict): ReturnInfo {
        try {
            if (dictDao.checkTypeAndCode(id = dict.id, dictType = dict.dictType, dictCode = dict.dictCode) > 0) {
                return ReturnInfo(SystemStaticConst.FAIL, "字典类型和字典编码已经存在，请修改以后再提交！")
            }
            dictDao.insert(dict)
        } catch (e: Exception) {
            return ReturnInfo(SystemStaticConst.FAIL, "增加字典数据失败，失败原因：" + e.message)
        }
        return ReturnInfo(SystemStaticConst.SUCCESS, "增加字典数据成功", dict)
    }

    /**
     * 功能描述：实现删除字典数据
     *
     * @param id 字典流水ID
     * @return 返回删除结果
     */
    fun deleteDict(id: String): ReturnInfo {
        try {
            return if (dictDao.deleteByPrimaryKey(id) > 0) {
                ReturnInfo(SystemStaticConst.SUCCESS, "删除字典数据成功")
            } else ReturnInfo(SystemStaticConst.FAIL, "删除字典数据失败！失败原因：该字典数据不存在")
        } catch (e: Exception) {
            return ReturnInfo(SystemStaticConst.FAIL, "删除字典数据失败！失败原因：" + e.message)
        }

    }

    /**
     * 功能描述：验证字典的类型和编码是否重复
     *
     * @param id       字典流水ID
     * @param dictType 字典类型
     * @param dictCode 字典编码
     * @return 返回验证结果
     */
    fun checkTypeAndCode(id: String?, dictType: String?, dictCode: String?): ReturnInfo {
        val result = HashMap<String, Any>(1)
        try {
            if (dictDao.checkTypeAndCode(id = id, dictType = dictType, dictCode = dictCode) > 0) {
                result["success"] = "unPass"
            } else {
                result["success"] = "pass"
            }
        } catch (e: Exception) {
            return ReturnInfo(SystemStaticConst.FAIL, "验证请求处理失败，失败原因：" + e.message)
        }
        return ReturnInfo(SystemStaticConst.SUCCESS, "获取数据字典列表数据成功！", result)
    }


    /**
     * 功能描述：获取数据字典列表
     *
     * @param search   模糊匹配数据字典的dictType、dictText、dictValue、dictCode
     * @param dictCode 模糊查询dictCode
     * @param pageSize 每页显示的记录的条数
     * @param current  当前访问第几页
     * @param orderKey     排序字段
     * @param orderByValue 排序方式，降序还是升序
     * @return 返回查询结果
     */
    fun queryDictList(search: String?, dictCode: String?, pageSize: Int, current: Int, orderKey: String?, orderByValue: String?): ReturnInfo {
        PageHelper.startPage<Any>(current, if (pageSize in 0..500) pageSize else 20, if (orderKey != null) if (orderByValue != null) "$orderKey $orderByValue" else orderKey else "")
        val res = PageUtil.getResult(dictDao.queryDictList(search = search, dictCodeArray = dictCode?.split(" ")))
        return ReturnInfo(SystemStaticConst.SUCCESS, "获取数据字典列表数据成功！", Page(pageSize = pageSize, current = current, total = res["total"] as Long, rows = res["rows"] as List<*>))
    }


}