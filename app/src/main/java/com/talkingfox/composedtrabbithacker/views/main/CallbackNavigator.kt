package com.talkingfox.composedtrabbithacker.views.main

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.res.stringResource
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.talkingfox.composedtrabbithacker.CreateHabitView
import com.talkingfox.composedtrabbithacker.domain.Habits
import java.util.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.talkingfox.composedtrabbithacker.views.create.CreateScreenViewModel

fun buildCallbackNavigator(navController: NavHostController): CallbackNavigator =
    object: CallbackNavigator {
        val mainRoute = "mainRoute"
        val createHabitRoute = "createHabitRoute"
        val aboutRoute = "aboutRoute"

        override fun toMain() {
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
            NavHost(navController = navController, startDestination = mainRoute) {
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
                    CreateHabitView(viewModel, { toMain() },null)
                }

                composable("${createHabitRoute}/{habitId}", arguments = listOf(navArgument("habitId"){type = NavType.StringType})) {
                    val habitId = it.arguments?.getString("habitId")
                    val viewModel = hiltViewModel<CreateScreenViewModel>()
                    CreateHabitView(viewModel, { toMain() }, UUID.fromString(habitId))
                }
            }
        }

    }


interface CallbackNavigator {
    fun toMain(): Unit
    fun toAbout(): Unit
    fun toCreate(): Unit
    fun toEdit(id: UUID): Unit

    @Composable
    fun GetNavHost()
}