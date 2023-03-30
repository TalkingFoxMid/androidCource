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
        override fun GetNavHost(items: State<List<Habits.Habit>>, createHabit: (Habits.Habit) -> Unit,
                                updateHabit: ((Habits.Habit, UUID) -> Unit), deleteHabit: (UUID) -> Unit){
            NavHost(navController = navController, startDestination = mainRoute) {
                composable(aboutRoute) {
                    Text(text = stringResource(id = com.talkingfox.composedtrabbithacker.R.string.about_text))
                }

                composable(mainRoute) {
                    HomeScreen(
                        items = items,
                        { toCreate() },
                        { toEdit(it) },
                        { deleteHabit(it) }
                    )
                }

                composable(createHabitRoute) {
                    CreateHabitView(null,
                        {
                            toMain()
                        },{createHabit(it)},{h, uuid -> updateHabit(h, uuid)})
                }

                composable("${createHabitRoute}/{habitId}", arguments = listOf(navArgument("habitId"){type = NavType.StringType})) {
                    val habitId = it.arguments?.getString("habitId")
                    CreateHabitView(
                        items.value.find {el -> el.id.toString() == habitId},
                        {toMain()},
                        {x -> createHabit(x)},
                        {h, uuid -> updateHabit(h, uuid)}
                    )
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
    fun GetNavHost(items: State<List<Habits.Habit>>, createHabit: (Habits.Habit) -> Unit,
                   updateHabit: ((Habits.Habit, UUID) -> Unit), deleteHabit: (UUID) -> Unit)
}