package com.talkingfox.composedtrabbithacker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talkingfox.composedtrabbithacker.domain.Habits
import com.talkingfox.composedtrabbithacker.views.create.*
import com.talkingfox.composedtrabbithacker.views.main.HabitCardElement

@Composable
fun CreateHabitView(items: SnapshotStateList<HabitCardElement>, habitId: Int?, backToMainScreen: () -> Unit) {
    val name = remember { mutableStateOf("") }
    val desc = remember { mutableStateOf("") }
    val priority = remember { mutableStateOf(Habits.Priority.LOW) }
    val type = remember { mutableStateOf(Habits.HabitType.NEUTRAL) }
    val periodDays = remember {
        mutableStateOf(0)
    }
    val periodRetries = remember {
        mutableStateOf(0)
    }
    val isSelected = remember {
        mutableStateOf(false)
    }
    Column() {
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(id = R.string.add_habit_text), fontSize = 30.sp, modifier = Modifier.padding(bottom = 15.dp))
            Divider(color = Color.Black, thickness = 3.dp)
            OutlinedTextField(value = name.value, placeholder = { Text(stringResource(id = R.string.create_habbit_name_placeholder)) }, onValueChange = {name.value = it})
            Divider(color = Color.Black, thickness = 1.dp)

            OutlinedTextField(value = desc.value, placeholder = { Text(stringResource(id = R.string.create_habbit_description_placeholder)) }, onValueChange = {desc.value = it})
            Divider(color = Color.Black, thickness = 1.dp)

            SelectPriority(priority)
            Divider(color = Color.Black, thickness = 1.dp)

            SelectType(type)
            Divider(color = Color.Black, thickness = 1.dp)
            SelectPeriod(periodDays, periodRetries)

        }
        Box(contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
            FloatingActionButton(onClick = {
                if(habitId != null) {
                    val habitCardElement = items[habitId]
                    habitCardElement.name.value = name.value
                    habitCardElement.description.value = desc.value
                    habitCardElement.priority.value = priority.value
                    habitCardElement.type.value = type.value
                    habitCardElement.periodDays.value = periodDays.value
                    habitCardElement.periodRetries.value = periodRetries.value
                    habitCardElement.isSelected.value = isSelected.value
                } else {
                    items.add(
                        HabitCardElement(
                            name, desc, priority, periodRetries, periodDays, type, isSelected
                        )
                    )
                }
                backToMainScreen()
            }) {
                Icon(Icons.Filled.Check,"", modifier = Modifier.size(30.dp))

            }
        }
    }
}
