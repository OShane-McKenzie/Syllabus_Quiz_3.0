package com.syllabus.pq

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.UpdateValuesResponse
import com.google.api.services.sheets.v4.model.ValueRange
import java.io.ByteArrayInputStream
import java.util.Collections

import com.google.api.services.docs.v1.Docs
import com.google.api.services.docs.v1.DocsScopes
import com.google.api.services.docs.v1.model.BatchUpdateDocumentRequest
import com.google.api.services.docs.v1.model.DeleteContentRangeRequest
import com.google.api.services.docs.v1.model.InsertTextRequest
import com.google.api.services.docs.v1.model.Location
import com.google.api.services.docs.v1.model.Range
import com.google.api.services.docs.v1.model.Request
import com.google.api.services.docs.v1.model.ReplaceAllTextRequest
import com.google.api.services.docs.v1.model.SubstringMatchCriteria
import java.io.IOException


class QuizDatabase(private val credentialsJson: String = dataProvider.values.value.dba.fullDecode(),
                   private val applicationName: String = "Promotional Syllabus Quiz") {
    private val JSON_FACTORY = GsonFactory.getDefaultInstance()
    private val SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS)
    private val secretsJson = credentialsJson

    private val sheetID: String = dataProvider.values.value.dbId.fullDecode()
    private val httpTransport = GoogleNetHttpTransport.newTrustedTransport()

    private val credentials = GoogleCredential.fromStream(
        ByteArrayInputStream(secretsJson.toByteArray(Charsets.UTF_8))
    ).createScoped(SCOPES)

    private val service = Sheets.Builder(httpTransport, JSON_FACTORY, credentials)
        .setApplicationName(applicationName)
        .build()

    suspend fun readCell(spreadsheetId: String = sheetID, range: String): String? {
        return try{
            val response = service.spreadsheets().values().get(spreadsheetId, range).execute()
            val values = response.getValues()
            if (values != null && values.isNotEmpty()) {
                values[0][0].toString()
            } else {
                null
            }
        }catch (e: Exception){
            null
        }
        
    }

    suspend fun writeCell(spreadsheetId: String = sheetID, range: String, value: String) {
        val values = listOf(listOf(value))
        val body = ValueRange().setValues(values)
        service.spreadsheets().values().update(spreadsheetId, range, body)
            .setValueInputOption("RAW")
            .execute()
    }
    suspend fun writeCellWithResponse(spreadsheetId: String = sheetID, range: String, value: String): UpdateValuesResponse? {
        val values = listOf(listOf(value))
        val body = ValueRange().setValues(values)

        return try {
            val updateRequest = service.spreadsheets().values().update(spreadsheetId, range, body)
            updateRequest.valueInputOption = "RAW"
            val response = updateRequest.execute()
            response
        } catch (e: Exception) {
            null
        }
    }
}


class QuizDatabaseV2(private val credentialsJson: String = dataProvider.values.value.dba.fullDecode(),
                         private val applicationName: String = "Promotional Syllabus Quiz") {

    private val SCOPES = Collections.singletonList(DocsScopes.DOCUMENTS)
    private val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
    private val JSON_FACTORY = GsonFactory.getDefaultInstance()
    private val docs: Docs = initializeDocsService()
    private val docId = dataProvider.values.value.dbId.fullDecode()
    private fun initializeDocsService(): Docs {
        val credentials = GoogleCredential.fromStream(
            ByteArrayInputStream(credentialsJson.toByteArray(Charsets.UTF_8))
        ).createScoped(SCOPES)

        return Docs.Builder(httpTransport, JSON_FACTORY, credentials)
            .setApplicationName(applicationName)
            .build()
    }

    fun readDocument(documentId: String = docId): String {
        return try {
            val response = docs.documents().get(documentId).execute()
            val content = response.body.content?.joinToString("") { paragraph ->
                paragraph.paragraph?.elements?.joinToString("") { element ->
                    element.textRun?.content ?: ""
                } ?: ""
            } ?: ""
            content.trim()
        } catch (e: GoogleJsonResponseException) {
            println("Error reading document: ${e.message}")
            ""
        } catch (e: IOException) {
            println("Error reading document: ${e.message}")
            ""
        }
    }


    fun writeDocument(documentId: String = docId, content: String): Boolean {

        return try {
            val requests = mutableListOf<Request>()
            val deleteContentRequest = Request().setDeleteContentRange(
                DeleteContentRangeRequest()
                    .setRange(Range().setStartIndex(1).setEndIndex(readDocument().length+1)) // Delete all content in the document
            )
            requests.add(deleteContentRequest)

            // Insert new content at index 1 (or any other index you prefer)
            val insertContentRequest = Request().setInsertText(
                InsertTextRequest()
                    .setText(content)
                    .setLocation(Location().setIndex(1)) // Index 1 indicates the start of the document
            )
            requests.add(insertContentRequest)

            // Execute the batch update request with both delete and insert requests
            docs.documents().batchUpdate(documentId, BatchUpdateDocumentRequest().setRequests(requests)).execute()

            true
        } catch (e: GoogleJsonResponseException) {
            println("Error writing document: ${e.message}")
            false
        } catch (e: IOException) {
            println("Error writing document: ${e.message}")
            false
        }
    }
}


