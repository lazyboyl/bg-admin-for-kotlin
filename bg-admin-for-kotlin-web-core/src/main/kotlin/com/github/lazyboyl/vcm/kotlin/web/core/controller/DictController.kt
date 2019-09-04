package com.github.lazyboyl.vcm.kotlin.web.core.controller

import com.github.lazyboyl.vcm.kotlin.web.core.entity.Dict
import com.github.lazyboyl.vcm.kotlin.web.core.entity.ReturnInfo
import com.github.lazyboyl.vcm.kotlin.web.core.service.DictService
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * 类描述： 数据字典的controller
 * @author linzf
 * @since 2019-09-04
 */
@RestController
@RequestMapping("/dict")
class DictController {

    /**
     * 数据字典的service
     */
    @Autowired
    lateinit var dictService: DictService

    /**
     * 功能描述：加载全部的字典数据
     *
     * @return 返回操作结果
     */
    @ApiOperation(value = "加载全部的字典数据")
    @PostMapping("loadAll")
    fun loadAll(): ReturnInfo {
        return dictService.loadAll()
    }

    /**
     * 功能描述：更新字典数据
     *
     * @param dictCode  字典编码
     * @param dictText  字典文本
     * @param dictValue 字典值
     * @param id        字典流水id
     * @return 返回更新结果
     */
    @ApiOperation(value = "更新字典数据")
    @PostMapping("updateDict")
    fun updateDict(@RequestParam(name = "dictCode") dictCode: String, @RequestParam(name = "dictText") dictText: String, @RequestParam(name = "dictValue") dictValue: String, @RequestParam(name = "id") id: String): ReturnInfo {
        return dictService.updateDict(dictCode, dictText, dictValue, id)
    }

    /**
     * 功能描述：根据字典流水来获取字典数据
     *
     * @param id 字典流水ID
     * @return 返回操作结果
     */
    @ApiOperation(value = "根据字典流水来获取字典数据")
    @PostMapping("getDict")
    fun getDict(@RequestParam(name = "id") id: String): ReturnInfo {
        return dictService.getDict(id)
    }

    /**
     * 功能描述：实现删除字典数据
     *
     * @param id 字典流水ID
     * @return 返回删除结果
     */
    @ApiOperation(value = "实现删除字典数据")
    @PostMapping("deleteDict")
    fun deleteDict(@RequestParam(name = "id") id: String): ReturnInfo {
        return dictService.deleteDict(id)
    }

    /**
     * 功能描述：实现增加字典数据
     *
     * @param dict 包含字典数据的实体
     * @return 返回操作结果
     */
    @ApiOperation(value = "实现增加字典数据")
    @PostMapping("addDict")
    fun save(dict: Dict): ReturnInfo {
        return dictService.save(dict = dict)
    }

    /**
     * 功能描述：验证字典的类型和编码是否重复
     *
     * @param id       字典流水ID 允许为空
     * @param dictType 字典类型
     * @param dictCode 字典编码
     * @return 返回验证结果
     */
    @ApiOperation(value = "验证字典的类型和编码是否重复")
    @PostMapping("checkTypeAndCode")
    fun checkTypeAndCode(@RequestParam(name = "id", required = false) id: String?, @RequestParam(name = "dictType") dictType: String, @RequestParam(name = "dictCode") dictCode: String): ReturnInfo {
        return dictService.checkTypeAndCode(id, dictType, dictCode)
    }


    /**
     * 功能描述：获取数据字典列表
     *
     * @param search       模糊匹配数据字典的dictType、dictText、dictValue、dictCode 允许为空
     * @param dictCode     模糊查询dictCode
     * @param pageSize     每页显示的记录的条数
     * @param current      当前访问第几页
     * @param orderKey     排序字段
     * @param orderByValue 排序方式，降序还是升序
     * @return 返回查询结果
     */
    @ApiOperation(value = "获取数据字典列表")
    @PostMapping("queryDictList")
    fun queryDictList(@RequestParam(name = "search", required = false) search: String?,
                      @RequestParam(name = "dictCode", required = false) dictCode: String?,
                      @RequestParam(name = "pageSize") pageSize: Int,
                      @RequestParam(name = "current") current: Int,
                      @RequestParam(name = "orderKey", required = false) orderKey: String?,
                      @RequestParam(name = "orderByValue", required = false) orderByValue: String?): ReturnInfo {
        return dictService.queryDictList(search = search, dictCode = dictCode, pageSize = pageSize, current = current, orderKey = orderKey, orderByValue = orderByValue)
    }


}