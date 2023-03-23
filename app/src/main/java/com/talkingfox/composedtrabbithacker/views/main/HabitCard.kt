package com.talkingfox.composedtrabbithacker.views.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import com.talkingfox.composedtrabbithacker.domain.Habits
import androidx.compose.foundation.background
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class HabitCardElement(
    val name: MutableState<String>,
    val description: MutableState<String>,
    val priority: MutableState<Habits.Priority>,
    val periodRetries: MutableState<Int>,
    val periodDays: MutableState<Int>,
    val type: MutableState<Habits.HabitType>,
    val isSelected: MutableState<Boolean>
) {
    fun core(): Habits.Habit =
        Habits.Habit(
            name = name.value,
            description = description.value,
            priority = priority.value,
            type = type.value,
            period = Habits.Period(periodRetries.value, periodDays.value)
        )
}

fun colorFromType(type: Habits.HabitType): Color {
    return when (type) {
        Habits.HabitType.BAD -> Color.Red
        Habits.HabitType.GOOD -> Color.Green
        Habits.HabitType.NEUTRAL -> Color.Gray
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HabitComposable(hce: HabitCardElement, flushSelected: () -> Unit, toEdit: () -> Unit, delete: () -> Unit) {
    val habit = hce.core()
    val selected = hce.isSelected
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(120.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = 20.dp,
        onClick = {
            val isSelected = selected.value
            flushSelected()
            selected.value = !isSelected
        }
    ) {
        Box() {
            Row() {
                Column(
                    Modifier
                        .background(colorFromType(habit.type))
                        .fillMaxWidth(0.2f)
                        .fillMaxHeight()) {
                }
                Column(
                    Modifier
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight()
                        .padding(10.dp)) {
                    Text(text = habit.name, fontSize = 25.sp, modifier = Modifier.weight(0.4f))

                    AnimatedVisibility(selected.value) {
                        Text(text = habit.description, modifier = Modifier.weight(0.4f))
                    }
                    Divider(color = Color.Black, thickness = 1.dp)
                    Row(modifier = Modifier.weight(0.2f)) {
                        Text(text = habit.priority.toString(), modifier = Modifier.padding(start = 5.dp, end = 5.dp))
                        Text(text = habit.type.toString(), modifier = Modifier.padding(start = 5.dp, end = 5.dp))
                        Text(text = "${habit.period.retries} / ${habit.period.periodDays}", modifier = Modifier.padding(start = 5.dp, end = 5.dp))
                    }
                }
                AnimatedVisibility(
                    selected.value
                ){
                    Column() {
                        FloatingActionButton(backgroundColor = Color.Gray, modifier = Modifier
                            .width(48.dp)
                            .height(48.dp)
                            .padding(5.dp), onClick = { toEdit() }) {
                            Icon(Icons.Filled.Settings,"", modifier = Modifier.size(32.dp))
                        }

                        FloatingActionButton(backgroundColor = Color.Gray, modifier = Modifier
                            .width(48.dp)
                            .height(48.dp)
                            .padding(5.dp), onClick = {
                            delete()
                        }) {
                            Icon(Icons.Filled.Delete,"", modifier = Modifier.size(32.dp))
                        }
                    }
                }


            }
        }
    }
}