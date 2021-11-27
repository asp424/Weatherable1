package com.example.weatherable.app_widgets

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.example.weatherable.R
import com.example.weatherable.data.internet.jsoup.getOnSitesTemps
import com.example.weatherable.utilites.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Implementation of App Widget functionality.
 */
class Hydro: AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            CoroutineScope(Dispatchers.Default).launch {
                updateHydAppWidget(context, appWidgetManager, appWidgetId)
            }
        }
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action == "update") {
            val watchYanWidget = ComponentName(context!!, Yandex::class.java)
            val watchGisWidget = ComponentName(context, Gismeteo::class.java)
            val watchHydWidget = ComponentName(context, Hydro::class.java)
            CoroutineScope(Dispatchers.Default).launch {
                updateGisViews(context) {
                    AppWidgetManager.getInstance(context).updateAppWidget(watchGisWidget, it)
                }
            }
            CoroutineScope(Dispatchers.Default).launch {
                updateYanViews(context) {
                    AppWidgetManager.getInstance(context).updateAppWidget(watchYanWidget, it)
                }
            }
            CoroutineScope(Dispatchers.Default).launch {
                updateHydViews(context) {
                    AppWidgetManager.getInstance(context).updateAppWidget(watchHydWidget, it)
                }
            }
        }
    }
}


@SuppressLint("SimpleDateFormat")
private suspend fun updateHydAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    updateHydViews(context){
        appWidgetManager.updateAppWidget(appWidgetId, it)
    }
}


@SuppressLint("SimpleDateFormat")
 suspend fun updateHydViews(context: Context?, function: (RemoteViews) -> Unit) {
    val views = RemoteViews(context?.packageName, R.layout.hydro)
    views.setOnClickPendingIntent(
        R.id.image_yandex,
        getPendingSelfIntent(context, "update", Hydro::class.java)
    )
    val im = Glide.with(context!!)
        .asBitmap()
        .load(
            when (getOnSitesTemps(GID_URL, TAG, 15, 1)) {
                "Ливневый дождь со снегом" -> "https://meteoinfo.ru/images/ico/24d_s.png"
                "Количество облаков не изменилось" -> "https://meteoinfo.ru/images/ico/5d_s.png"
                "Снег слабый непрерывный" -> "https://meteoinfo.ru/images/ico/2d_s.png"
                "Снег" -> "https://meteoinfo.ru/images/ico/2d_s.png"
                "Небольшая облачность, без осадков" -> "https://meteoinfo.ru/images/ico/7d_s.png"
                "Облачно с прояснениями, без осадков" -> "https://meteoinfo.ru/images/ico/6d_s.png"
                "Снег умеренный непрерывный" -> "https://meteoinfo.ru/images/ico/2d_s.png"
                "Облачно, кратковременный снег" -> "https://meteoinfo.ru/images/ico/13d_s.png"
                "Облачно, временами небольшие осадки" -> "https://meteoinfo.ru/images/ico/15d_s.png"
                "Облачно, без осадков" -> "https://meteoinfo.ru/images/ico/5d_s.png"
                "Облачно с прояснениями, временами небольшой снег" -> "https://meteoinfo.ru/images/ico/12d_s.png"
                "Переменная облачность, без осадков" -> "https://meteoinfo.ru/images/ico/6d_s.png"
                "Облачно, небольшой кратковременный снег" -> "https://meteoinfo.ru/images/ico/13d_s.png"
                else -> {
                    "https://meteoinfo.ru/images/ico/5d_s.png"
                }
            }
        )
        .submit()
    views.setTextViewText(
        R.id.appwidget_text,
        getOnSitesTemps(GID_URL, TAG, 10, 1) + " °C"
    )
    views.setTextViewText(
        R.id.hyd_time,
        SimpleDateFormat("HH:mm").format(Calendar.getInstance().time.time)
    )
    runCatching {
        views.setImageViewBitmap(R.id.image_now, im.get())
    }
    function(views)
}


