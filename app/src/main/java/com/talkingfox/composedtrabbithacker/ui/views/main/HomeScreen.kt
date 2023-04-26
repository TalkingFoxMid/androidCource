package com.talkingfox.composedtrabbithacker.ui.views.main

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
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.talkingfox.composedtrabbithacker.domain.Habits
import com.talkingfox.composedtrabbithacker.repository.TokenRepository
import com.talkingfox.composedtrabbithacker.ui.viewmodels.main.Event
import com.talkingfox.composedtrabbithacker.ui.viewmodels.main.HomeScreenState
import com.talkingfox.composedtrabbithacker.ui.viewmodels.main.HomeScreenViewModel
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel, navigateCreateHabit: () -> Unit,
    navigateEditHabit: (UUID) -> Unit
) {
    viewModel.reduce(Event.PreloadHabits)
    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val state = viewModel.state.observeAsState(
        HomeScreenState(
            listOf()
        )
    )
    val refreshing = remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(refreshing.value, {
        refreshing.value = true
        viewModel.reduce(Event.ForceReloadHabits {refreshing.value = false})

    })
    val localState = state.value
    val landings = listOf("All", "Good", "Bad")
    fun getFilterByIndex(i: Int): (Habits.Habit) -> Boolean {
        return when (i) {
            0 -> { _ -> true }
            1 -> { hce -> hce.data.type == Habits.HabitType.GOOD }
            2 -> { hce -> hce.data.type == Habits.HabitType.BAD }
            else -> { _ -> false }
        }
    }

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val filterName = remember {
        mutableStateOf("")
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 10.dp,
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .height(48.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = filterName.value,
                    onValueChange = { filterName.value = it })
            }
        }
    ) {
        Surface() {
            Column() {
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    backgroundColor = Color.Magenta,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.pagerTabIndicatorOffset(
                                pagerState = pagerState,
                                tabPositions = tabPositions
                            )
                        )
                    }) {
                    landings.forEachIndexed { index, s ->
                        Tab(selected = pagerState.currentPage == index, onClick = {
                            scope.launch { pagerState.animateScrollToPage(index) }
                        }, text = {
                            Text(text = landings[index])
                        }, modifier = Modifier.background(
                            color = if (pagerState.currentPage == index) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant
                        )
                        )
                    }
                }

                Column() {

                    Divider(color = Color.Black, thickness = 1.dp)

                    HorizontalPager(pageCount = landings.size, state = pagerState) { page ->
                        Box(modifier = Modifier.fillMaxHeight(0.8f).pullRefresh(pullRefreshState)) {
                            val itemsLocal = localState.habits
                            val selected = remember {
                                mutableStateOf<UUID?>(null)
                            }
                            LazyColumn {
                                items(itemsLocal) { item ->
                                    HabitComposable(
                                        item,
                                        selected,
                                        toEdit = { navigateEditHabit(item.id) },
                                        delete = { viewModel.reduce(Event.DeleteHabit(item.id)) },
                                        visible = getFilterByIndex(page)(item) && item.data.name.startsWith(
                                            filterName.value
                                        )
                                    )


                                }
                            }
                            PullRefreshIndicator(refreshing.value, pullRefreshState, Modifier.align(Alignment.TopCenter))

                        }

                    }

                    Divider(color = Color.Black, thickness = 4.dp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(), contentAlignment = Alignment.Center
                    ) {

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                           Row(
                               Modifier
                                   .fillMaxHeight()
                                   .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                               FloatingActionButton(onClick = {
                                   navigateCreateHabit()
                               }) {
                                   Icon(Icons.Filled.Add, "", modifier = Modifier.size(30.dp))
                               }
                               FloatingActionButton(onClick = {
                                   scope.launch{sheetState.expand()}
                               }) {
                                   Icon(Icons.Filled.Search, "", modifier = Modifier.size(30.dp))
                               }
                           }

                        }
                    }


                }
            }
        }
    }


}