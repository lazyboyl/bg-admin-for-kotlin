package com.github.lazyboyl.vcm.kotlin.web.core.entity

/**
 * @author linzf
 * @since 2019/09/03
 * 类描述： 统一的返回处理的实体对象
 */
class ReturnInfo constructor(code: Int, msg: String) {

    /**
     * 子构造函数直接使用父的构造函数
     * 【obj: Any?】这种写法允许我们实例化的时候obj参数传null进来，若不加问号则传null会报错
     */
    constructor(code: Int, msg: String, obj: Any?) : this(code, msg) {
        this.obj = obj
    }

    /**
     * 返回的错误码
     */
    var code: Int? = code

    /**
     * 返回的错误信息
     */
    var msg: String? = msg

    /**
     * 返回的json数据
     */
    var obj: Any? = null
}