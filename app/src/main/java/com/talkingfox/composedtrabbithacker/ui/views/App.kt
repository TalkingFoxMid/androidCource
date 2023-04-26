package com.talkingfox.composedtrabbithacker.ui.views.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.talkingfox.composedtrabbithacker.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
private fun TopBar(scope: CoroutineScope, scaffoldState: ScaffoldState) =  TopAppBar(
    title = { Text(stringResource(id = R.string.app_name_informal)) },
    navigationIcon = {
        IconButton(onClick = {
            scope.launch { scaffoldState.drawerState.open() }
        }) {
            Icon(
                Icons.Filled.Settings,
                "",
                modifier = Modifier.size(32.dp)
            )
        }
    },
    actions = {

    }
)

@Composable
private fun DrawerContent(toAbout: () -> Unit, toHome: () -> Unit, scope: CoroutineScope, scaffoldState: ScaffoldState) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = { toAbout(); scope.launch { scaffoldState.drawerState.close() }}, modifier = Modifier.fillMaxWidth()) {
            Text("About")
        }
        Button(onClick = { toHome(); scope.launch { scaffoldState.drawerState.close() }}, modifier = Modifier.fillMaxWidth()) {
            Text("Home")
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun App(toAbout: () -> Unit, toHome: () -> Unit, content: @Composable Any.() -> Unit) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopBar(scope, scaffoldState)
        },
        content = content,
        scaffoldState = scaffoldState,
        drawerContent = {
            DrawerContent(toAbout, toHome, scope, scaffoldState)
        },
        drawerGesturesEnabled = true
    )
}