package com.extra.cyclyx.utils

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