package com.talkingfox.composedtrabbithacker.views.main

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.res.stringResource
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.talkingfox.composedtrabbithacker.CreateHabitView


fun getCallbackNavigator(navController: NavHostController): CallbackNavigator =
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

        override fun toEdit(id: Int) {
            navController.navigate("${createHabitRoute}/${id}")
        }


        @Composable
        override fun GetNavHost(items: SnapshotStateList<HabitCardElement>) {
            NavHost(navController = navController, startDestination = mainRoute) {
                composable(aboutRoute) {
                    Text(text = stringResource(id = com.talkingfox.composedtrabbithacker.R.string.about_text))
                }

                composable(mainRoute) {
                    MainView(
                        items = items,
                        {
                            toCreate()
                        },
                        {
                            toEdit(it)
                        }
                    )
                }

                composable(createHabitRoute) {
                    CreateHabitView(items, null) { toMain() }
                }

                composable("${createHabitRoute}/{habitId}", arguments = listOf(navArgument("habitId"){type = NavType.IntType})) {
                    CreateHabitView(items, it.arguments?.getInt("habitId")) { toMain() }
                }
            }
        }

    }


interface CallbackNavigator {
    fun toMain(): Unit
    fun toAbout(): Unit
    fun toCreate(): Unit
    fun toEdit(id: Int): Unit

    @Composable
    fun GetNavHost(items: SnapshotStateList<HabitCardElement>)
}