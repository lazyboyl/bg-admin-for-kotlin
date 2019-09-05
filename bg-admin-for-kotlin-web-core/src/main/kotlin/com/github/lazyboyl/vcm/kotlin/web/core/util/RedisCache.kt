package com.github.lazyboyl.vcm.kotlin.web.core.util

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.ArrayList

@Component
class RedisCache {

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, *>

    /**
     * 功能描述：从redis中获取数据
     * @param key redis的key
     * @param elementType 获取以后需要转换的对象
     * @return T 返回的泛型
     **/
    fun <T> getObject(key: String, elementType: Class<T>): T? {
        return redisTemplate.execute {
            val keyBytes = redisTemplate.stringSerializer.serialize(key)
            if (it.exists(keyBytes!!)!!) {
                val valueBytes = it.get(keyBytes)
                SerializeUtil.unserialize(valueBytes!!) as T
            }
            null
        }
    }

    /**
     * 功能描述：设值到redis中
     * @param key redis的key
     * @param obj key所对应的对象
     */
    fun setObject(key: String, obj: Any) {
        val bytes = SerializeUtil.serialize(obj)
        redisTemplate.execute {
            it.set(redisTemplate.stringSerializer.serialize(key)!!, bytes)
            null
        }
    }

    /**
     * 功能描述：获取模糊匹配的数据
     * @param key 模糊匹配的key
     * @return 返回查询的结果
     */
    fun queryKeys(key: String): List<String> {
        val keys = ArrayList<String>()
        val result = redisTemplate.keys("$key*")
        for (s in result) {
            keys.add(this.getString(s) ?: "")
        }
        return keys
    }

    /**
     * 功能描述：将键值设置到redis中
     * @param key redis的key
     * @param value key所对应的值
     */
    fun setString(key: String, value: String): Boolean? {
        return redisTemplate.execute {
            val serializer = redisTemplate.stringSerializer
            it.set(serializer.serialize(key)!!, serializer.serialize(value)!!)
            true
        }
    }

    /**
     * 功能描述：根据key从redis中获取数据
     * @param key redis的key
     */
    fun getString(key: String): String? {
        return redisTemplate.execute {
            val serializer = redisTemplate.stringSerializer
            serializer.deserialize(it.get(serializer.serialize(key)!!))
        }
    }

}