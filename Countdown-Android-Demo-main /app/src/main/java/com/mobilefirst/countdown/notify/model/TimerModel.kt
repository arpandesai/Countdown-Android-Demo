package com.mobilefirst.countdown.notify.model

import com.mobilefirst.countdown.notify.AppConstant
import java.time.Duration

data class TimerModel(
    val timeDuration: Duration = Duration.ofMillis(AppConstant.totalDuration),
    val remainingTime: Long = timeDuration.toMillis(),
    val status: Status = Status.STARTED,
    val toggle: ButtonState = ButtonState.START
)

enum class Status {
    STARTED, RUNNING, FINISHED
}

enum class ButtonState {
    START, PAUSE, RESUME
}