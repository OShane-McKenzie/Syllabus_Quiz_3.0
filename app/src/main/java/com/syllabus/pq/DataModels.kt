package com.syllabus.pq


data class Option(
    var options: Map<String, String> = mapOf()
)

data class Question(
    var question: String = "",
    var options: Option = Option(),
    var answer: String = "",
    var notes: String = ""
)

data class QuizData(
    var title: String = "",
    var count: Int = 0,
    var questions: List<Question> = listOf()
)

data class Results(
    var question: String = "",
    var correct: String = "",
    var chosen: String = "",
    var notes: String = "",
    var simpleAnswer:String = "",
    var correctAnswerChosen:Boolean = false,

)

data class ScoreRecord(
    var quiz:String = "",
    var score:Double = 0.0,
    var star:Int = 0
)

data class Player(
    var id:String = "",
    var userName:String = "",
    var password:String = "",
    var stars:Int = 0,
    var badge:Int = 0,
    var showHistory:Boolean = true,
    var history:MutableList<ScoreRecord> = mutableListOf(ScoreRecord())
)

data class Ticket(
    var content:String = "",
    var size:Int = 12,
    var link:String = "",
    var img:String = "",
    var duration:Int = 1000
)

data class Badge(
    var title:String = "",
    var img:String = "",
    var min:Int = 0,
    var max:Int = 4
)
data class Values(
    var ads:Boolean = true,
    var version:Double = 1.0,
    var live:Boolean = false,
    var db1:String = "",
    var db1Active:String = "",
    var dbId:String = "",
    var dba:String = "",
    var app:String = "https://play.google.com/store/apps/details?id=com.syllabus.pq",
    var bw:List<String> = listOf()
)