package com.home.cmoneytestdemo.common.util

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * personally recommend using Gson Moshi
 */
class JsonUtil(var jsonString: Any?) {

    var jsonObject: JSONObject? = null
    var jsonArray: JSONArray? = null
    private var value: Any? = null

    private val isNull: Boolean
        get() {
            if (value == null && jsonArray == null && jsonObject == null) {
                return true
            }
            return this.value().toString() == "null"
        }

    init {
        if (ArrayList::class.java.isInstance(jsonString)) {
            jsonString = JSONArray(jsonString as ArrayList<*>?)
        }
        when {
            jsonString == null -> value = null
            // json object
            jsonString.toString().trim { it <= ' ' }.startsWith("{") ->
                try {
                    jsonObject = JSONObject(jsonString.toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            // json array
            jsonString.toString().trim { it <= ' ' }.startsWith("[") ->
                try {
                    jsonArray = JSONArray(jsonString.toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            else -> value = jsonString
        }
    }

    override fun toString(): String {
        return if (isNull) "" else this.value()!!.toString()
    }

    fun count(): Int {
        if (jsonObject != null) {
            return jsonObject!!.length()
        }
        return if (jsonArray != null) {
            jsonArray!!.length()
        } else {
            0
        }
    }

    fun key(keyStr: String): JsonUtil {
        if (jsonObject == null) {
            return JsonUtil(null)
        }
        try {
            val obj = jsonObject!!.get(keyStr)
            return JsonUtil(obj)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return JsonUtil(null)
    }

    fun index(index: Int): JsonUtil {
        if (jsonArray == null) {
            return JsonUtil(null)
        }
        try {
            return JsonUtil(jsonArray!!.get(index))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return JsonUtil(null)
    }

    fun stringValue(): String {
        if (this.value() == null) {
            return ""
        }
        return if (isNull) {
            ""
        } else {
            this.value().toString()
        }
    }

    fun intValue(): Int {
        if (this.value() == null) {
            return 0
        }
        return try {
            Integer.valueOf(this.value()!!.toString())
        } catch (e: NumberFormatException) {
            0
        }
    }

    fun longValue(): Long {
        if (this.value() == null) {
            return 0
        }
        return try {
            java.lang.Long.valueOf(this.value()!!.toString())
        } catch (e: NumberFormatException) {
            0
        }
    }

    fun doubleValue(): Double? {
        if (this.value() == null) {
            return 0.0
        }
        return try {
            java.lang.Double.valueOf(this.value()!!.toString())
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    fun booleanValue(): Boolean {
        return if (this.value() == null) {
            false
        } else {
            this.stringValue() == "true"
        }
    }

    fun value(): Any? {
        return if (nullableValue() != null) {
            nullableValue()
        } else {
            JsonUtil(null)
        }
    }

    private fun nullableValue(): Any? {
        if (value != null) {
            return value
        }
        if (jsonObject != null) {
            return jsonObject!!.toString()
        }
        return if (jsonArray != null) {
            jsonArray!!.toString()
        } else {
            null
        }
    }

    fun exist(): Boolean {
        return nullableValue() != null
    }

    fun removeWithKey(Key: String?) {
        if (Key == null) {
            return
        }
        val jsonObject = this.jsonObject ?: return
        jsonObject.remove(Key)
        this.jsonObject = jsonObject
    }

    fun addEditWithKey(Key: String?, Object: Any?) {
        if (Object == null || Key == null) {
            return
        }
        val jsonObject = this.jsonObject ?: return
        var normalizedObject = Object
        if (JsonUtil::class.java.isInstance(Object)) {
            when {
                (Object as JsonUtil).jsonArray != null -> normalizedObject = Object.jsonArray
                Object.jsonObject != null -> normalizedObject = Object.jsonObject
                else -> normalizedObject = Object.value()
            }
        }
        try {
            jsonObject.putOpt(Key, normalizedObject)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        this.jsonObject = jsonObject
    }

    fun addWithIndex(inputObject: Any?, index: Int) {
        if (inputObject == null) {
            return
        }
        val jsonArr = this.jsonArray ?: return
        try {
            if (JsonUtil::class.java.isInstance(inputObject)) {
                if ((inputObject as JsonUtil).jsonArray != null) {
                    if (index == -1) {
                        jsonArr.put(inputObject.jsonArray)
                    } else {
                        jsonArr.put(index, inputObject.jsonArray)
                    }
                } else if (inputObject.jsonObject != null) {
                    if (index == -1) {
                        jsonArr.put(inputObject.jsonObject)
                    } else {
                        jsonArr.put(index, inputObject.jsonObject)
                    }
                } else {
                    if (index == -1) {
                        jsonArr.put(inputObject.value())
                    } else {
                        jsonArr.put(index, inputObject.value())
                    }
                }
            } else {
                if (index == -1) {
                    jsonArr.put(inputObject)
                } else if (index > -1) {
                    jsonArr.put(index, inputObject)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        this.jsonArray = jsonArr
    }

    fun add(inputObject: Any) {
        addWithIndex(inputObject, -1)
    }

    fun removeWithIndex(index: Int) {
        val jsonArr = this.jsonArray ?: return
        val newJsonArr = JSONArray()
        for (i in 0 until jsonArr.length()) {
            try {
                if (i != index) {
                    newJsonArr.put(jsonArr.get(i))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        this.jsonArray = newJsonArr
    }

    fun remove(inputObject: Any?) {
        if (inputObject == null) {
            return
        }
        val jsonArr = this.jsonArray ?: return
        val newJsonArr = JSONArray()
        for (i in 0 until jsonArr.length()) {
            try {
                if (JSONObject::class.java.isInstance(inputObject) &&
                    JSONObject::class.java.isInstance(jsonArr.get(i))
                ) {
                    if (!jsonObjectComparesEqual(
                            jsonArr.get(i) as JSONObject,
                            (inputObject as JSONObject?)!!,
                            null,
                            null
                        )
                    ) {
                        newJsonArr.put(jsonArr.get(i))
                    }
                } else {
                    if (jsonArr.get(i) != inputObject) {
                        newJsonArr.put(jsonArr.get(i))
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        this.jsonArray = newJsonArr
    }

    private class AsIterable<T>(private val iterator: Iterator<T>) : Iterable<T> {
        override fun iterator(): Iterator<T> {
            return iterator
        }
    }

    companion object {
        fun create(`object`: Any): JsonUtil {
            return JsonUtil(`object`.toString())
        }

        fun dic(vararg values: Any): JSONObject {
            val mainDic = JSONObject()
            var i = 0
            while (i < values.size) {
                try {
                    var valueObject: Any? = values[i + 1]
                    if (JsonUtil::class.java.isInstance(valueObject)) {
                        if ((valueObject as JsonUtil).jsonArray != null) {
                            valueObject = valueObject.jsonArray
                        } else if (valueObject.jsonObject != null) {
                            valueObject = valueObject.jsonObject
                        }
                    }
                    mainDic.put(values[i] as String, valueObject ?: JSONObject.NULL)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                i += 2
            }
            return mainDic
        }

        fun array(vararg values: Any): JSONArray {
            val mainList = JSONArray()
            for (obj in values) {
                mainList.put(obj)
            }
            return mainList
        }

        fun jsonObjectComparesEqual(
            x: JSONObject,
            y: JSONObject,
            only: Collection<String>?,
            except: Collection<String>?
        ): Boolean {
            val keys = keySet(x)
            keys.addAll(keySet(y))
            if (only != null) {
                keys.retainAll(only)
            }
            if (except != null) {
                keys.removeAll(except)
            }
            for (s in keys) {
                var a: Any? = null
                var b: Any? = null
                try {
                    a = x.get(s)
                } catch (e: JSONException) {
                }
                try {
                    b = x.get(s)
                } catch (e: JSONException) {
                }
                if (a != null) {
                    if (a != b) {
                        return false
                    }
                } else if (b != null) {
                    if (b != a) {
                        return false
                    }
                }
            }
            return true
        }

        private fun keySet(j: JSONObject): MutableSet<String> {
            val res = TreeSet<String>()
            for (s in AsIterable(j.keys())) {
                res.add(s)
            }
            return res
        }
    }
}
