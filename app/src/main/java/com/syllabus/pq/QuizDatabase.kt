package com.syllabus.pq

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.UpdateValuesResponse
import com.google.api.services.sheets.v4.model.ValueRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.Collections


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
//fun getEmployees(context: Context, resourceId: Int): String {
//    val inputStream = context.resources.openRawResource(resourceId)
//    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
//    val stringBuilder = StringBuilder()
//    var line: String? = bufferedReader.readLine()
//
//    while (line != null) {
//        stringBuilder.append(line)
//        stringBuilder.append('\n')
//        line = bufferedReader.readLine()
//    }
//
//    bufferedReader.close()
//    inputStream.close()
//
//    return stringBuilder.toString()
//}

//suspend fun getEmployees(api: String = empApi
//    .toDecodedString()
//    .decryptAes(DataEncode()
//        .getKeys(1)
//        .toDecodedString())): List<Employee> {
//
//    val data: MutableList<Employee> = mutableListOf()
//    val typeToken = object : TypeToken<List<Employee>>() {}.type
//    val gson = Gson()
//
//    try {
//        val url = URL(api)
//        val jsonText = withContext(Dispatchers.IO) {
//            val reader = BufferedReader(InputStreamReader(url.openStream()))
//            reader.readText()
//        }
//        val employees: List<Employee> = gson.fromJson(jsonText, typeToken)
//        data.addAll(employees)
//    } catch (e: MalformedURLException) {
//        throw RuntimeException("Invalid URL: $api", e)
//    } catch (e: IOException) {
//        throw RuntimeException("Error reading data from URL: $api", e)
//    } catch (e: Exception) {
//        throw RuntimeException("Error parsing JSON data from URL: $api", e)
//    }
//
//    return data
//}

//suspend fun getApi(api:String):String{
//    try {
//        val url = URL(api)
//        val jsonText = withContext(Dispatchers.IO) {
//            val reader = BufferedReader(InputStreamReader(url.openStream()))
//            reader.readText()
//        }
//        return jsonText
//    }catch (e: MalformedURLException) {
//        throw RuntimeException("Invalid URL: $api", e)
//    } catch (e: IOException) {
//        throw RuntimeException("Error reading data from URL: $api", e)
//    } catch (e: Exception) {
//        throw RuntimeException("Error parsing JSON data from URL: $api", e)
//    }
//}