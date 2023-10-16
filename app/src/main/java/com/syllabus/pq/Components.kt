package com.syllabus.pq

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Locale

@Composable
fun QuestionAnswer(questionData: QuizData){
    val bgColors = listOf(
        Color(0xFFB58ADD),  // Purple
        Color(0xFFEBB2EB),  // Magenta
        Color(0xFF9595F0)   // Blue
    )

    val scrollState = rememberScrollState()
    var backBtnTxt by remember {
        mutableStateOf("Cancel")
    }
    var nextBtnTxt by remember {
        mutableStateOf("Next")
    }

    val maxQuestionIndex = questionData.count - 1
    val currentQuestion = questionData.questions[dataProvider.questionIterator.value]
    dataProvider.currentQuiz.value = questionData.title
    Column(
        Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(bgColors, startY = 0.0f, endY = 2800.0f)
            )
            .verticalScroll(scrollState)
            .padding(3.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        if(dataProvider.values.value.ads){
            val adID1 = stringResource(id = R.string.ad_mob_banner_id)
            AndroidView(
                factory = { context ->
                    AdView(context).apply {
                        setAdSize(AdSize.BANNER)
                        adUnitId = adID1
                        loadAd(AdRequest.Builder().build())
                    }
                }
            )
            Spacer(modifier = Modifier.height(5.dp))
        }
        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ){
            Text(
                questionData.title,
                fontSize = 15.sp,
                color = Color(0xFFDFF5F8),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            Text(text = "Question: ${dataProvider.questionCount.value+1} of ${questionData.count}",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center)
        }
        Spacer(modifier = Modifier.height(15.dp))
        Column(Modifier.wrapContentSize().background(
            brush = Brush.verticalGradient(listOf(Color(0xFFF8F8F8),
                Color(0xFFBAC8DF)
            ), startY = 0.0f, endY = 300.0f),
            shape = RoundedCornerShape(6)
        ).padding(4.dp)){
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                currentQuestion.question,
                fontSize = 20.sp,
                color = Color(0xFF827717),
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color(0xFFC8C9D1),
                        offset = Offset(2.0f, 5.0f),
                        blurRadius = 2f
                    )
                ), textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(5.dp))
        }
        Spacer(modifier = Modifier.height(25.dp))

        for (i in currentQuestion.options.options){
            val isSelected = i.key == dataProvider.answerHistory[currentQuestion.question]?.chosen

            OptionCard(ans = i, allOptions = currentQuestion.options.options) { first, second->
                    for (h in currentQuestion.options.options){
                        if(h in dataProvider.selectedOptions && h != i){
                            dataProvider.selectedOptions.remove(h)
                        }
                    }
                    dataProvider.selectedOptions.add(i)

                    dataProvider.answerHistory[currentQuestion.question] = Results(
                        question = currentQuestion.question,
                        correct = currentQuestion.answer.uppercase(Locale.ROOT) +
                                ") ${currentQuestion.options.options[currentQuestion.answer]}",
                        chosen = first,
                        notes = currentQuestion.notes,
                        simpleAnswer = second.uppercase(Locale.ROOT),
                    )

                    if (second.trim() == currentQuestion.answer.trim() &&
                        dataProvider.answerHistory[currentQuestion.question]?.correctAnswerChosen == false
                    ) {
                        dataProvider.currentScore.value += 1
                        dataProvider.answerHistory[currentQuestion.question]?.correctAnswerChosen =
                            true
                    } else if (second.trim() != currentQuestion.answer.trim() &&
                        dataProvider.answerHistory[currentQuestion.question]?.correctAnswerChosen == true
                    ) {
                        dataProvider.currentScore.value -= 1
                        dataProvider.answerHistory[currentQuestion.question]?.correctAnswerChosen =
                            false
                    }
                }
            Spacer(modifier = Modifier.height(20.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Button(onClick = {
                if(dataProvider.questionIterator.value>0) {
                    dataProvider.questionIterator.value -= 1
                    dataProvider.questionCount.value -= 1
                }else if(dataProvider.questionIterator.value == 0){
                    dataProvider.confirmationTitle.value = "Exit Quiz"
                    dataProvider.confirmationMessage.value = "Are you sure you want to EXIT this quiz?"
                    dataProvider.exitQuiz.value = true
                    dataProvider.showConfirmation.value = true
                }
            }) {
                Text(backBtnTxt)
            }
            if(dataProvider.answerHistory[currentQuestion.question]?.chosen!=null) {
                Text("You answered: ${dataProvider.answerHistory[currentQuestion.question]?.simpleAnswer!!.uppercase(
                    Locale.ROOT
                )}")
            }
            Button(onClick = {
                if(dataProvider.questionIterator.value<maxQuestionIndex) {
                    dataProvider.questionIterator.value += 1
                    dataProvider.questionCount.value += 1
                }else if(dataProvider.questionIterator.value == maxQuestionIndex){
                    dataProvider.confirmationTitle.value = "Submit Quiz"
                    dataProvider.confirmationMessage.value = "Are you sure you want to submit this quiz?"
                    dataProvider.showConfirmation.value = true
                }
            }) {
                Text(nextBtnTxt)
            }
        }
        if(dataProvider.showConfirmation.value){
            Confirm(){
                dataProvider.showConfirmation.value = false
                dataProvider.questionCount.value = questionData.count
                appNavigator.setViewState("results", updateHistory = false)
            }
        }
        LaunchedEffect(dataProvider.questionIterator.value){
            when (dataProvider.questionIterator.value) {
                maxQuestionIndex -> {
                    nextBtnTxt = "Submit"
                }
                0 -> {
                    backBtnTxt = "Cancel"
                }
                in 1 until maxQuestionIndex -> {
                    nextBtnTxt = "Next"
                    backBtnTxt = "Back"
                }
            }
        }

    }
}

