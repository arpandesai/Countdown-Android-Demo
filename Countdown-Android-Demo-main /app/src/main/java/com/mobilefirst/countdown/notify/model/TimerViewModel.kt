package com.mobilefirst.countdown.notify.model


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.Duration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilefirst.countdown.notify.AppConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {

    private val _viewState = MutableStateFlow(TimerModel())
    val viewState: StateFlow<TimerModel> = _viewState

    private var timerJob: Job? = null

    init {
        _viewState.value = TimerModel()
    }


    private fun startTime(duration: Duration) {
        timerJob?.cancel() // Cancel any existing timer
        val endTime = System.currentTimeMillis() + duration.toMillis()
        _viewState.value = TimerModel(
            timeDuration = duration,
            remainingTime = duration.toMillis(),
            status = Status.RUNNING,
            toggle = ButtonState.PAUSE
        )
        timerJob = viewModelScope.launch(Dispatchers.IO) {
            // ... background work ...

            while (System.currentTimeMillis() < endTime) {
                val remainingTime = endTime - System.currentTimeMillis()
                _viewState.value = TimerModel(
                    timeDuration = Duration.ofMillis(remainingTime),
                    remainingTime = remainingTime,
                    status = Status.RUNNING,
                    toggle = ButtonState.PAUSE
                )
                delay(10) // Update every 10 milliseconds
            }

            // Handle timer completion
            _viewState.value = _viewState.value.copy(
                status = Status.STARTED,
                timeDuration = Duration.ofMillis(AppConstant.totalDuration),
                remainingTime = AppConstant.totalDuration,
                toggle = ButtonState.START
            )

        }

    }

    private fun pauseTimer() {
        timerJob?.cancel()
        _viewState.value = _viewState.value.copy(
            status = Status.STARTED,
            toggle = ButtonState.RESUME
        )
    }

    fun resetTimer() {

        timerJob?.cancel()
        _viewState.value = _viewState.value.copy(
            status = Status.STARTED,
            timeDuration = Duration.ofMillis(AppConstant.totalDuration),
            remainingTime = AppConstant.totalDuration,
            toggle = ButtonState.START
        )
    }

    fun buttonSelection() {
        val state = _viewState.value

        when (state.status) {
            Status.STARTED -> {

                if(state.toggle==ButtonState.RESUME)
                {
                    startTime(state.timeDuration)
                }
                else
                {

                    startTime(Duration.ofMillis(AppConstant.totalDuration))
                }

            }
            Status.RUNNING -> {
                pauseTimer()
            }
            Status.FINISHED -> {
                resetTimer()
            }
            else -> {}
        }
    }
}
