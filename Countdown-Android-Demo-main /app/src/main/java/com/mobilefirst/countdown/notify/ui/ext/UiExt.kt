package com.mobilefirst.countdown.notify.ui.ext

import java.time.Duration
import kotlin.math.abs


fun Duration.format(): String {
    val seconds = abs(seconds)
    return String.format(
        "%02d:%02d",
        (seconds % 3600) / 60, // minutes
        seconds % 60       // seconds
    )

}