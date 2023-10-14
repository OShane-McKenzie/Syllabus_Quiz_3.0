package com.syllabus.pq

import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
//import kotlin.system.exitProcess

class CustomNavigator(initialScreen:String = "splash") {

    private val screenArray = mutableStateListOf<String>()
    val setView = mutableStateOf(initialScreen)
    private val firstScreen = initialScreen

    fun setViewState(view:String, execTask:Boolean = false, updateHistory:Boolean = true,clearHistory:Boolean = false,runTask:()->Unit = {}){
        if (setView.value!=view &&
            setView.value!=firstScreen &&
            setView.value!="loading"  &&
            view.trim()!="" &&
            updateHistory
            ) {
            screenArray.add(setView.value)
        }
        if (view=="loading"||execTask){
            runTask()
        }
        setView.value = view
        if(clearHistory){
            screenArray.clear()
        }

    }

    @Composable
    fun GetBackHandler(context:Context,lifecycleOwner: LifecycleOwner):Unit{
        val activity = context as? Activity
        return BackHandler(onBack = {

            // Get the last index of the navigation history array.
            val lastIndex = screenArray.size - 1

            // Check if the last index is valid (greater than -1).
            if(!dataProvider.showInfo.value && !dataProvider.showProfile.value && !dataProvider.showRankings.value) {
                if (lastIndex > -1) {

                    // Set the current view state to the last item in the navigation history array.

                    setView.value = screenArray[lastIndex]

                    // Remove the last item from the navigation history array.
                    screenArray.removeAt(lastIndex)
                } else {

                    // If the navigation history array is empty, add the current view state to it.
                    screenArray.add(setView.value)

                    // Close the current activity.
                    activity?.finish()

                    // Update the lifecycle state to DESTROYED.
                    (lifecycleOwner.lifecycle as? LifecycleRegistry)?.currentState =
                        Lifecycle.State.DESTROYED

                    // Terminate the process.
                    //exitProcess(0)
                }
            }else{
                dataProvider.showInfo.value = false
                dataProvider.showProfile.value = false
                dataProvider.showRankings.value = false
                dataProvider.rankingsLoaded.value = false
                dataProvider.playNoticeBoard.value = true
            }
        })
    }
}