@Composable
fun OptionCard(
    ans: Map.Entry<String, String>,
    allOptions: Map<String,String>,
    callBack: (String, String) -> Unit = { _, _ -> }
) {
    val selectedColor by remember {
        mutableStateOf(Color(0xFF0452F8))
    }
    var highlight by remember {
        mutableStateOf(false)
    }

    highlight = ans in dataProvider.selectedOptions

    val borderColor by animateColorAsState(targetValue = if (highlight) selectedColor else Color.Transparent)

    Column(
        modifier = Modifier
            .border(
                BorderStroke(width = 2.dp, color = borderColor),
                shape = RoundedCornerShape(6)
            )
            .wrapContentHeight()
            .fillMaxWidth(0.9f)
            .background(
                brush = Brush.verticalGradient(listOf(Color(0xFFF8F8F8),
                    Color(0xFFE0D6EB)
                ), startY = 0.0f, endY = 300.0f),
                shape = RoundedCornerShape(6)
            )
            .padding(2.dp)
            .clickable(onClick = {
                callBack("${ans.key.uppercase(Locale.ROOT)}) ${ans.value}", ans.key)
            }),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            color = Color(0xFF0C6F5E),
            text = "${ans.key.uppercase(Locale.ROOT)}) ${ans.value}",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun QuizThumbnail(quizData: QuizData){
    val bgColors = listOf(
        Color(0xFF640B6F),  // Purple
        Color(0xFF73C2E6),  // Magenta
    )
    Column (
        modifier = Modifier
            .border(BorderStroke(width = 1.dp, color = Color.White), shape = RoundedCornerShape(6))
            .height(80.dp)
            .width(120.dp)
            .background(
                brush = Brush.verticalGradient(bgColors, startY = 0.0f, endY = 500.0f), shape = RoundedCornerShape(6)
            )
            //.background(color = Color(0xFFFFFFFF).copy(alpha = 0.8f), shape = RoundedCornerShape(6))
            .padding(2.dp)
            .clickable {
                dataProvider.quizInit()
                dataProvider.startThisQuiz.value = quizData
                appNavigator.setViewState("quiz")
            }
        ,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(color = Color(0xFFF8F7F7), text = quizData.title, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(4.dp))
        Text(color = Color(0xFFF8FAFA), text = "Questions: ${quizData.count}", fontSize = 12.sp, textAlign = TextAlign.Center)
    }
}

@Composable
fun AdsStack(){
    val adID1 = stringResource(id = R.string.ad_mob_banner_id)
    val adID2 = stringResource(id = R.string.ad_mod_banner_id2)
    val adID3 = stringResource(id = R.string.ad_mob_banner_id3)
    if(dataProvider.values.value.ads) {
        AndroidView(
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = adID1
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
        Spacer(modifier = Modifier.height(1.dp))
        AndroidView(
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = adID2
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
        Spacer(modifier = Modifier.height(1.dp))
        AndroidView(
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = adID3
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }else{
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
            ){ Image(painter = painterResource(id = R.drawable.placeholder), contentDescription = "") }
    }
}

@Composable
fun TicketSlides(ticket: Ticket){
    Row(
        Modifier
            .fillMaxSize()
            .padding(5.dp)
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        val scrollState = rememberScrollState()
        val context = LocalContext.current
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(1f)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                ticket.content,
                fontSize = ticket.size.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = Color(0xFF0B7B80),
                modifier = Modifier.clickable(onClick = {
                    dataProvider.startIntentShare.value = !dataProvider.startIntentShare.value
                    if (dataProvider.startIntentShare.value && ticket.link != "") {
                        val sendIntent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(ticket.link)
                        }

                        ContextCompat.startActivity(
                            context,
                            Intent.createChooser(sendIntent, null),
                            null
                        )

                    }
                    if(dataProvider.startIntentShare.value){
                        dataProvider.startIntentShare.value = !dataProvider.startIntentShare.value
                    }
                })
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = ticket.img)
                .apply<ImageRequest.Builder>(block = fun ImageRequest.Builder.() {
                    error(R.drawable.placeholder)
                    placeholder(R.drawable.placeholder)
                }).build()
        )
        Image(
            painter = painter,
            contentDescription = "Image loaded from URL",
            modifier = Modifier
                //.padding(top = 8.dp)
                //.aspectRatio(0.8f)
                .clickable(onClick = {
                    dataProvider.startIntentShare.value = !dataProvider.startIntentShare.value
                    if (dataProvider.startIntentShare.value && ticket.link != "") {
                        val sendIntent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(ticket.link)
                        }

                        ContextCompat.startActivity(
                            context,
                            Intent.createChooser(sendIntent, null),
                            null
                        )
                    }
                    if (dataProvider.startIntentShare.value) {
                        dataProvider.startIntentShare.value = !dataProvider.startIntentShare.value
                    }
                })
                .weight(1f)
                .fillMaxHeight()
        )
        dataProvider.ticketDuration.value = ticket.duration
    }
}

@Composable
fun Confirm(runTask:()->Unit={}){
    AlertDialog(
        onDismissRequest = {
            dataProvider.showConfirmation.value = false
            dataProvider.exitQuiz.value = false
                           },
        title = { Text(text = dataProvider.confirmationTitle.value, color = Color(0xFF0B7B80)) },
        text = { Text(text = dataProvider.confirmationMessage.value, color = Color(0xFF0B7B80)) },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(0.5f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    onClick = {
                        if (dataProvider.exitQuiz.value) {
                            dataProvider.exitQuiz.value = false
                            appNavigator.setViewState("home", updateHistory = false, execTask = true){
                                dataProvider.quizInit()
                            }
                        }else{
                            runTask()
                        }

                        dataProvider.showConfirmation.value = false
                    }
                ) {
                    Text(text = "Yes")
                }
            }
        },
        dismissButton = {
            Row(
                modifier = Modifier.fillMaxWidth(0.4f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom
            ){
                Button(
                    onClick = {
                        if(dataProvider.exitQuiz.value){
                            dataProvider.exitQuiz.value = false
                        }
                        dataProvider.showConfirmation.value = false
                    }
                ) {
                    Text(text = "Cancel")
                }
            }
        },

    )
}

