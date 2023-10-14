package com.syllabus.pq

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import kotlinx.coroutines.delay

@Composable
fun Views(){

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var setTransitionLayer by remember {
        mutableStateOf(false)
    }
    var transitionLayerAlpha by remember {
        mutableStateOf(0.0f)
    }
    if(setTransitionLayer){
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFFFFF).copy(alpha = transitionLayerAlpha))) {
        }
    }
    @Composable
    fun fadeTransition(runTask:@Composable ()->Unit={}){
        LaunchedEffect(Unit) {
            setTransitionLayer = true
            while (transitionLayerAlpha < 1.0) {
                if(transitionLayerAlpha + 0.1f < 1.0f) {
                    transitionLayerAlpha += 0.1f
                    delay(50)
                }else{
                    transitionLayerAlpha += 0.0f
                    setTransitionLayer = false
                    break
                }

                if (transitionLayerAlpha >= 1 ){
                    transitionLayerAlpha += 0.0f
                    setTransitionLayer = false
                    break
                }
            }
        }
        if (!setTransitionLayer) {
            runTask()
        }
    }
    appNavigator.GetBackHandler(context = context, lifecycleOwner = lifecycleOwner)
    Box(
        Modifier.fillMaxSize()
    ){

        when(appNavigator.setView.value){
            "home"->{

                HomeScreen(ticketList = dataProvider.tickets)
            }
            "quiz"->{

                QuestionAnswer(questionData = dataProvider.startThisQuiz.value)
            }
            "results"->{
                ResultsView()
            }
            "splash"->{
                SplashScreen()
            }
            "update"->{
                UpdateApp()
            }
            "offline"->{
                Offline()
            }
            "login"->{
                Login()
            }
            "success"->{
                AccountCreated()
            }
//            "rankings"->{
//                Rankings()
//            }
        }
    }
}