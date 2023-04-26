package com.talkingfox.composedtrabbithacker.ui.views

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.talkingfox.composedtrabbithacker.CreateHabitView
import java.util.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.talkingfox.composedtrabbithacker.ui.viewmodels.create.CreateScreenViewModel
import com.talkingfox.composedtrabbithacker.ui.viewmodels.load.LoadViewModel
import com.talkingfox.composedtrabbithacker.ui.viewmodels.main.HomeScreenViewModel
import com.talkingfox.composedtrabbithacker.ui.views.load.LoadView
import com.talkingfox.composedtrabbithacker.ui.views.main.HomeScreen

fun buildCallbackNavigator(navController: NavHostController): CallbackNavigator =
    object: CallbackNavigator {
        val mainRoute = "mainRoute"
        val createHabitRoute = "createHabitRoute"
        val aboutRoute = "aboutRoute"
        val loadRoute = "loadRoute"

        override fun toMainFlushStack() {
            navController.navigate(mainRoute) {
                popUpTo(0)
            }
        }

        override fun toAbout() {
            navController.navigate(aboutRoute)
        }

        override fun toCreate() {
            navController.navigate(createHabitRoute)
        }

        override fun toEdit(id: UUID) {
            navController.navigate("${createHabitRoute}/${id}")
        }


        @Composable
        override fun GetNavHost(){
            NavHost(navController = navController, startDestination = loadRoute) {
                composable(loadRoute) {
                    val vm = hiltViewModel<LoadViewModel>()
                    LoadView(vm) { navController.navigate(mainRoute) }
                }

                composable(aboutRoute) {
                    Text(text = stringResource(id = com.talkingfox.composedtrabbithacker.R.string.about_text))
                }

                composable(mainRoute) {
                    val viewModel = hiltViewModel<HomeScreenViewModel>()
                    HomeScreen(viewModel, { toCreate() },
                        { toEdit(it) })
                }


               composable(createHabitRoute) {
                   val viewModel = hiltViewModel<CreateScreenViewModel>()
                   CreateHabitView(viewModel, { toMainFlushStack() },null)
               }


                composable("${createHabitRoute}/{habitId}", arguments = listOf(navArgument("habitId"){type = NavType.StringType})) {
                    val habitId = it.arguments?.getString("habitId")
                    val viewModel = hiltViewModel<CreateScreenViewModel>()
                    CreateHabitView(viewModel, { toMainFlushStack() }, UUID.fromString(habitId))
                }
            }
        }

    }


interface CallbackNavigator {
    fun toMainFlushStack(): Unit
    fun toAbout(): Unit
    fun toCreate(): Unit
    fun toEdit(id: UUID): Unit

    @Composable
    fun GetNavHost()
}