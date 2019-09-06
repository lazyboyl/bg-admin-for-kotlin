package com.github.lazyboyl.vcm.kotlin.web.core.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthController(val authorities: Array<String>)