@Composable
fun HistoryDialog(player:Player,task:(Boolean)->Unit={}){
    val thisScrollState = rememberScrollState()
    AlertDialog(
        onDismissRequest = {
        },
        title = { Text(text = "${player.userName}'s history.", color = Color(0xFF0B7B80)) },
        text =
        {
            Column(modifier = Modifier
                .height(200.dp)
                .wrapContentWidth()
                .verticalScroll(thisScrollState)) {
                player.history.forEach{i->
                    if(i.quiz!="") {
                        InfoText(
                            boldness = FontWeight.Normal,
                            text = "Quiz: ${i.quiz}\nScore: ${i.score}%\nStar earned: ${i.star}",
                            spaceBelow = 8, size = 14, color = Color(0xFF0B7B80)
                        )
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp), color = Color(0xFF0B7B80)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(0.5f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    onClick = {
                        task(false)
                    }
                ) {
                    Text(text = "OK")
                }
            }
        }
    )
}

@Composable
fun ResultsView(){
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val percentage = ((dataProvider.currentScore.value.toDouble() / dataProvider.questionCount.value.toDouble()) * 100.0)
    val roundedPercentage = String.format("%.3f", percentage)
    val stars = if(percentage in 80.000..99.000){
        1
    }else if (percentage >= 100){
        2
    }else{
        0
    }
    fun updatePlayerRecord(quiz:String = dataProvider.currentQuiz.value,score:Double,star:Int){
        var recordFound = false
        var setQuiz:ScoreRecord = ScoreRecord()
        var index = -1
        for (i in dataProvider.loggedInPlayer.value.history){
            index++
            if (i.quiz == dataProvider.currentQuiz.value){
                recordFound = true
                setQuiz = i
                break
            }
        }
        if (recordFound){
            if(setQuiz.star<star){
                setQuiz.star = star
            }
            if(setQuiz.score<score){
                if(score>100.0){
                    setQuiz.score = 100.0
                }else{
                    setQuiz.score = score
                }

            }
            dataProvider.loggedInPlayer.value.history[index]=setQuiz
        }else{
            setQuiz.quiz = quiz
            setQuiz.star = star
            if(score>100.0){
                setQuiz.score = 100.0
            }else{
                setQuiz.score = score
            }

            dataProvider.loggedInPlayer.value.history.add(setQuiz)
        }
        var totalStars = 0
        for(i in dataProvider.loggedInPlayer.value.history){
           totalStars += i.star
        }

        dataProvider.loggedInPlayer.value.stars = totalStars

    }

    LaunchedEffect(Unit){
        delay(300)
        withContext(Dispatchers.IO){
            if(isInternetConnected(context)){
                dataProvider.initPlayers()
                updatePlayerRecord(score = roundedPercentage.toDouble(), star = stars)
                dataProvider.players[dataProvider.loggedInPlayer.value.id] = dataProvider.loggedInPlayer.value
                val update = quizRepository.writeToDb(dataProvider.players)
                if (!update){
                    withContext(Dispatchers.Main){
                        getToast(context,"Unable to update record at this time.")
                    }
                }else{
                    withContext(Dispatchers.Main){
                        getToast(context,"Record has been saved.")
                    }
                }
            }else{
                withContext(Dispatchers.Main){
                    getToast(context,"Unable to update record at this time. No internet connection.")
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(5.dp))
        if(dataProvider.values.value.ads){
            val adID1 = stringResource(id = R.string.ad_mod_banner_id2)
            AndroidView(
                factory = { context ->
                    AdView(context).apply {
                        setAdSize(AdSize.BANNER)
                        adUnitId = adID1
                        loadAd(AdRequest.Builder().build())
                    }
                }
            )
            Spacer(modifier = Modifier.height(5.dp))
        }
        Row(
            modifier = Modifier
                .fillMaxHeight(0.1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text("Quiz: ${dataProvider.currentQuiz.value}}",
                textAlign = TextAlign.Center, modifier = Modifier.weight(1f), fontSize = 11.sp)
            Text("Your score: $roundedPercentage%",
                textAlign = TextAlign.Center, modifier = Modifier.weight(1f), fontSize = 11.sp)
            Text("Stars earned: $stars",
                textAlign = TextAlign.Center, modifier = Modifier.weight(1f), fontSize = 11.sp)
        }
        Divider(modifier = Modifier.height(1.dp), thickness = 1.dp)
        Spacer(modifier = Modifier.height(5.dp))
        Column(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .verticalScroll(scrollState),verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (i in dataProvider.answerHistory){
                ResultsCard(results = i.value)
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}

@Composable
fun ResultsCard(results: Results){

    val selectedColor by remember {
        mutableStateOf(Color(0xFF090808))
    }
    var textColor by remember {
        mutableStateOf(Color(0xFF02D50A))
    }
    if(!results.correctAnswerChosen){
        textColor = Color(0xFFF71A0A)
    }
    val report = """
        Question: ${results.question}
        
        You answered: ${results.chosen}
        
        Correct answer: ${results.correct}
        
        Comment: ${results.notes}
    """.trimIndent()
    Column (
        modifier = Modifier
            .border(
                BorderStroke(width = 2.dp, color = selectedColor),
                shape = RoundedCornerShape(6)
            )
            .wrapContentHeight()
            .fillMaxWidth(0.9f)
            .background(color = Color(0xFF06117E).copy(alpha = 1.0f), shape = RoundedCornerShape(6))
            .padding(5.dp)
            ,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(2.dp))
        Text(report, color = textColor)
        Spacer(modifier = Modifier.height(2.dp))
    }
}

@Composable
fun SplashScreen(){
    val thisContext = LocalContext.current
    var id by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var isLoggedIn by remember {
        mutableStateOf(false)
    }
    var isOffline by remember {
        mutableStateOf(false)
    }
    fun getNetworkStatus(): Boolean {
        return runBlocking {
            val net = withContext(CoroutineScope(Dispatchers.IO).coroutineContext){
                isInternetConnected(thisContext)
            }
            net
        }
    }
    fun loadCredentials(){
        val sharedPreferences = thisContext.getSharedPreferences("credentials", MODE_PRIVATE)
        id = sharedPreferences.getString("id", "") ?: ""
        pass = sharedPreferences.getString("password", "") ?: ""
    }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFFF0F6F7)
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(5.dp))
        if(isOffline == false) {
            Image(
                painter = painterResource(id = R.drawable.placeholder),
                contentDescription = "Icon",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(percent = 100))
                    .graphicsLayer(alpha = 1.0f)
            )
        }else{
            Offline("No network detected.")
        }
        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO){
                if (getNetworkStatus()){
                    dataProvider.quizzes = quizRepository.getQuizzes().toMutableStateList()
                    dataProvider.tickets = quizRepository.getTickets().toMutableStateList()
                    //dataProvider.values.value = quizRepository.getValues()
                    dataProvider.values.value = quizRepository.getValuesV2()
                    dataProvider.badges = quizRepository.getBadges().toMutableStateList()
                    delay(2000)
                    if(dataProvider.values.value.live) {
                        if(dataProvider.values.value.version <=  version.fullDecode().toDouble()){

                            dataProvider.players.putAll(quizRepository.getPlayers())
                            for(i in dataProvider.players){
                                dataProvider.playerList.add(i.value)
                            }
                            loadCredentials()
                            if(id!=""&&pass!=""){
                                for (i in dataProvider.players) {
                                    if (i.key == id.trim()) {
                                        if (pass.trim() == i.value.password) {
                                            dataProvider.loggedInPlayer.value = dataProvider.players[i.key]!!
                                            isLoggedIn = true
                                            break
                                        }else{
                                            isLoggedIn = false
                                            break
                                        }
                                    }
                                }
                                if (isLoggedIn) {

                                    appNavigator.setViewState("home", updateHistory = false){
                                        isLoggedIn = false
                                    }
                                }else{
                                    appNavigator.setViewState("login", updateHistory = false)
                                }
                            }else{
                                appNavigator.setViewState("login", updateHistory = false)
                            }

                        }else{
                            println(version.fullDecode().toDouble())
                            println(dataProvider.values.value.version)
                            appNavigator.setViewState("update", updateHistory = false)
                        }
                    }else{
                        appNavigator.setViewState("offline", updateHistory = false)
                    }
                }else{
                    isOffline = true
                }
            }
        }
    }
}

