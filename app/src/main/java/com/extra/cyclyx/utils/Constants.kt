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
const val SOURCE_ID = "SOURCE_ID";
const val CIRCLE_LAYER_ID = "CIRCLE_LAYER_ID";
const val LINE_LAYER_ID = "LINE_LAYER_ID";
const val CIRCLE_COLOR = Color.RED;
const val LINE_COLOR = CIRCLE_COLOR;
const val CIRCLE_RADIUS: Float = 3f;
const val LINE_WIDTH: Float = 2f;

//onclick action
const val DELETE_ITEM = "delete_item"
const val DETAIL_ITEM = "detail_item"