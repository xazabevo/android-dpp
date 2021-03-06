/**
 * Copyright (c) 2018-present, Dash Core Team
 *
 * This source code is licensed under the MIT license found in the
 * COPYING file in the root directory of this source tree.
 */

package org.dashevo.dpp.document

import org.dashevo.dpp.BaseObject
import org.dashevo.dpp.contract.DataContract
import org.dashevo.dpp.identifier.Identifier
import org.dashevo.dpp.toBase64
import kotlin.collections.HashMap

class Document(rawDocument: MutableMap<String, Any?>, dataContract: DataContract) : BaseObject() {

    companion object {
        const val PROTOCOL_VERSION = 0

        fun convertDataToString(map: MutableMap<String, Any>) {
            for (key in map.keys) {
                var value = map[key]
                when (value) {
                    is Map<*, *> -> convertDataToString(value as MutableMap<String, Any>)
                    is ByteArray -> map[key] = value.toBase64()
                    is Identifier -> value.toString()
                }
            }
        }

        fun convertIdentifierToByteArray(map: MutableMap<String, Any>) {
            for (key in map.keys) {
                var value = map[key]
                when (value) {
                    is Map<*, *> -> convertDataToString(value as MutableMap<String, Any>)
                    is Identifier -> map[key] = value.toBuffer()
                }
            }
        }
    }
    val dataContract: DataContract
    var id: Identifier
    var type: String
    var dataContractId: Identifier
    var ownerId: Identifier
    lateinit var entropy: ByteArray
    var revision: Int = 0
    var data: Map<String, Any?>
    var createdAt: Long?
    var updatedAt: Long?
    var protocolVersion: Int

    init {
        this.dataContract = dataContract
        val data = HashMap(rawDocument)

        this.id = Identifier.from(data.remove("\$id")!!)
        this.type = data.remove("\$type") as String
        this.dataContractId = Identifier.from(data.remove("\$dataContractId")!!)
        this.ownerId = Identifier.from(data.remove("\$ownerId")!!)
        this.revision = data.remove("\$revision") as Int
        this.createdAt = data.remove("\$createdAt")?.let { it as Long }
        this.updatedAt = data.remove("\$updatedAt")?.let { it as Long }
        this.protocolVersion = data.remove("\$protocolVersion") as Int

        this.data = data
    }
    override fun toObject(): Map<String, Any> {
        return toObject(false)
    }

    fun toObject(skipIdentifierConversion: Boolean): MutableMap<String, Any> {
        val map = hashMapOf(
                "\$protocolVersion" to protocolVersion,
                "\$id" to id,
                "\$type" to type,
                "\$dataContractId" to dataContractId,
                "\$ownerId" to ownerId,
                "\$revision" to revision
        )

        data.keys.iterator().forEach {
            data[it]?.let { it1 -> map[it] = it1 }
        }

        createdAt?.let { map["\$createdAt"] = it }
        updatedAt?.let { map["\$updatedAt"] = it }

        if (!skipIdentifierConversion) {
            map["\$id"] = id.toBuffer()
            map["\$dataContractId"] = dataContractId.toBuffer()
            map["\$ownderId"] = ownerId.toBuffer()

            //TODO: change binary items items in data to ByteArray
            convertIdentifierToByteArray(map)
        }

        return map
    }

    override fun toJSON(): Map<String, Any> {

        val json = toObject(true)
        //TODO: change binary items in data to base64
        convertDataToString(json)
        return json
    }



    fun get(path: String): Any? {
        val paths = path.split("/")
        var map = data
        for (i: Int in 0..paths.size) {
            if (map.containsKey(paths[i])) {
                if (i == paths.size)
                    return map[paths[i]]
                else {
                    map = map[paths[i]] as Map<String, Any?>
                }
            }
        }
        return null
    }

    fun set(path: String, value: Any) {
        TODO("set field specified by path to the value")
    }
}

