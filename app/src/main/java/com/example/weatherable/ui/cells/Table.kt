package com.example.weatherable.ui.cells

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherable.ui.viewmodel.MainViewModel
import kotlin.math.abs


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Table(visible: Boolean, viewModel: MainViewModel, k: Int = 4) {
    val pressPoints by viewModel.presList.collectAsState()
    var scale by remember { mutableStateOf(1f) }
    val stateList = rememberLazyListState()
    val textPress = listOf(700, 710, 720, 730, 740, 750, 760, 770, 780, 790, 800)
    val state = rememberTransformableState { zoomChange, _, _ -> scale *= zoomChange }
    LaunchedEffect(key1 = pressPoints, block = {
        stateList.animateScrollToItem(pressPoints.lastIndex)
    })
    Visibility(visible = visible) {
        Card(
            modifier = Modifier
                .width(600.dp)
                .transformable(state)
                .height(550.dp)
                .padding(start = 10.dp, end = 10.dp, top = 20.dp),
            shape = RoundedCornerShape(20.dp), border = BorderStroke(2.dp, Color.Black)
        ) {
            LazyRow(content = {
                itemsIndexed(pressPoints) { i, item ->
                    (pressPoints[0].pressure!!.toFloat()).also { prev ->
                        item.pressure!!.toFloat().also { act ->
                            Box {
                                (act.dp + (act - prev).dp * 4 - if (i == pressPoints.lastIndex)
                                    457.dp else 455.dp).also { v ->
                                    Text(
                                        text = "*", modifier = Modifier
                                            .padding(top = v, end = 10.dp),
                                        fontSize = if (i == pressPoints.lastIndex) 16.sp else 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "\n" + item.pressure!!, modifier = Modifier
                                            .padding(top = v, end = 10.dp),
                                        fontSize = if (i == pressPoints.lastIndex) 12.sp else 8.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }, modifier = Modifier.padding(start = 50.dp), state = stateList)

            (0..10).toList().forEach {
                Text(
                    text = "${textPress[it]} --",
                    modifier = Modifier.offset(10.dp, (it * 10 * 5).dp)
                )
            }
        }
    }
}


