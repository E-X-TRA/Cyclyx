package com.extra.cyclyx.utils

import android.graphics.Color
import java.util.concurrent.TimeUnit

const val ENCODED_STRING = "encoded_string"

val ONE_SECOND = TimeUnit.MILLISECONDS.convert(1,TimeUnit.SECONDS)

const val START_SERVICE = "start_service"
const val PAUSE_SERVICE = "pause_service"
const val STOP_SERVICE = "stop_service"

object MAPBOX_STYLE{
    const val DEFAULT = "Default"
    const val STREETS = "Mapbox Streets"
    const val OUTDOORS = "Mapbox Outdoors"
    const val TRAFFIC = "Mapbox Traffic"
}

object GENDER{
    const val MALE = "male"
    const val FEMALE = "female"
    const val UNSELECTED = ""
}

object WARNING_TYPES{
    const val REGISTRATION_MUST_NOT_NULL = 101
    const val NOT_ELIGIBLE_BERSEPEDA = 102
    const val IS_REFRESHING = 103
}

object STATUSES{
    const val LOADING = "loading"
    const val DONE = "done"
}

const val TRACKING_STARTED = "tracking_started"
const val TRACKING_PAUSED = "tracking_paused"
const val TRACKING_RESUMED = "tracking_resumed"
const val TRACKING_STOPPED = "tracking_stopped"

const val PERMISSION_FINE_LOCATION_REQUEST = 100

//maps const
const val SOURCE_ID = "SOURCE_ID"
const val CIRCLE_LAYER_ID = "CIRCLE_LAYER_ID"
const val LINE_LAYER_ID = "LINE_LAYER_ID"
const val CIRCLE_COLOR = Color.BLUE
const val LINE_COLOR = CIRCLE_COLOR
const val CIRCLE_RADIUS: Float = 3f
const val LINE_WIDTH: Float = 7.5f
const val SYMBOL_LAYER_RED_ID = "SYMBOL_LAYER_RED_ID"
const val SYMBOL_LAYER_BLUE_ID = "SYMBOL_LAYER_BLUE_ID"
const val ICON_ID = "icon_id"
//item type
const val RIWAYAT_ITEM_ERROR = 20
const val RIWAYAT_ITEM_NORMAL = 21
//onclick action
const val DELETE_ITEM = "delete_item"
const val DETAIL_ITEM = "detail_item"
//shared preferences key
const val SP_CYCLYX = "CYCLYX_PROFILE"
const val SP_SETTING = "com.extra.cyclyx_preferences"
const val USER_FIRST_NAME = "first_name"
const val USER_LAST_NAME = "last_name"
const val USER_BIRTHYEAR = "birthdate"
const val USER_HEIGHT = "height"
const val USER_WEIGHT = "weight"
const val USER_GENDER = "gender"
const val USER_TOKEN = "token"
//user challenges key
const val USER_TOTAL_DISTANCE = "total_distance"
const val USER_MAX_AVG_SPEED = "max_avg_speed"
const val USER_MAX_PEAK_SPEED = "max_peak_speed"
const val USER_TOTAL_DURATION = "total_duration"
const val USER_TOTAL_CALORIES = "total_calories"
//service
const val EXTRA_ROUTE = "extra_route"
const val EXTRA_ALT = "extra_alt"

object FIREBASE_CONSTANTS{
    const val BASE_KEY = "base"
    const val REFERENSI_KEY = "referensi"
    const val TANTANGAN_KEY = "tantangan"

    const val TIPS_ITEM = "tips"
    const val TUTORIAL_ITEM = "tutorial"
    const val MOTIVASI_ITEM = "motivasi"
}