package com.example.weatherable.app_widgets

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.weatherable.activity.DetailGisActivity
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
class Gismeteo : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            CoroutineScope(Dispatchers.Default).launch {
                updateGisAppWidget(
                    context,
                    appWidgetManager,
                    appWidgetId
                )
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
internal suspend fun updateGisAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    updateGisViews(context) {
        appWidgetManager.updateAppWidget(appWidgetId, it)
    }
}


@SuppressLint("SimpleDateFormat", "UnspecifiedImmutableFlag")
suspend fun updateGisViews(context: Context?, function: (RemoteViews) -> Unit) {
    val views = RemoteViews(context?.packageName, R.layout.gismeteo)
    val nowTime = SimpleDateFormat("H:mm").format(Calendar.getInstance().time)
    views.setTextViewText(R.id.gis_time, nowTime)
    val nowTimeInt = nowTime.rep
    val sunUp = getOnSitesTemps(KRM_URL, GIS_SUN_UP, 0)!!.rep
    val sunDown = getOnSitesTemps(KRM_URL, GIS_SUN_UP, 1)!!.rep
    val value = getOnSitesTemps(KRM_URL, "div", flag = 3)?.sA?.sB!!
    views.apply {
        setOnClickPendingIntent(R.id.image_now_gis,
            PendingIntent.getActivity(context, 0,
                Intent(context, DetailGisActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0))
        setOnClickPendingIntent(R.id.image_gis,
            getPendingSelfIntent(context, "update", Gismeteo::class.java))
        setTextViewText(R.id.gis_text, getOnSitesTemps(GIS_URL_TOD, GIS_TEMP_TOD)
            ?.replace(",", ".")?.replace("+", "") + " °C")
    }
    if (nowTimeInt in sunUp..sunDown || nowTimeInt in sunDown..sunUp
    ) {
        views.setImageViewResource(R.id.image_now_gis, getIconDayGis(value))
        function(views)
    } else {
        views.setImageViewResource(R.id.image_now_gis, getIconNightGis(value))
        function(views)
    }
}

