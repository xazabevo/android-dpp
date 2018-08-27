package org.dashevo.schema

import org.dashevo.schema.util.HashUtils
import org.jsonorg.JSONObject

object Serialize {

    fun encode(obj: JSONObject): ByteArray {
        return HashUtils.encode(obj)
    }

    fun decode(byteArray: ByteArray): JSONObject {
        TODO()
    }

}