@Composable
fun UpdateApp(){
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFFF0F6F7)
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("New version available.")
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = {

            val sendIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(dataProvider.values.value.app)
            }

            ContextCompat.startActivity(
                context,
                Intent.createChooser(sendIntent, null),
                null
            )
        }) {
            Text("Update App")
        }
    }
}

@Composable
fun Offline(msg:String="This App is offline."){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFFF0F6F7)
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(msg)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(){
    val bgColors = listOf(
        Color(0xFFF3C7F8),  // Purple
        Color(0xFFD9E9F0),  // Magenta
    )
    val scrollState = rememberScrollState()
    var nickText by remember {
        mutableStateOf("")
    }
    var idText by remember {
        mutableStateOf("")
    }
    var idText2 by remember {
        mutableStateOf("null")
    }
    var isLoggedIn by remember {
        mutableStateOf(false)
    }
    var passwordText by remember {
        mutableStateOf("")
    }
    var createAccount by remember {
        mutableStateOf("Create new account.")
    }
    var isCreateAccount by remember {
        mutableStateOf(false)
    }
    var loginBtnTxt by remember {
        mutableStateOf("Login")
    }
    var helpText by remember {
        mutableStateOf("")
    }
    var clearIdField by remember {
        mutableStateOf(true)
    }
    val maxText = 15
    if(isCreateAccount){
        createAccount = "Already have an account? Login."
        loginBtnTxt = "Register"
        helpText = "This is your Player ID. Save it."
        clearIdField = false
    }else{
        createAccount = "Create new account."
        loginBtnTxt = "Login"
        if (!clearIdField){
            idText = ""
            idText2 = "null"
            clearIdField = true
        }
        //idText = ""
        //idText2 = "null"
        helpText = ""
    }
    val context = LocalContext.current
    fun getToast(context: Context, msg:String){
        Toast.makeText(
            context,
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }
    var id by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    // Load data from SharedPreferences when the screen is first created
    fun saveCredentials(id:String,pass:String){
        val sharedPreferences = context.getSharedPreferences("credentials", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("id", id)
        editor.putString("password", pass)
        editor.apply()
    }
    fun loadCredentials(){
        val sharedPreferences = context.getSharedPreferences("credentials", MODE_PRIVATE)
        id = sharedPreferences.getString("id", "") ?: ""
        pass = sharedPreferences.getString("password", "") ?: ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(bgColors, startY = 0.0f, endY = 2000.0f))
            .padding(5.dp)
            .verticalScroll(scrollState)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {


        Spacer(modifier = Modifier.height(10.dp))
        Text("Welcome. Login or create a new account.",
            fontSize = 20.sp,
            color = Color(0xFF2C2A2E),
            fontWeight = FontWeight.Medium,
            style = TextStyle(
                shadow = Shadow(
                    color = Color(0xFFF2F2F7),
                    offset = Offset(2.0f, 5.0f),
                    blurRadius = 2f
                )
            ), textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(15.dp))
        Image(
            painter = painterResource(id = R.drawable.placeholder),
            contentDescription = "Icon",
            modifier = Modifier
                .size(160.dp)
                .clip(RoundedCornerShape(percent = 100))
                .graphicsLayer(alpha = 1.0f)
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.8f),
            value = idText, onValueChange = {
                idText=it
                idText2=it
                                            },
            label = {Text("Player ID")},
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color(0xFFB301B3),
                containerColor = Color(0xFFFDFDFD)
            ),
            enabled = !isCreateAccount,
            singleLine = true,
            supportingText = {
                Text(
                    text = helpText,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            }
        )
        if(isCreateAccount){
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = nickText, onValueChange =
                {
                    //nickText=it
                    if (it.length <= maxText) {
                        nickText = it
                    }
                },
                label = {Text("Nick Name")},
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color(0xFFB301B3),
                    containerColor = Color(0xFFFDFDFD)
                ),
                singleLine = true,
                supportingText = {
                    Text(
                        text = "${nickText.length} / $maxText",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                    )
                }
            )
        }else{
            Spacer(modifier = Modifier.width(1.dp))
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.8f),
            value = passwordText, onValueChange =
            {
                //passwordText=it
                if (it.length <= maxText) {
                    passwordText = it
                }
            },
            label = {Text("Password")},
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color(0xFFB301B3),
                containerColor = Color(0xFFFDFDFD)
            ),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            supportingText = {
                Text(
                    text = "${passwordText.length} / $maxText",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            }

        )
        Spacer(modifier = Modifier.height(15.dp))
        Button(onClick =
        {
            if(!isCreateAccount){
                for (i in dataProvider.players) {
                    if (i.key == idText.trim()) {
                        if (passwordText.trim().fullEncode() == i.value.password) {
                            dataProvider.loggedInPlayer.value = dataProvider.players[i.key]!!
                            isLoggedIn = true
                            break
                        }
                    }
                }
                if (isLoggedIn) {
                    saveCredentials(dataProvider.loggedInPlayer.value.id,
                        dataProvider.loggedInPlayer.value.password)
                    appNavigator.setViewState("home", updateHistory = false){
                        isLoggedIn = false
                    }
                }else{
                    getToast(context,"Login failed. Check credentials.")
                }

            }else{
                if(nickText!="" && passwordText!=""){
                    var bw = false
                    dataProvider.values.value.bw.forEach {
                        if(nickText.lowercase(Locale.ROOT).contains(it)) {
                            bw = true
                        }
                    }
                    if (bw){
                        getToast(context = context,"Inappropriate language is not allowed.")
                        bw = false
                    }else{
                        dataProvider.loggedInPlayer.value = Player(
                            id = idText,
                            userName = nickText,
                            password = passwordText.fullEncode()
                        )
                        dataProvider.initPlayers()
                        dataProvider.players[idText]=dataProvider.loggedInPlayer.value

                        val saveOperation = quizRepository.writeToDb(dataProvider.players)

                        if(saveOperation){
                            saveCredentials(dataProvider.loggedInPlayer.value.id,
                                dataProvider.loggedInPlayer.value.password)

                            appNavigator.setViewState("success", updateHistory = false)
                        }else{
                            getToast(context = context,"Unable to create account at this time. Contact admin for help if this persists.")
                        }
                    }
                }else{
                    getToast(context = context,"Required fields are empty.")
                }
            }
        },
            modifier = Modifier.fillMaxWidth(0.8f)) {
            Text(text = loginBtnTxt, fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(30.dp))

        Text(createAccount, Modifier.clickable
        {
            val newPlayerID = quizRepository.generateUniquePlayerID(dataProvider.playerList)
            idText = newPlayerID
            idText2 = idText
            isCreateAccount = !isCreateAccount
        },
            color = Color(0xFF0860E9), fontSize = 18.sp,
            fontWeight = FontWeight.Medium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountCreated(){
    val bgColors = listOf(
        Color(0xFFF3C7F8),
        Color(0xFFD9E9F0),
    )
    Column(modifier = Modifier
        .fillMaxSize()
        .background(brush = Brush.verticalGradient(bgColors, startY = 0.0f, endY = 2000.0f))
        .padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        val msg = """
            ACCOUNT SUCCESSFULLY CREATED!
            
            Player ID: ${dataProvider.loggedInPlayer.value.id}
            
            Nick Name: ${dataProvider.loggedInPlayer.value.userName}
            
            Your Password: ${dataProvider.loggedInPlayer.value.password.fullDecode()}
            
            Copy and save the above information and do not share them with anyone else.
            You will need your Player ID and password to login in the future.
        """.trimIndent()

        OutlinedTextField(value = msg , onValueChange = {},colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color(0xFFB301B3),
            containerColor = Color(0xFFFDFDFD)
        ))
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = { appNavigator.setViewState("home", updateHistory = false) }) {
            Text("Next")
        }
    }
}

