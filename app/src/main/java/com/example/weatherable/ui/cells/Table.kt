package com.example.weatherable.ui.cells

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherable.data.room.bluetooth_db.models.PressureModel
import com.example.weatherable.ui.viewmodel.MainViewModel
import com.example.weatherable.utilites.LP
import com.example.weatherable.utilites.getStringWasForChat
import com.example.weatherable.utilites.log
import com.example.weatherable.utilites.noRippleClickable
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Table(visible: Boolean, viewModel: MainViewModel) {
    val pressPoints by viewModel.presList.collectAsState()
    val tempPoints by viewModel.tempList.collectAsState()
    var scale by remember { mutableStateOf(1f) }
    val stateList = rememberLazyListState()
    var timeVis by remember { mutableStateOf(false) }
    var info by remember { mutableStateOf(Pair("", "")) }
    var coords by remember { mutableStateOf(Pair(0.dp, 0.dp)) }
    var sel by remember { mutableStateOf(-1) }
    val coroutine = rememberCoroutineScope()
    val dens = LocalDensity.current
    with(Modifier) {
        Visibility(visible = visible) {
            timeVis = false; sel = -1
            Card(
                width(600.dp)
                    .transformable(
                        rememberTransformableState { zoomChange, _, _ -> scale *= zoomChange })
                    .noRippleClickable { timeVis = false; sel = -1 }
                    .height(530.dp)
                    .padding(10.dp, 20.dp, 10.dp), RoundedCornerShape(20.dp),
                border = BorderStroke(2.dp, Black)
            ) {
                Visibility(timeVis) {
                    Column(fillMaxSize(), Top, CenterHorizontally) {
                        Card(
                            wrapContentSize().padding(top = 20.dp), border =
                            BorderStroke(2.dp, Red)
                        ) {
                            Text(
                                "${
                                    getStringWasForChat(info.first.toLong())
                                }, ??????????????????????: ${info.second}", padding(10.dp), textAlign = Center
                            )
                        }
                    }
                }
                Box(padding(top = 6.dp)) {
                    LazyRow(padding(start = 60.dp).scale(if (scale >= 1f) scale else 1f),
                        stateList, content = {
                            itemsIndexed(pressPoints) { i, item ->
                                if (item.pressure.startsWith("7")) Card(
                                    noRippleClickable {
                                        coroutine.launch(IO) {
                                            if (sel != i) {
                                                timeVis = false; delay(50L); timeVis = true
                                                info = Pair(item.id, tempPoints[i].temp); sel = i
                                            }
                                        }
                                    }.padding(top = (if (item.paddingTop.dp > 0.dp) item.paddingTop.dp else 0.dp) -
                                            if(i == pressPoints.lastIndex) 2.dp else 0.dp, end = 1.dp),
                                    border = sel.border(i), elevation = 0.dp
                                )
                                { item.text.TextV(pressPoints, i); "*".TextV(pressPoints, i) }
                            }
                        })
                    (0..4).forEach {
                        (0..4).forEach { s ->
                            if (s != 0)
                                Text(
                                    "${s.smallItem} -",
                                    offset(29.dp, it.line(s)),
                                    fontSize = 12.sp
                                )
                        }; Text(
                        "${it.decItem} -", offset(10.dp, it.decLine),
                        fontWeight = FontWeight.Bold
                    )
                    }
                }
            }
        }
    }
    LaunchedEffect(pressPoints) {
        if (pressPoints.isNotEmpty() && visible) stateList.scrollToItem(pressPoints.lastIndex)
    }
}

private val Int.decItem get() = listOf(780, 770, 760, 750, 740)[this]
private val Int.smallItem get() = listOf(0, 8, 6, 4, 2)[this]
private val PressureModel.text get() = "\n${pressure}"
private val Int.decLine get() = (this * 120).dp
private fun Int.line(s: Int) = decLine + (s * 24).dp
private val String.press get() = substringAfter("7").toFloat()
private val PressureModel.paddingTop get() = with(pressure.press - 80f) { this - this * 13  }

private fun Int.border(i: Int) = if (this == i) BorderStroke(2.dp, Red)
else BorderStroke(0.dp, Transparent)

@Composable
private fun String.TextV(pressPoints: LP, i: Int) = Text(
    this, Modifier.padding(4.dp),
    if (pressPoints[i].type.isEmpty()) Black else Blue,
    if (i == pressPoints.lastIndex) 12.sp else 8.sp
)





