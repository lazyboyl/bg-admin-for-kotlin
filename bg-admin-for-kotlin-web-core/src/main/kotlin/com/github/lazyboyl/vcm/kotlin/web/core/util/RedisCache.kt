package com.github.lazyboyl.vcm.kotlin.web.core.util

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisCallback
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.ArrayList

@Component
class RedisCache {

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, *>

    /**
     * 功能描述：从redis中获取数据
     * @param key
     * @param elementType
     * @param <T>
     * @return
    </T> */
    fun <T> getObject(key: String, elementType: Class<T>): T? {
        return redisTemplate.execute(RedisCallback { connection ->
            val keyBytes = redisTemplate.stringSerializer.serialize(key)
            if (connection.exists(keyBytes!!)!!) {
                val valueBytes = connection.get(keyBytes)
                return@RedisCallback SerializeUtil.unserialize(valueBytes!!) as T
            }
            null
        })
    }

    /**
     * 功能描述：设值到redis中
     * @param key
     * @param obj
     */
    fun setObject(key: String, obj: Any) {
        val bytes = SerializeUtil.serialize(obj)
        redisTemplate.execute { connection ->
            connection.set(redisTemplate.stringSerializer.serialize(key)!!, bytes)
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
            keys.add(this.getString(s)?:"")
        }
        return keys
    }

    fun setString(key: String, value: String): Boolean? {
        return redisTemplate.execute{
            val serializer = redisTemplate.stringSerializer
            it.set(serializer.serialize(key)!!, serializer.serialize(value)!!)
            true
        }
    }

    fun getString(key: String): String? {
        return redisTemplate.execute {
            val serializer = redisTemplate.stringSerializer
            serializer.deserialize(it.get(serializer.serialize(key)!!))
        }
    }

}