package com.talkingfox.composedtrabbithacker.views.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.talkingfox.composedtrabbithacker.CreateHabitView
import com.talkingfox.composedtrabbithacker.R
import com.talkingfox.composedtrabbithacker.domain.Habits
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val items =  remember {
                mutableStateListOf<HabitCardElement>(

                )
            }
            val navController: NavHostController = rememberNavController()
            val callbackNavigator = getCallbackNavigator(navController)

            App({callbackNavigator.toAbout()}, {callbackNavigator.toMain()}) {
                callbackNavigator.GetNavHost(items = items)
            }
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
            TopAppBar(
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
        },
        content = content,
        scaffoldState = scaffoldState,
        drawerContent = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { toAbout(); scope.launch { scaffoldState.drawerState.close() }}, modifier = Modifier.fillMaxWidth()) {
                    Text("About")
                }
                Button(onClick = { toHome(); scope.launch { scaffoldState.drawerState.close() }}, modifier = Modifier.fillMaxWidth()) {
                    Text("Home")
                }
            }
        },
        drawerGesturesEnabled = true
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun MainView(items: SnapshotStateList<HabitCardElement>, createHabit: () -> Unit,  editHabit: (Int) -> Unit) {
    val landings = listOf("All", "Good", "Bad", "Neutral")

    fun getFilterByIndex(i: Int): (HabitCardElement) -> Boolean {
        return when(i) {
            0 -> { _ -> true}
            1 -> {hce -> hce.type.value == Habits.HabitType.GOOD}
            2 -> {hce -> hce.type.value == Habits.HabitType.BAD}
            3 -> {hce -> hce.type.value == Habits.HabitType.NEUTRAL}
            else -> { _ -> false }
        }
    }
    val state = rememberPagerState()
    val scope = rememberCoroutineScope()

    Surface() {
        Column() {
            TabRow(selectedTabIndex = state.currentPage, backgroundColor = Color.Magenta, indicator = {
                tabPositions -> TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState = state,tabPositions = tabPositions)
                )
            }) {
                landings.forEachIndexed { index, s ->
                    Tab(selected = state.currentPage == index, onClick = {
                        scope.launch { state.animateScrollToPage(index) }
                    }, text = {
                        Text(text = landings[index])
                    }, modifier = Modifier.background(
                        color = if(state.currentPage == index) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant
                    ))
                }
            }

            Column() {
                HorizontalPager(pageCount = 10, state=state) {
                        page ->  Box(modifier = Modifier.fillMaxHeight(0.8f)) {
                        val itemsLocal = items.filter { item -> getFilterByIndex(page)(item) }

                        LazyColumn {
                            itemsIndexed(itemsLocal) { index, item ->
                                HabitComposable(item, flushSelected = {items.forEach{it.isSelected.value = false}}, toEdit = {editHabit(index)}, delete = {items.removeAt(index)})
                                Divider(color = Color.Black, thickness = 1.dp)
                            }
                    }
                }

                }

                Divider(color = Color.Black, thickness = 4.dp)

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(), contentAlignment = Alignment.Center) {
                    FloatingActionButton(onClick = {
                        createHabit()
                    }) {
                        Icon(Icons.Filled.Add,"", modifier = Modifier.size(30.dp))
                    }
                }
            }
        }
    }

}



