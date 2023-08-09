package com.example.weatherable.utilites

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherable.R
import com.example.weatherable.data.room.bluetooth_db.models.PressureModel
import java.text.SimpleDateFormat
import java.util.*


@RequiresApi(Build.VERSION_CODES.M)
fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                return true
            }
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                return true
            }
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                return true
            }
        }
    }
    return false
}

@SuppressLint("UnspecifiedImmutableFlag")
fun getPendingSelfIntent(
    context: Context?,
    action: String?,
    classWidget: Class<*>
): PendingIntent? {
    val intent = Intent(context, classWidget)
    intent.action = action
    return PendingIntent.getBroadcast(context, 0, intent, FLAG_IMMUTABLE)
}

val String.sA: String
    get() = substringAfter("data-text=\"")
val String.sB: String
    get() = substringBefore("\">")
val String.rep: Int
    get() = if (isNotEmpty()) filter { it.isDigit() }.toInt() else 0

val String.repD
    get() = if (isNotEmpty()) substringBefore(".").filter { it.isDigit() } +
            "." + substringAfter(".").filter { it.isDigit() } else ""
val String.repPlus: String
    get() = replace(",", ".").replace("+", "")

fun MutableList<String>.addItem(value: String) = run {
    this.apply {
        value.apply {
            add(sA.sB)
            add(sA.sA.sB)
            add(sA.sA.sA.sB)
            add(sA.sA.sA.sA.sB)
            add(sA.sA.sA.sA.sA.sB)
            add(sA.sA.sA.sA.sA.sA.sB)
            add(sA.sA.sA.sA.sA.sA.sA.sB)
            add(sA.sA.sA.sA.sA.sA.sA.sA.sB)
        }
    }
}

fun MutableList<String>.addToList(value: String) {
    var item = ""
    value.forEach {
        if (it.toString() != " ")
            item += it.toString()
        else {
            this.add(item)
            item = ""
        }
    }
    Thread.sleep(300L)
    this.add(item)
}

fun  getIconDayGis(value: String) = when (value) {
    "Пасмурно, ливневые осадки" -> R.drawable.gis_e
    "Пасмурно, небольшие осадки" -> R.drawable.gis_u
    "Облачно, небольшой снег" -> R.drawable.gis_d
    "Пасмурно, снежные зёрна" -> R.drawable.gis_h
    "Пасмурно, дымка" -> R.drawable.gis_i
    "Пасмурно, небольшой мокрый снег" -> R.drawable.gis_u
    "Пасмурно, сильный снег" -> R.drawable.gis_w
    "Пасмурно, туман" -> R.drawable.gis_i
    "Облачно, ливневый снег" -> R.drawable.gis_s
    "Пасмурно, небольшой снег с&nbsp;дождём" -> R.drawable.gis_u
    "Пасмурно, ливневый дождь" -> R.drawable.gis_l
    "Пасмурно, небольшой замерзающий дождь, гололед" -> R.drawable.gis_u
    "Переменная облачность, сильные осадки" -> R.drawable.gis_o
    "Пасмурно, снег с дождём" -> R.drawable.gis_e
    "Малооблачно, небольшой дождь" -> R.drawable.gis_t
    "Малооблачно, дождь" -> R.drawable.gis_r
    "Облачно, небольшой дождь" -> R.drawable.gis_k
    "Пасмурно, небольшой дождь" -> R.drawable.gis_n
    "Пасмурно, дождь" -> R.drawable.gis_l
    "Пасмурно, сильный дождь" -> R.drawable.gis_m
    "Облачно, дождь" -> R.drawable.gis_j
    "Переменная облачность, небольшой дождь" -> R.drawable.gis_k
    "Переменная облачность, дождь" -> R.drawable.gis_j
    "Пасмурно, снег" -> R.drawable.gis_h
    "Облачно, снег" -> R.drawable.gis_s
    "Пасмурно" -> R.drawable.gis_i
    "Пасмурно, без осадков" -> R.drawable.gis_i
    "Облачно, без осадков" -> R.drawable.gis_c
    "Облачно" -> R.drawable.gis_c
    "Малооблачно" -> R.drawable.gis_g
    "Малооблачно, без осадков" -> R.drawable.gis_g
    "Малооблачно, поземок" -> R.drawable.gis_g
    "Малооблачно, снег" -> R.drawable.gis_x
    "Ясно, поземок" -> R.drawable.gis_b
    "Малооблачно, ливневый снег" -> R.drawable.gis_f
    "Пасмурно, мокрый снег" -> R.drawable.gis_e
    "Переменная облачность" -> R.drawable.gis_c
    "Переменная облачность, небольшой снег" -> R.drawable.gis_d
    "Ясно" -> R.drawable.gis_b
    "Пасмурно, небольшой снег" -> R.drawable.gis_a
    "Малооблачно, небольшой снег" -> R.drawable.gis_p
    "Пасмурно, небольшой замерзающий дождь" -> R.drawable.gis_u
    "Пасмурно, небольшой снег с дождём" -> R.drawable.gis_u
    "Пасмурно, замерзающий дождь" -> R.drawable.gis_e
    "Облачно, ливневый дождь" -> R.drawable.gis_j
    "Малооблачно, ливневые осадки" -> R.drawable.gis_q
    "Малооблачно, небольшие осадки" -> R.drawable.gis_v
    "Малооблачно, небольшой  дождь" -> R.drawable.gis_v
    "Пасмурно, ливневый снег" -> R.drawable.gis_h
    "Малооблачно, ливневый дождь" -> R.drawable.gis_r
    "Пасмурно, осадки" -> R.drawable.gis_e
    "Малооблачно, туман" -> R.drawable.gis_g
    "Малооблачно, дымка" -> R.drawable.gis_g
    else -> R.drawable.logo
}

