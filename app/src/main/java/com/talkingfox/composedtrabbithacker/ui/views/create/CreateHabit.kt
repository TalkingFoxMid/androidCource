package com.talkingfox.composedtrabbithacker

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talkingfox.composedtrabbithacker.ui.viewmodels.create.*
import com.talkingfox.composedtrabbithacker.ui.views.create.SelectPeriod
import com.talkingfox.composedtrabbithacker.ui.views.create.SelectPriority
import com.talkingfox.composedtrabbithacker.ui.views.create.SelectType
import java.util.*

@Composable
fun CreateHabitView(
    viewModel: CreateScreenViewModel,
    backToMainScreen: () -> Unit,
    habitUUID: UUID?
) {
    val state = viewModel.state.collectAsState(ModelState.Uninitialized)
    viewModel.reduce(Event.Initialize(habitUUID))
    val localState = state.value
    if (localState is ModelState.Modifying) {
        Column() {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.add_habit_text),
                    fontSize = 30.sp,
                    modifier = Modifier.padding(bottom = 15.dp)
                )
                Divider(color = Color.Black, thickness = 3.dp)
                OutlinedTextField(
                    value = localState.nameTextField,
                    placeholder = { Text(stringResource(id = R.string.create_habbit_name_placeholder)) },
                    onValueChange = {
                        viewModel.reduce(
                            Event.SetName(
                                it
                            )
                        )
                    })
                Divider(color = Color.Black, thickness = 1.dp)

                OutlinedTextField(
                    value = localState.descriptionTextField,
                    placeholder = { Text(stringResource(id = R.string.create_habbit_description_placeholder)) },
                    onValueChange = {
                        viewModel.reduce(
                            Event.SetDesc(
                                it
                            )
                        )
                    })
                Divider(color = Color.Black, thickness = 1.dp)

                SelectPriority(localState.prioritySelect) {
                    viewModel.reduce(
                        Event.SetPriority(
                            it
                        )
                    )

                }
                Divider(color = Color.Black, thickness = 1.dp)

                SelectType(localState.typeSelect) {
                    viewModel.reduce(Event.SetType(it))

                }
                Divider(color = Color.Black, thickness = 1.dp)
                SelectPeriod(localState.period) {
                    viewModel.reduce(Event.SetPeriod(it))
                }

            }
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                FloatingActionButton(onClick = {
                    viewModel.reduce(
                        Event.DoneEdit {
                            cb -> when(cb) {
                                is DoneEditResultSuccess -> {
                                    backToMainScreen()
                                }
                                is DoneEditResultError -> {

                                }
                            }
                        }
                    )
                }) {
                    Icon(Icons.Filled.Check, "", modifier = Modifier.size(30.dp))
                }
            }
        }
    }


}