@Composable
fun InfoText(text:String = "Header",
         size:Int = 18,
         boldness:FontWeight = FontWeight.ExtraBold,
         color: Color = Color(0xFFFDFDFD),
         spaceBelow:Int = 4,
             indent: Int = 0
           ){
    Column(modifier = Modifier
        .wrapContentSize()
        .padding(start = indent.dp)
    ){ Text(text, fontSize = size.sp, fontWeight = boldness, color = color) }
    Spacer(modifier = Modifier.height(spaceBelow.dp))
}
@Composable
fun BadgeImage(badge:Badge, size:Int = 100,indent: Int = 0){
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(data = badge.img)
            .apply<ImageRequest.Builder>(block = fun ImageRequest.Builder.() {
                error(R.drawable.placeholder)
                placeholder(R.drawable.placeholder)
            }).build()
    )
    Column(modifier = Modifier
        .wrapContentSize()
        .padding(start = indent.dp)){
        Image(
            painter = painter,
            contentDescription = "badge",
            Modifier
                .size(size.dp)
                .clip(CircleShape)
        )
    }
}
@Composable
fun Info(){
    val scrollState = rememberScrollState()
    var infoSize by remember {
        mutableStateOf(0.2f)
    }
    var showDetails by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit){
        if(infoSize < 0.9f){
            while (infoSize < 0.9f){
                infoSize+=0.1f
                delay(20)
            }
            infoSize+=0.07f
            showDetails = true
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize(infoSize)
            .background(color = Color(0xFF1F0888).copy(alpha = 0.8f), shape = RoundedCornerShape(6))
            .border(
                BorderStroke(width = 2.dp, color = Color(0xFFF7F7F8)),
                shape = RoundedCornerShape(6)
            )
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Button(onClick = { dataProvider.showInfo.value = false}) {
            Text(text ="Close")
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            if(showDetails){
                InfoText(text = "Welcome to the Promotion Quiz App!", spaceBelow = 10)
                InfoText(boldness = FontWeight.Bold, text = "Rules:")
                InfoText(boldness = FontWeight.SemiBold,text = "1. Quiz Completion:", spaceBelow = 3, indent = 5, size = 16)
                InfoText(
                    boldness = FontWeight.Normal,
                    text = "- Players can choose quizzes from the available list and complete them.\n"+"- Scoring 80% to 99% awards the player 1 star, while a perfect score of 100% earns 2 stars.",
                    spaceBelow = 4,
                    indent = 15,
                    size = 14
                )
                InfoText(boldness = FontWeight.SemiBold,text = "Ranking System:",
                    spaceBelow = 3, indent = 5, size = 16)
                InfoText(
                    boldness = FontWeight.Normal,
                    text = "- The ranking section displays players based on their star achievements.\n"+"- Players with the most stars are positioned at the top of the ranking list.",
                    spaceBelow = 4,
                    indent = 15,
                    size = 14
                )
                InfoText(boldness = FontWeight.SemiBold,text = "Badge Rewards:",
                    spaceBelow = 3, indent = 5, size = 16)
                InfoText(
                    boldness = FontWeight.Normal,
                    text = "- Stars earned unlock various badges, each representing a different skill level.\n"+"- Achieve the following star ranges to earn ",
                    spaceBelow = 3,
                    indent = 15,
                    size = 14
                )
                InfoText(
                    boldness = FontWeight.Normal,
                    text = "badges:",
                    spaceBelow = 4,
                    indent = 15,
                    size = 14
                )
                InfoText(
                    boldness = FontWeight.Black,
                    text = "Scout (0-4 Stars):",
                    spaceBelow = 4,
                    indent = 25,
                    size = 14
                )
                BadgeImage(badge = dataProvider.badges[0], indent = 25)
                Spacer(modifier = Modifier.height(4.dp))


                InfoText(
                    boldness = FontWeight.Black,
                    text = "Man-at-Arms (5-9 Stars):",
                    spaceBelow = 4,
                    indent = 25,
                    size = 14
                )
                BadgeImage(badge = dataProvider.badges[1], indent = 25)
                Spacer(modifier = Modifier.height(8.dp))


                InfoText(
                    boldness = FontWeight.Black,
                    text = "Long Sword Swordsman (10-14 Stars):",
                    spaceBelow = 4,
                    indent = 25,
                    size = 14
                )
                BadgeImage(badge = dataProvider.badges[2], indent = 25)
                Spacer(modifier = Modifier.height(8.dp))


                InfoText(
                    boldness = FontWeight.Black,
                    text = "Two-Handed Swordsman (15-19 Stars):",
                    spaceBelow = 4,
                    indent = 25,
                    size = 14
                )
                BadgeImage(badge = dataProvider.badges[3], indent = 25)
                Spacer(modifier = Modifier.height(8.dp))


                InfoText(
                    boldness = FontWeight.Black,
                    text = "Champion (20-24 Stars):",
                    spaceBelow = 4,
                    indent = 25,
                    size = 14
                )
                BadgeImage(badge = dataProvider.badges[4], indent = 25)
                Spacer(modifier = Modifier.height(8.dp))


                InfoText(
                    boldness = FontWeight.Black,
                    text = "Hero (25+ Stars):",
                    spaceBelow = 4,
                    indent = 25,
                    size = 14
                )
                BadgeImage(badge = dataProvider.badges[5], indent = 25)
                Spacer(modifier = Modifier.height(8.dp))

                InfoText(boldness = FontWeight.SemiBold,text = "Competition Seasons:",
                    spaceBelow = 3, indent = 5, size = 16)
                InfoText(
                    boldness = FontWeight.Normal,
                    text = "- A competition season lasts for two months.\n" +
                            "- At the end of each season, all scores and stars are reset, giving everyone a fresh start.",
                    spaceBelow = 4,
                    indent = 15,
                    size = 14
                )
                InfoText(boldness = FontWeight.SemiBold,text = "Notice Board:",
                    spaceBelow = 3, indent = 5, size = 16)
                InfoText(
                    boldness = FontWeight.Normal,
                    text = "- The player with the most stars from the previous season has their name proudly displayed on the Notice Board within the app.",
                    spaceBelow = 8,
                    indent = 15,
                    size = 14
                )
                InfoText(boldness = FontWeight.SemiBold,text = "Achievement Stars:",
                    spaceBelow = 3, indent = 5, size = 16)
                InfoText(
                    boldness = FontWeight.Normal,
                    text = "- Players earn stars based on their quiz performance. 80% to 99% correct answers grant 1 star, while 100% correct answers grant 2 stars.\n" +
                            "- Accumulate stars to climb the rankings and earn prestigious badges.",
                    spaceBelow = 8,
                    indent = 15,
                    size = 14
                )
                InfoText(text = "Important Notes:")
                InfoText(boldness = FontWeight.Bold, text = "- Fair Play:", indent = 5)
                InfoText(
                    boldness = FontWeight.Normal,
                    text = "Please ensure fair play and honest attempts during the quizzes. Cheating or exploiting the system will result in disqualification.",
                    spaceBelow = 8,
                    indent = 15,
                    size = 14
                )
                InfoText(boldness = FontWeight.Bold, text = "- Seasonal Reset:", indent = 5)
                InfoText(
                    boldness = FontWeight.Normal,
                    text = "Remember, stars and scores are reset at the end of each season, providing everyone with an equal chance to compete again.",
                    spaceBelow = 15,
                    indent = 15,
                    size = 14
                )
                InfoText(boldness = FontWeight.Bold, text = "Thank you for being a part of our quiz community! Enjoy the quizzes, aim for the stars, and have fun competing with fellow members! If you have any questions or concerns, feel free to contact our support team.\n" +
                        "\n" +
                        "Best regards,\n" +
                        "\n" +
                        "O'Shane McKenzie")
            }
        }
    }
}

