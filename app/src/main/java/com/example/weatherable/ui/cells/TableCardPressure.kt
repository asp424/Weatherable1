package com.example.weatherable.ui.cells

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherable.ui.viewmodel.MainViewModel
import com.example.weatherable.utilites.getStringWasForChat

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TableCard(visible: Boolean, viewModel: MainViewModel, onClick: () -> Unit) {
    val points by remember(viewModel) {
        viewModel.pointsList
    }.collectAsState()
    val press by remember(viewModel) {
        viewModel.presList
    }.collectAsState()
    val dens = LocalDensity.current.density
    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offset += offsetChange
    }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    val list = (1..39).toList()
    val list1 = (780 downTo 740).toList()
    val scroll = rememberScrollState()
    val scroll1 = rememberScrollState()
    LaunchedEffect(true) {
        scroll.animateScrollTo(0)
        scroll1.animateScrollTo(1700)
    }
    val paint = Paint().asFrameworkPaint()
    val bounds = Rect()
    Visibility(visible = visible) {
        Card(
            modifier = Modifier
                .width(LocalConfiguration.current.screenWidthDp.dp)
                .height(LocalConfiguration.current.screenHeightDp.dp / 2)
                .padding(start = 10.dp, end = 10.dp, top = 20.dp),
            shape = RoundedCornerShape(20.dp), border = BorderStroke(2.dp, Color.Black),
            backgroundColor = Color.Yellow
        ) {

            Column(
                modifier = Modifier
                    .verticalScroll(scroll1)
                    .padding(start = 10.dp, bottom = 20.dp)
                    .wrapContentHeight()
            ) {
                Box(Modifier.padding(start = 60.dp)) {
                    Canvas(modifier = Modifier
                        .graphicsLayer {
                            translationX = scale
                        }, onDraw = {
                        points!!.forEachIndexed { i, of ->
                            var color = Green
                            if (getStringWasForChat(press?.get(i)?.id!!.toLong()).endsWith(" "))
                                color = Black
                            drawPoints(
                                points = points!!,
                                color = color,
                                pointMode = PointMode.Polygon,
                                cap = StrokeCap.Round
                            )
                            drawCircle(
                                center = of,
                                color = color, radius = 5f
                            )
                        }
                    })
                }
                Canvas(
                    modifier = Modifier, onDraw = {
                        list.forEachIndexed { index, fl ->
                            drawLine(
                                color = Color.Black, strokeWidth = 3f,
                                start = Offset(95f, fl * 100f + 0f),
                                end = Offset(81f, fl * 100f + 0f)
                            )
                        }
                        drawLine(
                            color = Color.Black,
                            strokeWidth = 3f,
                            start = Offset(95f, 0f),
                            end = Offset(95f, 100f * 40 - 17f)
                        )
                    })
                list1.forEach {
                    Text(
                        text = it.toString(),
                        modifier = Modifier.padding(bottom = 43f.dp / dens)
                    )
                }
            }
            Row(
                Modifier
                    .padding(start = 42.dp, bottom = 40.dp)
                    .horizontalScroll(scroll),
                verticalAlignment = Alignment.Bottom
            ) {
                Canvas(modifier = Modifier, onDraw = {

                    points?.forEachIndexed { index, fl ->
                        var s = 0f
                        paint.apply {
                            isAntiAlias = true
                            textSize = 12.sp.toPx()
                            getTextBounds(
                                getStringWasForChat(press?.get(index)?.id!!.toLong()),
                                0,
                                getStringWasForChat(press?.get(index)?.id!!.toLong()).length,
                                bounds
                            )
                        }
                        if (index != 0 && (press?.get(index)?.id!!.toLong()
                                    - press?.get(index - 1)?.id!!.toLong()) <= 60000L * 30
                        )
                            s = 1045f

                        drawIntoCanvas {
                            it.nativeCanvas.drawText(
                                getStringWasForChat(press?.get(index)?.id!!.toLong()),
                                fl.x + 37f,
                                s,
                                paint
                            )
                        }
                        drawLine(
                            color = Color.Black, strokeWidth = 3f,
                            start = Offset(fl.x + 83f, -59f),
                            end = Offset(fl.x + 83f, -48f)
                        )
                    }
                    drawLine(
                        color = Color.Black, strokeWidth = 3f,
                        start = Offset(0f, -60f),
                        end = Offset(6000f, -60f)
                    )
                })
                Row(modifier = Modifier.padding(start = 29.dp)) {
                    list.forEach {
                        Text(text = " ", modifier = Modifier.padding(end = 126.dp))
                    }
                }
            }
        }
        Row(
            Modifier
                .padding(top = 20.dp)
                .wrapContentHeight()
                .width(LocalConfiguration.current.screenWidthDp.dp),
            horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top
        ) {
            Header(string = "Давление", color = Color.White)
        }
        Row(
            Modifier
                .padding(top = 10.dp, end = 3.dp)
                .wrapContentHeight()
                .width(LocalConfiguration.current.screenWidthDp.dp),
            horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Top
        ) {
            Card(border = BorderStroke(2.dp, Color.White),
                modifier = Modifier
                    .clickable {
                        viewModel.clearTablesValues()
                        onClick()
                    }
                    .padding(top = 20.dp, end = 20.dp)) {
                Icon(
                    Icons.Outlined.Delete, contentDescription = null,
                    Modifier.size(30.dp)
                )
            }
        }
    }
    scale = -scroll.value.toFloat()
}




