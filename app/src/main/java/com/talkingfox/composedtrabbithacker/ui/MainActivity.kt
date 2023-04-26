package com.talkingfox.composedtrabbithacker.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.talkingfox.composedtrabbithacker.ui.views.buildCallbackNavigator
import com.talkingfox.composedtrabbithacker.ui.views.main.App
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController: NavHostController = rememberNavController()
            val callbackNavigator = buildCallbackNavigator(navController)

            App({ callbackNavigator.toAbout() }, { callbackNavigator.toMainFlushStack() }) {
                callbackNavigator.GetNavHost()
            }
        }
    }
}







