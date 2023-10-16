package com.syllabus.pq

import androidx.compose.runtime.snapshots.SnapshotStateMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.Random

class QuizRepository {
    private val quizApiService = QuizApiService()

    fun generateUniquePlayerID(students: List<Player>): String {
        val random = Random()
        val chars = "0123456789ABCDEFGHI"
        val idLength = 8
        var randomID: String
        var isUnique: Boolean

        do {
            // Generate a random ID of the specified length
            val idBuilder = StringBuilder()
            for (i in 0 until idLength) {
                val randomChar = chars[random.nextInt(chars.length)]
                idBuilder.append(randomChar)
            }
            randomID = "Q-$idBuilder"

            // Check if the generated ID is unique
            isUnique = students.none { it.id == randomID }
        } while (!isUnique)

        return randomID
    }
    private fun convertJsonToQuizzes(jsonString: String): QuizData {

        //val gson = Gson()
        //return gson.fromJson(jsonString, QuizData::class.java)
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(Option::class.java, OptionDeserializer())
        val gson = gsonBuilder.create()

        return gson.fromJson(jsonString, QuizData::class.java)
    }


    fun getQuizzes():MutableList<QuizData>{
        val finalQuizList:MutableList<QuizData> = mutableListOf()
        return runBlocking {
            val db = withContext(CoroutineScope(Dispatchers.IO).coroutineContext){
                quizApiService.getQuizSources(quizSource.fullDecode())
            }
            for (i in db){
                val data  = quizApiService.fetchApiData(i)
                val quizData = convertJsonToQuizzes(data)
                finalQuizList.add(quizData)
            }
            finalQuizList
        }
    }
    fun getTickets():MutableList<Ticket>{
        val finalTicketList:MutableList<Ticket> = mutableListOf()
        return runBlocking {
            val ticketsApi = withContext(CoroutineScope(Dispatchers.IO).coroutineContext){
                quizApiService.fetchApiData(ticketsSource.fullDecode())
            }
            val gson = Gson()
            val tickets: List<Ticket> = gson.fromJson(ticketsApi, Array<Ticket>::class.java).toList()
            tickets.toMutableList()
        }
    }
    fun getValues():Values{
        return runBlocking {
            val valuesApi = withContext(CoroutineScope(Dispatchers.IO).coroutineContext){
                quizApiService.fetchApiData(valuesSource.fullDecode())
            }
            val gson = Gson()
            val values: Values = gson.fromJson(valuesApi, Values::class.java)
            values
        }
    }
    fun getValuesV2():Values{
        return runBlocking {
            val valuesApi = withContext(CoroutineScope(Dispatchers.IO).coroutineContext){
                quizApiService.fetchApiData(valuesSource_v2.fullDecode())
            }
            val gson = Gson()
            val values: Values = gson.fromJson(valuesApi, Values::class.java)
            values
        }
    }
    fun getBadges():List<Badge>{
        return runBlocking {
            val badgesApi = withContext(CoroutineScope(Dispatchers.IO).coroutineContext){
                quizApiService.fetchApiData(badgesSource.fullDecode())
            }
            val gson = Gson()
            val badges: List<Badge> = gson.fromJson(badgesApi, Array<Badge>::class.java).toList()
            badges
        }
    }
    fun getPlayers():Map<String, Player>{
        val quizDatabase = QuizDatabaseV2()
        return runBlocking {
            val playersApi = withContext(CoroutineScope(Dispatchers.IO).coroutineContext){
                //quizDatabase.readCell(range = dataProvider.values.value.db1.fullDecode())
                quizDatabase.readDocument()

            }

            val gson = Gson()
            val playerType = object : TypeToken<Map<String, Player>>() {}.type
            val players: Map<String, Player> = gson.fromJson(playersApi.decompressString(), playerType)
            players
        }
    }

    fun writeToDb(snapshotStateMap: SnapshotStateMap<String, Player>): Boolean {
        val quizDatabase = QuizDatabaseV2()
        return runBlocking {
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext){
                val data = convertPlayersToJson(snapshotStateMap)
                //val response = quizDatabase.writeCellWithResponse(range = dataProvider.values.value.db1.fullDecode(), value = data)
                val response =quizDatabase.writeDocument(content = data.compressString())
                response
            }
        }
    }

    private fun convertPlayersToJson(snapshotStateMap: SnapshotStateMap<String, Player>): String {
        val gson = Gson()
        val playerJsonMap = mutableMapOf<String, Player>()

        for ((key, player) in snapshotStateMap) {
            playerJsonMap[key] = player
        }
        
        return gson.toJson(playerJsonMap)
    }

}

class OptionDeserializer : JsonDeserializer<Option> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Option {
        val jsonObject = json?.asJsonObject
        val optionsMap = mutableMapOf<String, String>()

        jsonObject?.entrySet()?.forEach { entry ->
            optionsMap[entry.key] = entry.value.asString
        }

        return Option(optionsMap)
    }
}