@Composable
fun PlayerCard(player: Player){
    val rememberedPlayer by remember {
        mutableStateOf(player)
    }
    var playerBadge by remember {
        mutableStateOf(Badge())
    }
    var itemLoaded by remember {
        mutableStateOf(false)
    }
    val starList:MutableList<Int> = remember{ mutableStateListOf() }
    val maxSize = 25
    var starPos = 0
    if(!itemLoaded) {
        for (i in dataProvider.badges) {
            if (rememberedPlayer.stars >= i.min && rememberedPlayer.stars <= i.max) {
                playerBadge = i
            } else if (rememberedPlayer.stars > i.max) {
                playerBadge = i
            }
        }

        for (i in 1..maxSize) {
            starList.add(R.drawable.star_holder)
        }

        for (i in 1..rememberedPlayer.stars) {
            starList[starPos] = R.drawable.star
            starPos++
        }
        itemLoaded = true
    }
    var showHistory by remember {
        mutableStateOf(false)
    }
    val scrollState = rememberScrollState()
    Column(modifier = Modifier
        .wrapContentSize()
        .border(
            BorderStroke(width = 2.dp, color = Color(0xFFF7F7F8)),
            shape = RoundedCornerShape(6)
        )
        .padding(5.dp)) {
        Spacer(Modifier.height(5.dp))
        Row(modifier = Modifier.wrapContentSize(), horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically) {
            Spacer(Modifier.width(5.dp))
            BadgeImage(badge = playerBadge, size = 80)
            Divider(
                modifier = Modifier
                    .height(80.dp)
                    .width(1.dp), color = Color(0xFFFCFCFC)
            )
            Spacer(modifier = Modifier.width(5.dp))
            InfoText(boldness = FontWeight.Medium, text = "${rememberedPlayer.userName}\nStars: ${rememberedPlayer.stars}\nBadge: ${playerBadge.title}", spaceBelow = 0)
            Spacer(Modifier.width(5.dp))
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(modifier = Modifier
            .wrapContentSize()
            .horizontalScroll(scrollState), horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically){
            Spacer(Modifier.width(5.dp))
            for (i in starList){
                Image(
                    painter = painterResource(id = i),
                    contentDescription = "star",
                    Modifier
                        .size(30.dp)
                )
            }
            Spacer(Modifier.width(5.dp))
        }
        Spacer(Modifier.height(5.dp))
        Button(onClick = { showHistory = !showHistory }, enabled = rememberedPlayer.showHistory) {
            Text("Score History")
        }
        if(showHistory){
            HistoryDialog(player = rememberedPlayer){
                showHistory=it
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Rankings(){
    var rankingSize by remember {
        mutableStateOf(0.2f)
    }
    var showDetails by remember {
        mutableStateOf(false)
    }
    var filterText by remember { mutableStateOf("") }
    val context = LocalContext.current
    LaunchedEffect(Unit){
        if(!dataProvider.rankingsLoaded.value) {
            if (rankingSize < 0.9f) {
                while (rankingSize < 0.9f) {
                    rankingSize += 0.1f
                    delay(20)
                }
                rankingSize += 0.07f
                if (isInternetConnected(context)) {
                    withContext(Dispatchers.IO) {
                        dataProvider.initPlayers()
                        showDetails = true
                        dataProvider.rankingsLoaded.value = true
                        dataProvider.playNoticeBoard.value = false
                    }
                }
            }
        }
    }

    val filteredPlayers = dataProvider.players.values.filter {
        it.userName.contains(filterText, ignoreCase = true)||it.stars.toString().contains(filterText, ignoreCase = true)
    }.sortedByDescending { it.stars}

        Column(
        modifier = Modifier
            .fillMaxSize(rankingSize)
            .background(color = Color(0xFF006064).copy(alpha = 0.8f), shape = RoundedCornerShape(6))
            .border(
                BorderStroke(width = 2.dp, color = Color(0xFFF7F7F8)),
                shape = RoundedCornerShape(6)
            )
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Button(onClick = {
            dataProvider.showRankings.value = false
            dataProvider.rankingsLoaded.value = false
            dataProvider.playNoticeBoard.value = true
        }) {
            Text(text ="Close")
        }
            OutlinedTextField(
                value = filterText,
                onValueChange = { filterText = it },
                label = { Text("Filter") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color(0xFFB301B3),
                    containerColor = Color(0xFFFDFDFD)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ){
            if(showDetails) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        //.verticalScroll(scrollState)
                ) {
                    items(filteredPlayers) { player ->
                        PlayerCard(player = player)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(player: Player= dataProvider.loggedInPlayer.value){
    var profileSize by remember {
        mutableStateOf(0.2f)
    }
    var showDetails by remember {
        mutableStateOf(false)
    }
    var newPassword by remember{
        mutableStateOf("")
    }
    var oldPassword by remember{
        mutableStateOf("")
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    var showDialog2 by remember {
        mutableStateOf(false)
    }
    var isChecked by remember{
        mutableStateOf(player.showHistory)
    }
    var changePassword by remember{
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val maxText = 15
    val context = LocalContext.current
    LaunchedEffect(Unit){
        if (profileSize < 0.9f) {
            while (profileSize < 0.9f) {
                profileSize += 0.1f
                delay(20)
            }
            profileSize += 0.07f
            showDetails = true
        }

    }
    Column (
        modifier = Modifier
            .fillMaxSize(profileSize)
            .background(color = Color(0xFF827717).copy(alpha = 0.8f), shape = RoundedCornerShape(6))
            .border(
                BorderStroke(width = 2.dp, color = Color(0xFFF7F7F8)),
                shape = RoundedCornerShape(6)
            )
            .padding(10.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
        ){
        Button(onClick = { dataProvider.showProfile.value= false }) {
            Text("Close")
        }

        Spacer(modifier = Modifier.height(5.dp))
        if (showDetails) {
            InfoText(
                boldness = FontWeight.Bold,
                text = "ID: ${player.id}",
                spaceBelow = 8
            )
            InfoText(
                boldness = FontWeight.Bold,
                text = "Nick Name: ${player.userName}",
                spaceBelow = 8
            )
            Button(onClick = {
                val sharedPreferences = context.getSharedPreferences("credentials", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("id", "")
                editor.putString("password", "")
                editor.apply()

                dataProvider.loggedInPlayer.value = Player()
                dataProvider.showProfile.value = false
                appNavigator.setViewState("login", updateHistory = false, clearHistory = true){
                    dataProvider.showProfile.value = false
                }
            }) {
                Text(text = "Log out")
            }
            Spacer(modifier = Modifier.height(5.dp))
            Button(
                onClick =
                {

                    showDialog2 = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Delete account")
            }
            if(showDialog2){
                SaveAlert(title = "Delete Account","Are you sure you want to delete your account?\nThis task cannot be undone."){
                    if(it) {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                val sharedPreferences = context.getSharedPreferences(
                                    "credentials",
                                    Context.MODE_PRIVATE
                                )
                                val editor = sharedPreferences.edit()
                                editor.putString("id", "")
                                editor.putString("password", "")
                                editor.apply()
                                dataProvider.initPlayers()
                                dataProvider.players.remove(dataProvider.loggedInPlayer.value.id)
                                dataProvider.loggedInPlayer.value = Player()
                                val update = quizRepository.writeToDb(dataProvider.players)
                                if (update) {
                                    withContext(Dispatchers.Main) {
                                        getToast(
                                            context = context,
                                            "Your account has been deleted."
                                        )
                                    }
                                    withContext(Dispatchers.Main) {
                                        showDialog2 = false
                                        dataProvider.showProfile.value = false
                                    }
                                } else {

                                    withContext(Dispatchers.Main) {
                                        showDialog2 = false
                                        getToast(
                                            context = context,
                                            "Unable to save changes."
                                        )
                                    }
                                }
                                appNavigator.setViewState(
                                    "login",
                                    updateHistory = false,
                                    clearHistory = true
                                ) {

                                    dataProvider.showProfile.value = false
                                }
                            }
                        }
                    }else{
                        showDialog2 = false
                    }
                }
            }
            Text("Make game history visible.", color = Color(0xFFF7F7F8))
            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = it

                }
            )
            Text("Change password?", color = Color(0xFFF7F7F8))
            Checkbox(
                checked = changePassword,
                onCheckedChange = {
                    changePassword = it

                }
            )
            Text("Change password.", color = Color(0xFFF7F7F8))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value =oldPassword, onValueChange =
                {
                    //passwordText=it
                    if (it.length <= maxText) {
                        oldPassword = it
                    }
                },
                label = {Text("Enter old password")},
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color(0xFFB301B3),
                    containerColor = Color(0xFFFDFDFD)
                ),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                supportingText = {
                    Text(
                        text = "${oldPassword.length} / $maxText",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        color = Color(0xFFFDFDFD)
                    )
                },
                enabled = changePassword

            )
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("Enter new password") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color(0xFFB301B3),
                    containerColor = Color(0xFFFDFDFD)
                ),
                supportingText = {
                    Text(
                        text = "${newPassword.length} / $maxText",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        color = Color(0xFFFDFDFD)
                    )
                },
                modifier = Modifier.fillMaxWidth(0.8f),
                enabled = changePassword
            )
            Button(onClick = { showDialog = true }) {
                Text(text = "Save")
            }
            if(showDialog){
                SaveAlert(){
                    if(it){
                        if (changePassword) {
                            if (dataProvider.loggedInPlayer.value.password == oldPassword.trim().fullEncode() && newPassword.trim() != "") {
                                dataProvider.loggedInPlayer.value.showHistory = isChecked
                                dataProvider.loggedInPlayer.value.password = newPassword.trim().fullEncode()
                                scope.launch {
                                    withContext(Dispatchers.IO) {
                                        val sharedPreferences = context.getSharedPreferences(
                                            "credentials",
                                            Context.MODE_PRIVATE
                                        )
                                        val editor = sharedPreferences.edit()
                                        editor.putString("id", "")
                                        editor.putString("password", "")
                                        editor.apply()
                                        dataProvider.initPlayers()
                                        dataProvider.players[dataProvider.loggedInPlayer.value.id] =
                                            dataProvider.loggedInPlayer.value
                                        val update = quizRepository.writeToDb(dataProvider.players)
                                        if (update) {
                                            withContext(Dispatchers.Main) {
                                                getToast(
                                                    context = context,
                                                    "Changes has been saved."
                                                )
                                            }
                                            withContext(Dispatchers.Main) {
                                                showDialog = false
                                                dataProvider.showProfile.value = false
                                                appNavigator.setViewState("login", updateHistory = false, clearHistory = true){
                                                    dataProvider.showProfile.value = false
                                                }
                                            }
                                        } else {

                                            withContext(Dispatchers.Main) {
                                                showDialog = false
                                                getToast(
                                                    context = context,
                                                    "Unable to save changes."
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                showDialog = false
                                getToast(context, "Invalid passwords")
                            }
                        }else{
                            dataProvider.loggedInPlayer.value.showHistory = isChecked
                            scope.launch {
                                withContext(Dispatchers.IO) {
                                    dataProvider.initPlayers()
                                    dataProvider.players[dataProvider.loggedInPlayer.value.id] =
                                        dataProvider.loggedInPlayer.value
                                    val update = quizRepository.writeToDb(dataProvider.players)
                                    if (update) {
                                        withContext(Dispatchers.Main) {
                                            getToast(
                                                context = context,
                                                "Changes has been saved."
                                            )
                                        }
                                        withContext(Dispatchers.Main) {
                                            showDialog = false
                                            dataProvider.showProfile.value = false
                                        }
                                    } else {

                                        withContext(Dispatchers.Main) {
                                            showDialog = false
                                            getToast(
                                                context = context,
                                                "Unable to save changes."
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }else{
                        showDialog = false
                    }
                }
            }
        }
    }
}

@Composable
fun SaveAlert(title:String="Save?",msg:String="Are you sure you want to save the changes made?",task:(Boolean)->Unit={}){
    AlertDialog(
        onDismissRequest = {
                           task(false)
        },
        title = { Text(text = title, color = Color(0xFF0B7B80)) },
        text =
        {
            Text(text = msg, color = Color(0xFF0B7B80))
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(0.5f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    onClick = {
                        task(true)
                    }
                ) {
                    Text(text = "OK")
                }
            }
        },
        dismissButton = {
            Row(
                modifier = Modifier.fillMaxWidth(0.5f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    onClick = {
                        task(false)
                    }
                ) {
                    Text(text = "Cancel")
                }
            }
        }

    )
}






