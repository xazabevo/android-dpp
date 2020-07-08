/**
 * Copyright (c) 2020-present, Dash Core Team
 *
 * This source code is licensed under the MIT license found in the
 * COPYING file in the root directory of this source tree.
 */
package org.dashevo.dpp.document

class DocumentDeleteTransition : DocumentTransition {

    override val action = Action.DELETE
    var id: String
    var documentType: String

    constructor(rawStateTransition: MutableMap<String, Any?>) : super(rawStateTransition) {
        this.id = rawStateTransition["\$id"] as String
        this.documentType = rawStateTransition["\$type"] as String
    }

    override fun toJSON(): Map<String, Any> {
        var json = super.toJSON() as MutableMap<String, Any>
        json["\$id"] = id
        json["\$type"] = documentType
        return json
    }
}