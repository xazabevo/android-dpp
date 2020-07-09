/**
 * Copyright (c) 2020-present, Dash Core Team
 *
 * This source code is licensed under the MIT license found in the
 * COPYING file in the root directory of this source tree.
 */
package org.dashevo.dpp.document

import org.dashevo.dpp.Fixtures
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DocumentTest {

    @Test
    fun testDocument() {
        val documents = Fixtures.getDocumentsFixture()

        assertEquals(5, documents.size)
        assertEquals(Document.Action.CREATE, documents[0].action)
        assertEquals("4mZmxva49PBb7BE7srw9o3gixvDfj1dAx1K2dmAAauGp", documents[2].ownerId)
        assertEquals("3eHkM3mWjutxbc3EQwnVQHyymDgbZfK4EhZRectK11jV", documents[3].dataContractId)
    }

    @Test
    fun testDocumentFactory() {
        var factory = DocumentFactory()

        val contract = Fixtures.getDataContractFixtures()

        val factoryCreatedDocument = factory.create(contract, "4mZmxva49PBb7BE7srw9o3gixvDfj1dAx1K2dmAAauGp", "niceDocument", JSONObject("{ name: 'Cutie' }").toMap())
        val fixtureCreatedDocuments = Fixtures.getDocumentsFixture()

        // compare the first document
        assertEquals(fixtureCreatedDocuments[0].dataContractId, factoryCreatedDocument.dataContractId)
        assertEquals(fixtureCreatedDocuments[0].ownerId, factoryCreatedDocument.ownerId)
        assertEquals(fixtureCreatedDocuments[0].type, factoryCreatedDocument.type)
        assertEquals(fixtureCreatedDocuments[0].data["name"], factoryCreatedDocument.data["name"])

    }

    @Test
    fun applyStateTransition() {
        val documents = Fixtures.getDocumentsFixture()
        val batchTransition = hashMapOf(
                "create" to documents
        )
        val result = DocumentFactory().createStateTransition(batchTransition)
        //assertEquals(result.documents, documents)
    }

    @Test
    fun verifySignedDocumentsSTTest() {
        val documentST = Fixtures.getDocumentsSTSignedFixture()
        val identityST = Fixtures.getIdentityCreateSTSignedFixture()
        val identity = Fixtures.getIdentityForSignaturesFixture()
        //Assertions.assertTrue(documentST.verifySignature(identityST.publicKeys[0]))
        //Assertions.assertTrue(documentST.verifySignature(identity.publicKeys[0]))

        val documentSTTwo = Fixtures.getDocumentsSTSignedFixtureTwo()
        //assertEquals(documentST.documents[0].toJSON(), documentSTTwo.documents[0].toJSON())
    }
}