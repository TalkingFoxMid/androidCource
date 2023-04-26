package com.talkingfox.composedtrabbithacker.ui.views.load

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.talkingfox.composedtrabbithacker.ui.viewmodels.load.Event
import com.talkingfox.composedtrabbithacker.ui.viewmodels.load.LoadViewModel
import com.talkingfox.composedtrabbithacker.ui.viewmodels.load.State
import com.talkingfox.composedtrabbithacker.ui.views.CallbackNavigator

@Composable
fun LoadView(viewModel: LoadViewModel, toMain: () -> Unit) {
    val state = viewModel.state.collectAsState(
        State.Loading
    )
    val curVal = state.value
    var passed = remember {
        mutableStateOf(false)
    }
    when(curVal) {
        is State.Loading -> {
            viewModel.reduce(Event.TryLoad)

            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Green), contentAlignment = Alignment.Center) {
                Text(text = "LOADING", fontSize = 52.sp, color = Color.Red)
            }
        }
        is State.NeedAuthorization -> {
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()) {
                    OutlinedTextField(value = curVal.bufferToken, onValueChange = {
                        viewModel.reduce(Event.EditToken(it))
                    })

                    Button(onClick = {
                        viewModel.reduce(Event.Authorize)
                    }) {

                    }

                }
            }
        }
        is State.Ready -> {
            if(!passed.value) {
                passed.value = true
                toMain()
            }
        }
        else -> {}
    }
}