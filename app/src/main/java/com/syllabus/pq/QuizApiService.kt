package com.syllabus.pq

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

class QuizApiService {
    suspend fun getQuizSources(api: String): List<String> {
        val data: List<String>
        val typeToken = object : TypeToken<List<String>>(){}.type
        val gson = Gson()

        try {
            val url = URL(api)
            val jsonText = withContext(Dispatchers.IO) {
                val reader = BufferedReader(InputStreamReader(url.openStream()))
                reader.readText()
            }
            val tempJsonList: List<String> = gson.fromJson(jsonText, typeToken)
            data = tempJsonList
        } catch (e: MalformedURLException) {
            throw RuntimeException("Invalid URL: $api", e)
        } catch (e: IOException) {
            throw RuntimeException("Error reading data from URL: $api", e)
        } catch (e: JsonSyntaxException) {
            throw RuntimeException("Error parsing JSON data from URL: $api", e)
        }

        return data
    }
    fun printSources(){
        runBlocking {
            val db = withContext(CoroutineScope(Dispatchers.IO).coroutineContext){
                getQuizSources("https://oshane-mckenzie.github.io/syllabus_quiz/sources.json")
            }
            for(i in db){
                println(i)
            }
        }
    }
    suspend fun fetchApiData(api:String):String{
        val url = URL(api)
        val jsonText = withContext(Dispatchers.IO) {
            val reader = BufferedReader(InputStreamReader(url.openStream()))
            reader.readText()
        }
        return jsonText
    }
}

