package com.syllabus.pq

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(ticketList: List<Ticket>){
    val bgColors = listOf(
        Color(0xFFAE6AEE),  // Purple
        Color(0xFFFF00FF),  // Magenta
    )
    val scope = rememberCoroutineScope()
    val thisContext = LocalContext.current
    val maxTicketIndex = ticketList.size - 1

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(bgColors, startY = 0.0f, endY = 2800.0f)
                )
                .padding(5.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Row(modifier = Modifier
                .fillMaxHeight(0.09f)
                .fillMaxWidth(0.90f)
                .border(
                    BorderStroke(width = 1.dp, color = Color.White),
                    shape = RoundedCornerShape(6)
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    onClick = { dataProvider.showInfo.value = true },
                    modifier = Modifier.size(45.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.menu_1), contentDescription = "",
                        modifier = Modifier.size(45.dp)
                    )
                }
                IconButton(
                    onClick =
                    {
                        dataProvider.showRankings.value = true
                        //appNavigator.setViewState("rankings")
                    },
                    modifier = Modifier.size(45.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.leader_2), contentDescription = "",
                        modifier = Modifier.size(45.dp)
                    )
                }
                IconButton(
                    onClick = { dataProvider.showProfile.value = true },
                    modifier = Modifier.size(45.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_1), contentDescription = "",
                        modifier = Modifier.size(45.dp)
                    )
                }
                IconButton(
                    onClick = {
                              scope.launch {
                                  getToast(thisContext,"Reloading app.")
                                  withContext(Dispatchers.IO){
                                      if (isInternetConnected(thisContext)) {
                                          appNavigator.setViewState("splash")
                                      }
                                  }
                              }
                    },
                    modifier = Modifier.size(45.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.refresh_1), contentDescription = "",
                        modifier = Modifier.size(45.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Column(modifier = Modifier
                .fillMaxHeight(0.32f)
                .fillMaxWidth(0.90f)
                .border(
                    BorderStroke(width = 1.dp, color = Color.White),
                    shape = RoundedCornerShape(6)
                )
                .background(
                    color = Color(0xFFFADCFA).copy(alpha = 0.8f),
                    shape = RoundedCornerShape(6)
                ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                fun getNetworkStatus(): Boolean {
                    return runBlocking {
                        val net = withContext(CoroutineScope(Dispatchers.IO).coroutineContext){
                            isInternetConnected(thisContext)
                        }
                        net
                    }
                }
                when(dataProvider.ticketSlideView.value){
                    "ticket"->{
                        AnimatedVisibility(
                            visible = true,
                            enter = slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
                            )
                        ) {
                            TicketSlides(ticket = dataProvider.tickets[dataProvider.ticketIndex.value])
                        }
                    }
                    "ads"->{
                        dataProvider.ticketDuration.value = 10000
                        AdsStack()
                    }
                }

                LaunchedEffect(dataProvider.playNoticeBoard.value){
                    //scope.launch{
                    while(dataProvider.playNoticeBoard.value){
                            if (getNetworkStatus()) {

                                delay(dataProvider.ticketDuration.value.toLong())
                                if (dataProvider.ticketSlideView.value == "ticket") {
                                    dataProvider.ticketSlideView.value = "ads"
                                } else {

                                    if (dataProvider.ticketIndex.value < maxTicketIndex) {
                                        dataProvider.ticketIndex.value += 1
                                        dataProvider.ticketSlideView.value = "ticket"
                                    } else {
                                        dataProvider.ticketIndex.value = 0
                                        dataProvider.ticketSlideView.value = "ticket"
                                    }
                                }
                            }
                    }
                }

            }
            Spacer(modifier = Modifier.height(5.dp))
            OutlinedTextField(
                value = dataProvider.searchText.value,
                onValueChange = { dataProvider.searchText.value = it },
                label = { Text("Search") },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(16.dp)
            )

            val filteredQuizzes = dataProvider.quizzes.filter {
                it.title.contains(dataProvider.searchText.value, ignoreCase = true) || it.count.toString().contains(dataProvider.searchText.value)
            }
            Spacer(modifier = Modifier.height(5.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(5.dp)
            ) {
                items(filteredQuizzes) { it ->
                    Column(modifier = Modifier.wrapContentSize()){
                        QuizThumbnail(quizData = it)
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }
        }
        if (dataProvider.showInfo.value){
            Info()
        }
        if (dataProvider.showRankings.value){
            Rankings()
        }
        if (dataProvider.showProfile.value){
            Profile()
        }
    }

}