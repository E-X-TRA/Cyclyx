package com.extra.cyclyx.utils

import android.graphics.Color
import java.util.concurrent.TimeUnit

const val ENCODED_STRING = "encoded_string"

val ONE_SECOND = TimeUnit.MILLISECONDS.convert(1,TimeUnit.SECONDS)

const val START_SERVICE = "start_service"
const val PAUSE_SERVICE = "pause_service"
const val STOP_SERVICE = "stop_service"

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
const val SYMBOL_LAYER_ID = "SYMBOL_LAYER_ID"
const val ICON_ID = "ICON_ID"
//onclick action
const val DELETE_ITEM = "delete_item"
const val DETAIL_ITEM = "detail_item"
//shared preferences key
const val USER_FIRST_NAME = "first_name"
const val USER_LAST_NAME = "last_name"
const val USER_USERNAME = "username"
const val USER_EMAIL = "email"
const val USER_PASSWORD = "password"
const val USER_BIRTHDATE = "birthdate"
const val USER_HEIGHT = "height"
const val USER_WEIGHT = "weight"
const val USER_GENDER = "gender"
const val USER_TOKEN = "token"