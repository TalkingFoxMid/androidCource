package com.talkingfox.composedtrabbithacker.views.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.talkingfox.composedtrabbithacker.domain.HabitRepoInMemoryImpl

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val habitRepo = HabitRepoInMemoryImpl()
            val habitsObservedData = habitRepo.liveData().observeAsState(listOf())

            val navController: NavHostController = rememberNavController()
            val callbackNavigator = buildCallbackNavigator(navController)

            App({ callbackNavigator.toAbout() }, { callbackNavigator.toMain() }) {
                callbackNavigator.GetNavHost(items = habitsObservedData,
                    { habitRepo.addHabit(it) },
                    { h, i -> habitRepo.replaceHabit(i, h) },
                    { i -> habitRepo.deleteHabit(i) })
            }
        }
    }
}