fun getIconNightGis(value: String) = when (value) {
    "Пасмурно, ливневые осадки" -> R.drawable.gis_e
    "Пасмурно, небольшие осадки" -> R.drawable.gis_u
    "Облачно, небольшой снег" -> R.drawable.gis_j_n
    "Пасмурно, снежные зёрна" -> R.drawable.gis_h
    "Пасмурно, дымка" -> R.drawable.gis_i
    "Пасмурно, без осадков" -> R.drawable.gis_i
    "Пасмурно, небольшой мокрый снег" -> R.drawable.gis_u
    "Пасмурно, небольшой снег с дождём" -> R.drawable.gis_u
    "Пасмурно, сильный снег" -> R.drawable.gis_w
    "Пасмурно, туман" -> R.drawable.gis_i
    "Облачно, ливневый снег" -> R.drawable.gis_s_n
    "Пасмурно, осадки" -> R.drawable.gis_e
    "Пасмурно, небольшой снег с&nbsp;дождём" -> R.drawable.gis_u
    "Малооблачно, ливневый дождь" -> R.drawable.gis_e_n
    "Пасмурно, ливневый дождь" -> R.drawable.gis_l
    "Пасмурно, небольшой замерзающий дождь, гололед" -> R.drawable.gis_u
    "Облачно, ливневый дождь" -> R.drawable.gis_f_n
    "Малооблачно, снег" -> R.drawable.gis_x_n
    "Пасмурно, небольшой снег" -> R.drawable.gis_a
    "Пасмурно, мокрый снег" -> R.drawable.gis_e
    "Пасмурно" -> R.drawable.gis_i
    "Пасмурно, снег" -> R.drawable.gis_h
    "Облачно, снег" -> R.drawable.gis_s_n
    "Пасмурно, снег с дождём" -> R.drawable.gis_e
    "Пасмурно, небольшой дождь" -> R.drawable.gis_n
    "Пасмурно, дождь" -> R.drawable.gis_l
    "Пасмурно, сильный дождь" -> R.drawable.gis_m
    "Пасмурно, замерзающий дождь" -> R.drawable.gis_e
    "Малооблачно" -> R.drawable.gis_c_n
    "Облачно, без осадков" -> R.drawable.gis_c_n
    "Малооблачно, туман" -> R.drawable.gis_c_n
    "Малооблачно, поземок" -> R.drawable.gis_c_n
    "Малооблачно, ливневый снег" -> R.drawable.gis_k_n
    "Облачно" -> R.drawable.gis_a_n
    "Переменная облачность" -> R.drawable.gis_a_n
    "Ясно" -> R.drawable.gis_b_n
    "Ясно, поземок" -> R.drawable.gis_b_n
    "Малооблачно, небольшой снег" -> R.drawable.gis_d_n
    "Малооблачно, дождь" -> R.drawable.gis_e_n
    "Малооблачно, небольшой  дождь" -> R.drawable.gis_e_n
    "Малооблачно, ливневые осадки" -> R.drawable.gis_q_n
    "Облачно, дождь" -> R.drawable.gis_f_n
    "Малооблачно, небольшой дождь" -> R.drawable.gis_t_n
    "Малооблачно, дымка" -> R.drawable.gis_c_n
    "Малооблачно, без осадков" -> R.drawable.gis_c_n
    "Облачно, небольшой дождь" -> R.drawable.gis_h_n
    "Переменная облачность, сильные осадки" -> R.drawable.gis_g_n
    "Переменная облачность, небольшой дождь" -> R.drawable.gis_h_n
    "Переменная облачность, дождь" -> R.drawable.gis_f_n
    "Переменная облачность, небольшой снег" -> R.drawable.gis_j_n
    "Пасмурно, небольшой замерзающий дождь" -> R.drawable.gis_u
    "Малооблачно, небольшие осадки" -> R.drawable.gis_v_n
    "Пасмурно, ливневый снег" -> R.drawable.gis_h

    else -> R.drawable.logo
}

