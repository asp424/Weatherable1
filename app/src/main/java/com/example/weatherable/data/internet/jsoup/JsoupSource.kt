package com.example.weatherable.data.internet.jsoup

import android.content.Context
import com.example.weatherable.data.view_states.InternetResponse
import com.example.weatherable.utilites.ANA_URL
import com.example.weatherable.utilites.CHL_URL
import com.example.weatherable.utilites.CLASS_TEMP
import com.example.weatherable.utilites.CLASS_WIND
import com.example.weatherable.utilites.GEL_URL
import com.example.weatherable.utilites.GID_URL
import com.example.weatherable.utilites.GIS_ICON_LIST
import com.example.weatherable.utilites.GIS_ICON_LIST1
import com.example.weatherable.utilites.GIS_SUN_DOWN
import com.example.weatherable.utilites.GIS_SUN_DOWN1
import com.example.weatherable.utilites.GIS_SUN_UP
import com.example.weatherable.utilites.GIS_SUN_UP1
import com.example.weatherable.utilites.GIS_TEMP_TOD
import com.example.weatherable.utilites.KRM_URL
import com.example.weatherable.utilites.NOV_URL
import com.example.weatherable.utilites.TAG
import com.example.weatherable.utilites.YAN_TOD_DETAIL_RAIN
import com.example.weatherable.utilites.YAN_TOD_DETAIL_TEMP
import com.example.weatherable.utilites.YAN_URL
import com.example.weatherable.utilites.checkedCityUrlGisNow
import com.example.weatherable.utilites.checkedCityUrlGisTod
import com.example.weatherable.utilites.checkedCityUrlGisTom
import com.example.weatherable.utilites.checkedCityUrlYanDet
import com.example.weatherable.utilites.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.jsoup.Jsoup
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class JsoupSource {
    private val ioDispatcher = Dispatchers.IO
    suspend fun getCityValues(): InternetResponse =
        suspendCoroutine { continuation ->
            CoroutineScope(ioDispatcher).launch {
                continuation.resume(
                    InternetResponse.OnSuccess(
                        JSONObject()
                            .put("nov_value", getCityValue(NOV_URL))
                            .put("ana_value", getCityValue(ANA_URL))
                            .put("gel_value", getCityValue(GEL_URL))
                            .put("yan_temp", getOnSitesTemps(YAN_URL, CLASS_TEMP, 1))
                            .put("hydro_temp", getOnSitesTemps(GID_URL, TAG, 10, 1))
                            .put("gis_temp", getOnSitesTemps(KRM_URL, GIS_TEMP_TOD))
                            .put("krm_wind", getOnSitesTemps(KRM_URL, CLASS_WIND))
                            .put("chel_temp", getOnSitesTemps(CHL_URL, GIS_TEMP_TOD))
                    )
                )
            }
        }

    private suspend fun getCityValue(city: String): JSONObject =
        suspendCoroutine { continuation ->
            CoroutineScope(ioDispatcher).launch {
                runCatching {
                    continuation.resume(
                        JSONObject()
                            .put("temp", getOnSitesTemps(city, GIS_TEMP_TOD))
                            .put(
                                "water",
                                with(getOnSitesTemps(city, "now-info-item water", flag = 5)) {
                                    "${
                                        toString().substringAfter("<span class=\"sign\">")
                                            .substringBefore("</span>")
                                    }${
                                        toString().substringAfter("</span>")
                                            .substringBefore("</span>")
                                    }"
                                }
                            )
                    )
                }.onFailure {
                    continuation.resume(
                        JSONObject()
                            .put("temp", "Err")
                            .put("water", "Err")
                    )
                }
            }
        }

    suspend fun getGisData(context: Context): InternetResponse =
        suspendCoroutine { continuation ->
            CoroutineScope(Dispatchers.IO).launch {
                val listIconTod =
                    getOnSitesTemps(checkedCityUrlGisTod(context), GIS_ICON_LIST, 0, 5)
                val listIconTom =
                    getOnSitesTemps(checkedCityUrlGisTom(context), GIS_ICON_LIST, 0, 5)
                val sunUp = getOnSitesTemps(checkedCityUrlGisNow(context), GIS_SUN_UP)
                val sunDown = getOnSitesTemps(checkedCityUrlGisNow(context), GIS_SUN_DOWN)
                continuation.resume(
                    InternetResponse.OnSuccess(
                        JSONObject()
                            .put(
                                "gis_temp_tod",
                                getOnSitesTemps(checkedCityUrlGisTod(context), GIS_TEMP_TOD, 7, 4)
                            )
                            .put(
                                "gis_icon_tod", if (listIconTod!!.isEmpty())
                                    getOnSitesTemps(
                                        checkedCityUrlGisTod(context),
                                        GIS_ICON_LIST1,
                                        0,
                                        5
                                    ) else listIconTod
                            )
                            .put(
                                "gis_temp_tom",
                                getOnSitesTemps(checkedCityUrlGisTom(context), GIS_TEMP_TOD, 0, 4)
                            )
                            .put(
                                "gis_icon_tom", if (listIconTom!!.isEmpty())
                                    getOnSitesTemps(
                                        checkedCityUrlGisTom(context),
                                        GIS_ICON_LIST1,
                                        0,
                                        5
                                    ) else listIconTom
                            )
                            .put(
                                "gis_sun_up",
                                if (sunUp!!.isEmpty()) getOnSitesTemps(
                                    checkedCityUrlGisNow(context),
                                    GIS_SUN_UP1
                                ) else sunUp
                            )
                            .put(
                                "gis_sun_down",
                                if (sunDown!!.isEmpty()) getOnSitesTemps(
                                    checkedCityUrlGisNow(context), GIS_SUN_DOWN1
                                ) else sunDown
                            )
                    )
                )
            }
        }

    suspend fun getYanData(context: Context): InternetResponse =
        suspendCoroutine { continuation ->
            CoroutineScope(Dispatchers.IO).launch {
                continuation.resume(
                    InternetResponse.OnSuccess(
                        JSONObject().apply {
                            for (i in 0..3) {
                                put("yan_temp_tod$i", valueT(i, context))
                                put("yan_temp_rain$i", valueR(i, context))
                            }
                            for (i in 4..7) {
                                put("yan_temp_tom$i", valueT(i, context))
                                put("yan_temp_rain_t$i", valueR(i, context))
                            }
                        })
                )
            }
        }

    private suspend fun valueT(i: Int, context: Context) =
        getOnSitesTemps(checkedCityUrlYanDet(context), YAN_TOD_DETAIL_TEMP, i)

    private suspend fun valueR(i: Int, context: Context) =
        getOnSitesTemps(checkedCityUrlYanDet(context), YAN_TOD_DETAIL_RAIN, i)
}

suspend fun getOnSitesTemps(
    url: String,
    classOrTag: String,
    index: Int = 0,
    flag: Int = 0
): String? = suspendCoroutine { continuation ->
    runCatching {
        when (flag) {
            0 -> Jsoup.connect(url).get().getElementsByClass(classOrTag)[index]?.text()

            1 -> Jsoup.connect(url).get().getElementsByTag(classOrTag)[index]?.text()

            2 -> Jsoup.connect(url).get().getElementsByTag(classOrTag)[index]

            3 -> Jsoup.connect(url).get().getElementsByTag(classOrTag)

            4 -> Jsoup.connect(url).get()
                .getElementsByClass(classOrTag).text()

            5 -> Jsoup.connect(url).get().getElementsByClass(classOrTag)

            6 -> Jsoup.connect(url).get().getElementsByClass(classOrTag)
                .select("unit unit_temperature_c")

            else -> {
            }
        }
    }.onSuccess {
        it.log
        continuation.resume(it.toString())
    }.onFailure {
        continuation.resume("Err")
    }
}