package com.github.lazyboyl.vcm.kotlin.web.core.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException
import java.util.ArrayList
import java.util.HashMap

class JsonUtils {

    companion object {
        // 定义jackson对象
        private val MAPPER = ObjectMapper()
        private val MAP = MAPPER.typeFactory.constructMapType(HashMap::class.java, String::class.java, Any::class.java)
        private val STRING_MAP = MAPPER.typeFactory.constructMapType(HashMap::class.java, String::class.java, String::class.java)
        private val LIST_MAP = MAPPER.typeFactory.constructParametricType(ArrayList::class.java, MAP)

        init {
            MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }

        fun <T> map2object(map: Map<*, *>, beanType: Class<T>): T {
            try {
                return MAPPER.readValue(MAPPER.writeValueAsString(map), beanType)
            } catch (e: Exception) {
                throw RuntimeException("Jackson处理出现错误", e)
            }

        }


        fun objToJson(obj: Any): String {
            try {
                return MAPPER.writeValueAsString(obj)
            } catch (e: IOException) {
                throw RuntimeException("Jackson处理出现错误", e)
            }

        }

        fun <T> jsonToPojo(json: String, beanType: Class<T>): T {
            try {
                return MAPPER.readValue(json, beanType)
            } catch (e: IOException) {
                throw RuntimeException("Jackson处理出现错误", e)
            }

        }

        fun <T> objToPojo(obj: Any, valueType: Class<T>): T {
            try {
                return MAPPER.readValue(MAPPER.writeValueAsString(obj), valueType)
            } catch (e: IOException) {
                throw RuntimeException("Jackson处理出现错误", e)
            }

        }

        fun <T> objToList(obj: Any, valueType: Class<T>): ArrayList<T> {
            val listType = MAPPER.typeFactory.constructParametricType(ArrayList::class.java, valueType)
            try {
                return MAPPER.readValue(MAPPER.writeValueAsString(obj), listType)
            } catch (e: IOException) {
                throw RuntimeException("Jackson处理出现错误", e)
            }

        }

        fun <T> jsonToList(json: String, valueType: Class<T>): ArrayList<T> {
            val listType = MAPPER.typeFactory.constructParametricType(ArrayList::class.java, valueType)
            try {
                return MAPPER.readValue(json, listType)
            } catch (e: IOException) {
                throw RuntimeException("Jackson处理出现错误", e)
            }

        }

        fun objToMap(obj: Any): HashMap<String, Any> {
            try {
                return MAPPER.readValue(MAPPER.writeValueAsString(obj), MAP)
            } catch (e: IOException) {
                throw RuntimeException("Jackson处理出现错误", e)
            }

        }

        fun jsonToMap(json: String): HashMap<String, Any> {
            try {
                return MAPPER.readValue(json, MAP)
            } catch (e: IOException) {
                throw RuntimeException("Jackson处理出现错误", e)
            }

        }

        fun jsonToStringMap(json: String): HashMap<String, String> {
            try {
                return MAPPER.readValue(json, STRING_MAP)
            } catch (e: IOException) {
                throw RuntimeException("Jackson处理出现错误", e)
            }

        }

        fun objToMapList(obj: Any): ArrayList<HashMap<String, Any>> {
            try {
                return MAPPER.readValue(MAPPER.writeValueAsString(obj), LIST_MAP)
            } catch (e: IOException) {
                throw RuntimeException("Jackson处理出现错误", e)
            }

        }

        fun jsonToMapList(json: String): ArrayList<HashMap<String, Any>> {
            try {
                return MAPPER.readValue(json, LIST_MAP)
            } catch (e: IOException) {
                throw RuntimeException("Jackson处理出现错误", e)
            }

        }
    }

}