package com.example.weatherable.utilites

import android.widget.RemoteViews
import com.example.weatherable.R

const val KRM_URL = "https://www.gismeteo.ru/weather-krymsk-5212/now/"
const val NOV_URL = "https://www.gismeteo.ru/weather-novorossysk-5214/now/"
const val ANA_URL = "https://www.gismeteo.ru/weather-anapa-5211/now/"
const val GEL_URL = "https://www.gismeteo.ru/weather-gelendzhik-5213/now/"
const val CHL_URL = "https://www.gismeteo.ru/weather-chelyabinsk-4565/now/"
const val YAN_URL = "https://yandex.ru/pogoda/krymsk"
const val GID_URL = "https://meteoinfo.ru/pogoda/russia/krasnodar-territory/krymsk"
const val CLASS_CITY = "nowvalue__text_l"
const val CLASS_WIND = "unit unit_wind_m_s"
const val CLASS_TEMP = "temp__value"
const val CLASS_INFO = "nowinfo__value"
const val TAG = "td"
const val GIS_DIV_TAG = "div"
const val UUID_VAL = "00001101-0000-1000-8000-00805F9B34FB"
const val DEVICE = "98:D3:31:70:0F:B8"
const val GIS_TEMP = "now-info-item water"
const val YAN_RAIN = "link__condition"
const val YAN_TEMP = "temp__value"
const val GIS_URL_TOD = "https://www.gismeteo.ru/weather-krymsk-5212/"
const val GIS_URL_TOM = "https://www.gismeteo.ru/weather-krymsk-5212/tomorrow/"
const val GIS_TEMP_TOD = "unit unit_temperature_c"
const val GIS_SUN_UP = "now-astro-sunrise"
const val GIS_SUN_DOWN = "now-astro-sunset"
const val GIS_SUN_UP1 = "nowastro__item nowastro__item_sunrise"
const val GIS_SUN_DOWN1 = "nowastro__item nowastro__item_sunset"
const val GIS_ICON_LIST = "widget-row widget-row-icon"
const val GIS_ICON_LIST1 = "widget__row widget__row_table widget__row_icon"
const val YAN_TOD_DETAIL_TEMP = "weather-table__temp"
const val YAN_TOD_DETAIL_RAIN = "weather-table__body-cell weather-table__body-cell_type_condition"
const val YAN_URL_DETAILS = "https://yandex.ru/pogoda/krymsk/details"
//Change this URLs for get yours city weather info
//Gis
const val GIS_URL_CHEL = "https://www.gismeteo.ru/weather-chelyabinsk-4565/now/"
const val GIS_URL_KRYM = "https://www.gismeteo.ru/weather-krymsk-5212/now/"
const val GIS_URL_PUSH = "https://www.gismeteo.ru/weather-pushkin-23501/now/"
const val GIS_URL_MOSC = "https://www.gismeteo.ru/weather-moscow-4368/now/"

const val GIS_URL_CHEL_TOD = "https://www.gismeteo.ru/weather-chelyabinsk-4565/"
const val GIS_URL_KRYM_TOD = "https://www.gismeteo.ru/weather-krymsk-5212/"
const val GIS_URL_MOSC_TOD = "https://www.gismeteo.ru/weather-moscow-4368/"
const val GIS_URL_PUSH_TOD = "https://www.gismeteo.ru/weather-pushkin-23501/"

const val GIS_URL_CHEL_TOM = "https://www.gismeteo.ru/weather-chelyabinsk-4565/tomorrow/"
const val GIS_URL_KRYM_TOM = "https://www.gismeteo.ru/weather-krymsk-5212/tomorrow/"
const val GIS_URL_MOSC_TOM = "https://www.gismeteo.ru/weather-moscow-4368/tomorrow/"
const val GIS_URL_PUSH_TOM = "https://www.gismeteo.ru/weather-pushkin-23501/tomorrow/"
//Yandex
const val YAN_URL_CHEL = "https://yandex.ru/pogoda/chelyabinsk"
const val YAN_URL_KRYM = "https://yandex.ru/pogoda/?lat=44.93541336&lon=37.98788834"
const val YAN_URL_MOSC = "https://yandex.ru/pogoda/?lat=55.75581741&lon=37.61764526"
const val YAN_URL_PUSH = "https://yandex.ru/pogoda/?lat=59.72233582&lon=30.41676521"

const val YAN_URL_CHEL_DETAILS = "https://yandex.ru/pogoda/chelyabinsk/details"
const val YAN_URL_KRYM_DETAILS = "https://yandex.ru/pogoda/details?lat=44.93541336&lon=37.98788834&via=ms"
const val YAN_URL_MOSC_DETAILS = "https://yandex.ru/pogoda/details?lat=55.75581741&lon=37.61764526&via=ms"
const val YAN_URL_PUSH_DETAILS = "https://yandex.ru/pogoda/details?lat=59.72233582&lon=30.41676521&via=ms"
val VIEWS_GIS = RemoteViews("com.example.weatherable", R.layout.gismeteo)
val VIEWS_YAN = RemoteViews("com.example.weatherable", R.layout.yandex)
val VIEWS_HYD = RemoteViews("com.example.weatherable", R.layout.hydro)