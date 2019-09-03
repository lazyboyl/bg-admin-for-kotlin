package com.github.lazyboyl.vcm.kotlin.web

import com.didispace.swagger.EnableSwagger2Doc
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import tk.mybatis.spring.annotation.MapperScan

@SpringBootApplication
@MapperScan("com.github.lazyboyl.vcm.kotlin.web.core.dao")
@EnableSwagger2Doc
class BgAdminForKotlinWebCoreApplication

fun main(args: Array<String>) {
    runApplication<BgAdminForKotlinWebCoreApplication>(*args)
}

