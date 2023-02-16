package com.example.weatherable.ui.cells

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Top
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import com.example.weatherable.ui.viewmodel.MainViewModel
import com.example.weatherable.utilites.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Table(visible: Boolean, viewModel: MainViewModel) {
    viewModel.presList.collectAsState().value.apply {
        val tempPoints by viewModel.tempList.collectAsState()
        val stateList = rememberLazyListState(initialFirstVisibleItemIndex = size)
        var timeVis by remember { mutableStateOf(false) }
        var info by remember { mutableStateOf(Pair("", "")) }
        var sel by remember { mutableStateOf(-1) }
        val coroutine = rememberCoroutineScope()
        with(Modifier) {
            Visibility(visible = visible) {
                timeVis = false; sel = -1
                Card(
                    width(600.dp)
                        .noRippleClickable { timeVis = false; sel = -1 }
                        .height(530.dp)
                        .padding(10.dp, 20.dp, 10.dp), RoundedCornerShape(20.dp),
                    border = BorderStroke(2.dp, Black)) {
                    Visibility(timeVis) {
                        Column(fillMaxSize(), Top, CenterHorizontally) {
                            Card(
                                wrapContentSize().padding(top = 20.dp),
                                border = BorderStroke(2.dp, Red)
                            ) {
                                Text(
                                    "${getStringWasForChat(info.first.toLong())}," +
                                            " температура: ${info.second}", padding(10.dp),
                                    textAlign = Center
                                )
                            }
                        }
                    }
                    Box(padding(top = 6.dp)) {
                        LazyRow(padding(start = 50.dp), stateList,
                            content = {
                                itemsIndexed(this@apply) { i, item ->
                                    if (item.pressure.startsWith("7"))
                                        Card(
                                            noRippleClickable {
                                                coroutine.launch(IO) {
                                                    if (sel != i) {
                                                        timeVis = false; delay(50L)
                                                        timeVis = true
                                                        info = Pair(item.id, tempPoints[i].temp)
                                                        sel = i
                                                    }
                                                }
                                            }.padding(
                                                top = padding(i),
                                                end = if (lastIndex == i) 50.dp else 6.dp
                                            ),
                                            border = sel.border(i), elevation = 0.dp
                                        ) { TextV(item.text, i); TextV("*", i) }
                                }
                            }); (0..4).run {
                        forEach { forEach { s -> if (s != 0) it.TextS(s) }; it.TextB() }
                    }
                    }
                }
            }
        }; LaunchedEffect(this) {
        if (isNotEmpty() && visible) stateList.scrollToItem(lastIndex)
    }
    }
}