fun getIconNightYan(value: String) = when (value) {
    "Туман" -> R.drawable.gis_i
    "Снег" -> R.drawable.gis_h
    "Дождь" -> R.drawable.gis_f_n
    "Небольшой дождь" -> R.drawable.gis_h_n
    "Дождь со снегом" -> R.drawable.gis_e
    "Ясно" -> R.drawable.gis_b_n
    "Небольшой снег" -> R.drawable.gis_a
    "Пасмурно" -> R.drawable.gis_i
    "Облачно с прояснениями" -> R.drawable.gis_a_n
    "Малооблачно" -> R.drawable.gis_c_n
    else -> R.drawable.logo
}

fun getIconDayYan(value: String) = when (value) {
    "Туман" -> R.drawable.gis_i
    "Снег" -> R.drawable.gis_h
    "Дождь" -> R.drawable.gis_j
    "Дождь со снегом" -> R.drawable.gis_e
    "Небольшой дождь" -> R.drawable.gis_k
    "Ясно" -> R.drawable.gis_b
    "Небольшой снег" -> R.drawable.gis_a
    "Пасмурно" -> R.drawable.gis_i
    "Облачно с прояснениями" -> R.drawable.gis_c
    "Малооблачно" -> R.drawable.gis_g
    else -> R.drawable.logo
}

fun setStateScreen(mainActivity: Context, screen: Int, name: String) {
    val sharedPref = mainActivity.getSharedPreferences(name, 0x0000) ?: return
    with(sharedPref.edit()) {
        putInt(name, screen)
        apply()
    }
}

fun getStateScreen(mainActivity: Context, name: String): Int {
    val sharedPref = mainActivity.getSharedPreferences(name, 0x0000)
    return sharedPref.getInt(name, 0)
}

fun setCity(mainActivity: Context, value: String) {
    val sharedPref = mainActivity.getSharedPreferences("value", 0x0000) ?: return
    with(sharedPref.edit()) {
        putString("value", value)
        apply()
    }
}

fun getCity(mainActivity: Context): String? {
    val sharedPref = mainActivity.getSharedPreferences("value", 0x0000)
    return sharedPref.getString("value", "Крымск")
}

fun checkedCityUrlGisNow(context: Context) = when (getCity(context)) {
    "Челябинск" -> GIS_URL_CHEL
    "Пушкин" -> GIS_URL_PUSH
    "Москва" -> GIS_URL_MOSC
    "Крымск" -> GIS_URL_KRYM
    else -> GIS_URL_KRYM
}

fun checkedCityUrlGisTod(context: Context) = when (getCity(context)) {
    "Челябинск" -> GIS_URL_CHEL_TOD
    "Пушкин" -> GIS_URL_PUSH_TOD
    "Москва" -> GIS_URL_MOSC_TOD
    "Крымск" -> GIS_URL_KRYM_TOD
    else -> GIS_URL_KRYM_TOD
}

