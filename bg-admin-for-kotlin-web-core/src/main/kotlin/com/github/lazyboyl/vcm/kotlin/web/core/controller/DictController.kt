package com.github.lazyboyl.vcm.kotlin.web.core.controller

import com.github.lazyboyl.vcm.kotlin.web.core.entity.Dict
import com.github.lazyboyl.vcm.kotlin.web.core.service.DictService
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/dict")
class DictController {

    @Autowired
    private val dictService: DictService? = null

    @ApiOperation(value = "实现增加字典数据")
    @PostMapping("addDict")
    fun save(dict: Dict){
        dictService?.save(dict)
    }

}