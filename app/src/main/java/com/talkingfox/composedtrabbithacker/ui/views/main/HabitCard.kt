package com.talkingfox.composedtrabbithacker.ui.views.main

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
import java.util.UUID
private fun colorFromType(type: Habits.HabitType): Color {
    return when (type) {
        Habits.HabitType.BAD -> Color.Red
        Habits.HabitType.GOOD -> Color.Green
    }
}

@Composable
private fun CardButtons(isSelected: Boolean, toEdit: () -> Unit, delete: () -> Unit) {
        Column() {
            AnimatedVisibility(
                isSelected
            ){
                FloatingActionButton(backgroundColor = Color.Gray, modifier = Modifier
                    .width(48.dp)
                    .height(48.dp)
                    .padding(5.dp), onClick = { toEdit() }) {
                    Icon(Icons.Filled.Settings, "", modifier = Modifier.size(32.dp))
                }
            }

            AnimatedVisibility(
                isSelected
            ) {
                FloatingActionButton(backgroundColor = Color.Gray, modifier = Modifier
                    .width(48.dp)
                    .height(48.dp)
                    .padding(5.dp), onClick = {
                    delete()
                }) {
                    Icon(Icons.Filled.Delete, "", modifier = Modifier.size(32.dp))
                }
            }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HabitComposable(hce: Habits.Habit, selected: MutableState<UUID?>, toEdit: () -> Unit, delete: () -> Unit, visible: Boolean) {
    val isSelected = hce.id == selected.value
    AnimatedVisibility(visible){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(120.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(15.dp),
            elevation = 20.dp,
            onClick = {
                if (selected.value != hce.id) {
                    selected.value = hce.id
                } else {
                    selected.value = null
                }
            }
        ) {
            Box() {
                Row() {
                    Column(
                        Modifier
                            .background(colorFromType(hce.data.type))
                            .fillMaxWidth(0.2f)
                            .fillMaxHeight()
                    ) {
                    }
                    Column(
                        Modifier
                            .fillMaxWidth(0.8f)
                            .fillMaxHeight()
                            .padding(10.dp)
                    ) {
                        Text(text = hce.data.name, fontSize = 25.sp, modifier = Modifier.weight(0.4f))

                        AnimatedVisibility(isSelected) {
                            Text(text = hce.data.description, modifier = Modifier.weight(0.4f))
                        }
                        Divider(color = Color.Black, thickness = 1.dp)
                        Row(modifier = Modifier.weight(0.2f)) {
                            Text(
                                text = hce.data.priority.toString(),
                                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                            )
                            Text(
                                text = hce.data.type.toString(),
                                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                            )
                            Text(
                                text = "${hce.data.period.retries} / ${hce.data.period.periodDays}",
                                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                            )
                        }
                    }
                    CardButtons(isSelected, toEdit, delete)
                }
            }
        }
    }
}