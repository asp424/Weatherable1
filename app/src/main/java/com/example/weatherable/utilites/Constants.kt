package com.example.weatherable.utilites

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
const val UUID_VAL = "00001101-0000-1000-8000-00805F9B34FB"
const val DEVICE = "98:D3:31:70:0F:B8"
const val GIS_TEMP = "nowvalue__text_l"
const val YAN_RAIN = "link__condition"
const val YAN_TEMP = "temp__value"
const val GIS_URL_TOD = "https://www.gismeteo.ru/weather-krymsk-5212/"
const val GIS_URL_TOM = "https://www.gismeteo.ru/weather-krymsk-5212/tomorrow/"
const val GIS_TEMP_TOD = "unit unit_temperature_c"
const val GIS_SUN_UP = "nowastro__time"

const val GIS_ICON_LIST = "widget__item"
const val YAN_TOD_DETAIL_TEMP = "weather-table__temp"
const val YAN_TOD_DETAIL_RAIN = "weather-table__body-cell weather-table__body-cell_type_condition"
const val YAN_URL_DETAILS = "https://yandex.ru/pogoda/krymsk/details"
