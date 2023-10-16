package com.syllabus.pq

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import java.util.Random

class DataProvider {
    fun quizInit(){
        answerHistory  = mutableStateMapOf<String,Results>()
        questionCount = mutableStateOf(0)
        questionIterator = mutableStateOf(0)
        currentScore = mutableStateOf(0)
        currentOptionsList = mutableStateListOf<String>()
        optionsIdMap = mutableStateMapOf()
        confirmationTitle = mutableStateOf("")
        confirmationMessage = mutableStateOf("")
        exitQuiz = mutableStateOf(false)
        currentQuiz = mutableStateOf("")
        selectedOptions = mutableStateListOf<Map.Entry<String, String>>()
    }
    fun initPlayers(){
        val updatedPlayers = quizRepository.getPlayers()
        players = mutableStateMapOf()
        players.putAll(updatedPlayers)
        playerList = mutableStateListOf()
        for(i in dataProvider.players){
            dataProvider.playerList.add(i.value)
        }
    }
    var tickets:MutableList<Ticket> = mutableStateListOf()
    var values = mutableStateOf(Values())
    var values_v2 = mutableStateOf(Values())
    var quizzes:MutableList<QuizData> = mutableStateListOf()
    var badges:MutableList<Badge> = mutableStateListOf()
    var players: SnapshotStateMap<String,Player> = mutableStateMapOf()
    var rankingsLoaded = mutableStateOf(false)
    var loggedInPlayer = mutableStateOf(Player())
    var playerList:MutableList<Player>  = mutableStateListOf()
    var playNoticeBoard = mutableStateOf(true)

    var startThisQuiz = mutableStateOf(QuizData())
    var answerHistory = mutableStateMapOf<String,Results>()
    var searchText = mutableStateOf("")
    var questionCount = mutableStateOf(0)
    var questionIterator = mutableStateOf(0)
    var currentScore = mutableStateOf(0)
    var currentOptionsList = mutableStateListOf<String>()
    var optionsIdMap: SnapshotStateMap<String,String> = mutableStateMapOf()
    var showConfirmation = mutableStateOf(false)
    var confirmationTitle = mutableStateOf("")
    var confirmationMessage = mutableStateOf("")
    var exitQuiz = mutableStateOf(false)
    var currentQuiz = mutableStateOf("")
    var selectedOptions = mutableStateListOf<Map.Entry<String, String>>()
    val ticketIndex = mutableStateOf(0)
    val startIntentShare = mutableStateOf(false)
    val ticketDuration = mutableStateOf(2000)
    val ticketSlideView = mutableStateOf("ticket")
    var ticketReady = mutableStateOf(false)
    var thisTicket = mutableStateOf(Ticket())
    val showInfo = mutableStateOf(false)
    val showRankings = mutableStateOf(false)
    val showProfile = mutableStateOf(false)


}

