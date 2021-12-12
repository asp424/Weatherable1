package com.example.weatherable.app_widgets

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.weatherable.R
import com.example.weatherable.activity.DetailYanActivity
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
class Yandex : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            CoroutineScope(Dispatchers.IO).launch {
                setStateScreen(context, appWidgetId, "yan")
                updateYanAppWidget(
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
            val appWidgetManager = AppWidgetManager.getInstance(context)
            CoroutineScope(Dispatchers.IO).launch {
                updateGisViews(context, appWidgetManager) {
                    appWidgetManager
                        .updateAppWidget(getStateScreen(context!!, "gis"), VIEWS_GIS)
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                updateYanViews(context, appWidgetManager) {
                    appWidgetManager
                        .updateAppWidget(getStateScreen(context!!, "yan"), VIEWS_YAN)
                }
            }
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(
            context,
            appWidgetManager,
            appWidgetId, newOptions
        )
        // appWidgetManager?.updateAppWidget(appWidgetId, getView(context, newOptions))
        CoroutineScope(Dispatchers.IO).launch {
            updateYanAppWidget(context!!, appWidgetManager!!, appWidgetId)
        }
    }
}

@SuppressLint("SimpleDateFormat")
internal suspend fun updateYanAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    updateYanViews(context, appWidgetManager) {
        appWidgetManager.updateAppWidget(appWidgetId, VIEWS_YAN)
    }
}

@SuppressLint("SimpleDateFormat", "UnspecifiedImmutableFlag")
suspend fun updateYanViews(context: Context?, appWidgetManager: AppWidgetManager, function: () -> Unit) {
    VIEWS_YAN.apply {
        setViewVisibility(R.id.progress_yan, View.VISIBLE)
        appWidgetManager.updateAppWidget(getStateScreen(context!!, "yan"), this)
        val nowTime = SimpleDateFormat("H:mm").format(Calendar.getInstance().time)
        val sunUpPrOne = getOnSitesTemps(checkedCityUrlGisNow(context), GIS_SUN_UP, 0)!!
        val sunDownPrOne = getOnSitesTemps(checkedCityUrlGisNow(context), GIS_SUN_DOWN, 0)!!
        val value = getOnSitesTemps(checkedCityUrlYanNow(context), YAN_RAIN, 0)!!
        val sunUp = if (sunUpPrOne.isEmpty())
            getOnSitesTemps(checkedCityUrlGisNow(context), GIS_SUN_UP1, 0)!!.rep else sunUpPrOne.rep
        val sunDown = if (sunDownPrOne.isEmpty())
            getOnSitesTemps(checkedCityUrlGisNow(context), GIS_SUN_DOWN1, 0)!!.rep else sunDownPrOne.rep
        setOnClickPendingIntent(
            R.id.image_yan, getPendingSelfIntent(
                context,
                "update", Yandex::class.java
            )
        )
        setOnClickPendingIntent(
            R.id.image_now_yan,
            PendingIntent.getActivity(
                context, 0,
                Intent(context, DetailYanActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0
            )
        )
        setTextViewText(
            R.id.yan_text, getOnSitesTemps(
                checkedCityUrlYanNow(context),
                YAN_TEMP, 1
            )?.repPlus + " °C"
        )
        setTextViewText(R.id.yan_time, nowTime)

        if (nowTime.rep in sunUp..sunDown || nowTime.rep in sunDown..sunUp
        ) {
            setViewVisibility(R.id.progress_yan, View.INVISIBLE)
            setImageViewResource(R.id.image_now_yan, getIconDayYan(value))
            function()
        } else {
            setViewVisibility(R.id.progress_yan, View.INVISIBLE)
            setImageViewResource(R.id.image_now_yan, getIconNightYan(value))
            function()
        }
    }
}
