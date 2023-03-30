package com.talkingfox.composedtrabbithacker.views.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.talkingfox.composedtrabbithacker.domain.Habits
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun HomeScreen(items: State<List<Habits.Habit>?>,
               navigateCreateHabit: () -> Unit,
               navigateEditHabit: (UUID) -> Unit,
               deleteHabit: (UUID) -> Unit) {
    val landings = listOf("All", "Good", "Bad", "Neutral")
    println(items.value)
    fun getFilterByIndex(i: Int): (Habits.Habit) -> Boolean {
        return when(i) {
            0 -> { _ -> true}
            1 -> {hce -> hce.type == Habits.HabitType.GOOD}
            2 -> {hce -> hce.type == Habits.HabitType.BAD}
            3 -> {hce -> hce.type == Habits.HabitType.NEUTRAL}
            else -> { _ -> false }
        }
    }
    val state = rememberPagerState()
    val scope = rememberCoroutineScope()
    val filterName = remember {
      mutableStateOf("")
    }

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
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                contentAlignment = Alignment.CenterEnd) {
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(),value = filterName.value, onValueChange = {filterName.value = it})
                }
                Divider(color = Color.Black, thickness = 1.dp)

                HorizontalPager(pageCount = landings.size, state=state) {
                        page ->  Box(modifier = Modifier.fillMaxHeight(0.8f)) {
                    val itemsLocal = items.value
                    val selected = remember {
                        mutableStateOf<UUID?>(null)
                    }
                    LazyColumn {
                        items(itemsLocal!!) { item ->
                            HabitComposable(item, selected , toEdit = {navigateEditHabit(item.id)}, delete = {deleteHabit(item.id)},
                            visible = getFilterByIndex(page)(item) && item.name.startsWith(filterName.value))


                        }
                    }
                }

                }

                Divider(color = Color.Black, thickness = 4.dp)
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(), contentAlignment = Alignment.Center) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        FloatingActionButton(onClick = {
                            navigateCreateHabit()
                        }) {
                            Icon(Icons.Filled.Add,"", modifier = Modifier.size(30.dp))
                        }

                    }
                }


            }
        }
    }

}