fun checkedCityUrlGisTom(context: Context) = when (getCity(context)) {
    "Челябинск" -> GIS_URL_CHEL_TOM
    "Пушкин" -> GIS_URL_PUSH_TOM
    "Москва" -> GIS_URL_MOSC_TOM
    "Крымск" -> GIS_URL_KRYM_TOM
    else -> GIS_URL_KRYM_TOM
}

fun checkedCityUrlYanNow(context: Context) = when (getCity(context)) {
    "Челябинск" -> YAN_URL_CHEL
    "Пушкин" -> YAN_URL_PUSH
    "Москва" -> YAN_URL_MOSC
    "Крымск" -> YAN_URL_KRYM
    else -> YAN_URL_KRYM
}

fun checkedCityUrlYanDet(context: Context) = when (getCity(context)) {
    "Челябинск" -> YAN_URL_CHEL_DETAILS
    "Пушкин" -> YAN_URL_PUSH_DETAILS
    "Москва" -> YAN_URL_MOSC_DETAILS
    "Крымск" -> YAN_URL_KRYM_DETAILS
    else -> YAN_URL_KRYM_DETAILS
}

fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("H:mm", Locale.getDefault())
    return timeFormat.format(time)
}

fun getStringWasForChat(wasDate: Long): String {
    val timeWas = wasDate.toString().asTime()
    val dateNow = Calendar.getInstance(Locale.getDefault())
    val monthLitWas = formatDate("MMM", wasDate)
    val monthNumWas = formatDate("MM", wasDate)
    val yearWas = formatDate("yyyy", wasDate)
    val yearWasSmall = formatDate("yy", wasDate)
    val dayOfMonthWas = formatDate("d", wasDate)
    val dayOfYearWas = formatDate("D", wasDate)
    val yearNow = dateNow.get(Calendar.YEAR)
    return when (dateNow.get(Calendar.DAY_OF_YEAR) - dayOfYearWas.toInt()) {
        1 -> " вчера в $timeWas"
        2 -> " позавчера в $timeWas"
        0 -> " сегодня в  $timeWas"
        else -> {
            when (yearNow - yearWas.toInt()) {
                1 -> " $dayOfMonthWas.$monthNumWas.$yearWasSmall в $timeWas"
                0 -> " $dayOfMonthWas $monthLitWas в $timeWas"
                else -> " в $yearWas году"
            }
        }
    }
}

private fun formatDate(value: String, date: Long): String {
    return SimpleDateFormat(value, Locale.getDefault()).format(date)
}

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

val <T> T.log get() = Log.d("My", toString())

typealias LP = List<PressureModel>

typealias SP = SnapshotStateList<Int>

val DarkGreen = Color(0xFF103C12)

val Int.decItem get() = listOf(780, 770, 760, 750, 740)[this]
val Int.smallItem get() = listOf(0, 8, 6, 4, 2)[this]
val PressureModel.text get() = "\n${pressure}\n     $type"
val Int.decLine get() = (this * 120).dp
fun Int.lineS(s: Int) = decLine + (s * 24).dp
val String.press get() = substringAfter("7").toFloat()
val PressureModel.paddingTop get() = with(pressure.press - 80f) { this - this * 13 }

fun LP.padding(i: Int) = (if (get(i).paddingTop.dp > 0.dp) get(i).paddingTop.dp else 0.dp) -
        if (i == lastIndex) 2.dp else 0.dp

fun Int.border(i: Int) = if (this == i) BorderStroke(2.dp, Color.Red)
else BorderStroke(0.dp, Color.Transparent)

@Composable
fun LP.TextV(text: String, i: Int) = Text(
    text, Modifier.padding(6.dp), when {
        getStringWasForChat(get(i).id.toLong()).startsWith(" сегодня") -> Blue
        else -> DarkGreen
    }, if (i == lastIndex) 12.sp else 8.sp
)

@Composable
fun Int.TextS(small: Int) = Text(
    "${small.smallItem} -", Modifier.offset(36.dp, lineS(small)),
    fontSize = 12.sp
)

@Composable
fun Int.TextB() = Text(
    "$decItem -", Modifier.offset(10.dp, decLine), fontWeight = Bold
)

