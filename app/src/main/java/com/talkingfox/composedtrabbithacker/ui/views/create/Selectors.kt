package com.talkingfox.composedtrabbithacker.ui.views.create

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talkingfox.composedtrabbithacker.R
import com.talkingfox.composedtrabbithacker.domain.Habits


@Composable
fun SelectPriority(priority: Habits.Priority, setPriority: (Habits.Priority) -> Unit) {
    val expanded = remember { mutableStateOf(false) }
    val items = listOf(Habits.Priority.LOW, Habits.Priority.MEDIUM, Habits.Priority.HIGH)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = stringResource(id = R.string.create_habbit_select_priority), fontSize = 20.sp)
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .wrapContentSize(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            Card(elevation = 15.dp,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(priority.toString(), modifier = Modifier
                    .padding(15.dp)
                    .clickable(onClick = { expanded.value = true }),
                    textAlign = TextAlign.Center)
            }
            DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = true }) {
                items
                    .map {
                        DropdownMenuItem(onClick = { setPriority(it); expanded.value = false }) {
                            Text(text = it.toString())
                        }
                    }
            }
        }
    }
}

@Composable
fun SelectType(type: Habits.HabitType, setType: (Habits.HabitType) -> Unit) {
    val variance = listOf(Habits.HabitType.BAD, Habits.HabitType.GOOD)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = stringResource(id = R.string.create_habbit_select_type), fontSize = 20.sp)
        Row() {
            variance.map {
                Text(text = it.toString())
                RadioButton(selected = type == it, onClick = { setType(it) })
            }
        }
    }
}

@Composable
fun SelectPeriod(period: Habits.Period, setPeriod: (Habits.Period) -> Unit) {

    Card(shape = RoundedCornerShape(15.dp),
        elevation = 20.dp,
        modifier = Modifier
            .padding(30.dp)
            .height(170.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
            Text(text = stringResource(id = R.string.create_habbit_select_period_days))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.5f),
                value = period.periodDays.toString(),
                onValueChange = {setPeriod(Habits.Period(period.retries, it.toInt()))},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Text(text = stringResource(id = R.string.create_habbit_select_period_tries))
            OutlinedTextField(modifier = Modifier.fillMaxWidth(0.5f),
                value = period.retries.toString(),
                onValueChange = {setPeriod(Habits.Period(it.toInt(), period.periodDays))},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

