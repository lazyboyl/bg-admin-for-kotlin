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

@RestController
@RequestMapping("/dict")
class DictController {

    @Autowired
    lateinit var dictService: DictService

    @ApiOperation(value = "实现增加字典数据")
    @PostMapping("addDict")
    fun save(dict: Dict) {
        dictService.save(dict = dict)
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