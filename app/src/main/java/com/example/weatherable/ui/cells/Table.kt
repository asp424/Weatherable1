package com.example.weatherable.ui.cells

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherable.ui.viewmodel.MainViewModel
import com.example.weatherable.utilites.getStringWasForChat
import com.example.weatherable.utilites.noRippleClickable

val c = 0

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Table(visible: Boolean, viewModel: MainViewModel) {
    val pressPoints by viewModel.presList.collectAsState()
    var scale by remember { mutableStateOf(1f) }
    val stateList = rememberLazyListState()
    var timeVis by remember { mutableStateOf(false) }
    var time by remember { mutableStateOf("") }
    val textPress = listOf(780, 770, 760, 750, 740)
    val state = rememberTransformableState { zoomChange, _, _ -> scale *= zoomChange }

    LaunchedEffect(key1 = pressPoints, block = {
        if (pressPoints.isNotEmpty())
        stateList.scrollToItem(pressPoints.lastIndex)
    })

    Visibility(visible = visible) {
        Card(
            modifier = Modifier
                .width(600.dp)
                .transformable(state)
                .noRippleClickable { timeVis = false }
                .height(530.dp)
                .padding(start = 10.dp, end = 10.dp, top = 20.dp),
            shape = RoundedCornerShape(20.dp), border = BorderStroke(2.dp, Color.Black)
        ) {
            Visibility(visible = timeVis) {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Card(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(top = 20.dp),
                        border = BorderStroke(2.dp, Red)
                    ) {
                        Text(
                            text = getStringWasForChat(time.toLong()),
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }

            LazyRow(
                content = {
                    itemsIndexed(pressPoints) { i, item ->
                        (pressPoints[0].pressure!!.toFloat()).also { prev ->
                            item.pressure!!.toFloat().also { act ->
                                Box(modifier = Modifier.noRippleClickable {
                                    time = item.id
                                    timeVis = true
                                }) {
                                    (act.dp - (act - prev).dp * 13 - if (i == pressPoints.lastIndex)
                                        372.dp else 368.dp).also { v ->
                                        Text(
                                            text = "*", modifier = Modifier
                                                .padding(top = v, end = 10.dp),
                                            fontSize = if (i == pressPoints.lastIndex) 16.sp else 12.sp,
                                            fontWeight = FontWeight.Bold, color = Black
                                        )
                                        Text(
                                            text = "\n" + "${item.pressure!!}\n" + "${item.type}",
                                            modifier = Modifier
                                                .padding(top = v, end = 10.dp),
                                            fontSize = if (i == pressPoints.lastIndex) 12.sp else 8.sp,
                                            color = Black
                                        )
                                        }
                                }
                            }
                        }
                    }
                }, modifier = Modifier
                    .padding(start = 60.dp)
                    .scale(if (scale >= 1f) scale else 1f), state = stateList
            )

            (0..4).toList().forEach {
                Text(
                    text = "${textPress[it]}-",
                    modifier = Modifier.offset(10.dp, (it * 24 * 5).dp)
                )
            }
        }
    }
}




