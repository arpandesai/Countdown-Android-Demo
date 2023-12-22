package com.mobilefirst.countdown.notify.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobilefirst.countdown.notify.AppConstant
import com.mobilefirst.countdown.notify.AppLog
import com.mobilefirst.countdown.notify.MainActivity
import com.mobilefirst.countdown.notify.R
import com.mobilefirst.countdown.notify.model.ButtonState
import com.mobilefirst.countdown.notify.model.Status
import com.mobilefirst.countdown.notify.model.TimerViewModel
import com.mobilefirst.countdown.notify.service.TimerNotificationReceiver
import com.mobilefirst.countdown.notify.ui.ext.format

@ExperimentalAnimationApi

@Composable
fun TimerHomeScreen(activity: MainActivity, viewModel: TimerViewModel) {
    val timer by viewModel.viewState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
            contentAlignment = Alignment.Center

    ) {
        // Add a background ring
        RingRow(timer.remainingTime,viewModel)

        // Your existing Column
        Column(
            modifier = Modifier
                .width(330.dp)
                .height(330.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimerHeader()
            //RingRow(timer.remainingTime,viewModel)
            TimerTopSection(timer.timeDuration.format(), timer.remainingTime,viewModel)
            TimerButtons(activity,viewModel)
        }
    }


}

@Composable
fun TimerTopSection(time: String, remainingTime: Long,timerState: TimerViewModel) {
    val toggle by timerState.viewState.collectAsState()
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color.Green,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val colorCode= when (toggle.toggle) {
        ButtonState.START -> {
            Color.White
        }
        else -> {
            if (isTimeLessThan10Seconds(remainingTime)) alpha else Color.White
        }
    }

    // Calculate progress based on the remaining time

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Draw the animated ring using Canvas

        // Display the time text
        Text(
            text = time,
            fontSize = 31.sp,
            color = colorCode
        )
    }
}

@Composable
fun RingRow(remainingTime: Long,timerState: TimerViewModel ) {
    val toggle by timerState.viewState.collectAsState()

        val progress = if (toggle.remainingTime<=0)
        {
            0.0f

        }
        else
        {
            1 - (remainingTime.toFloat() / AppConstant.totalDuration.toFloat())
        }

        val infiniteTransition = rememberInfiniteTransition()
        val alpha by infiniteTransition.animateColor(
            initialValue = Color.Red,
            targetValue = Color.Green,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

    // Calculate progress based on the remaining time


    Row(
        modifier = Modifier
            .width(350.dp)
            .height(350.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Draw the animated ring using Canvas
        RingCanvas(
            modifier = Modifier .width(350.dp)
                .height(350.dp),
            color = if (isTimeLessThan10Seconds(remainingTime)) alpha else Color.White,
            progress = progress
        )
        
    }
}


@Composable
fun RingCanvas(modifier: Modifier = Modifier, color: Color, progress: Float) {
    Canvas(
        modifier = modifier
    ) {
        val strokeWidth = 31f
        val diameter = size.minDimension
        // Draw the outer ring with background color
        drawCircle(
            color = Color.DarkGray,
            style = Stroke(strokeWidth),
            radius = diameter / 2
        )

        // Draw the circular fill based on the progress with progress color
      //  val startAngle = 0f
        val startAngle = -90f         // Set the start angle to be at the top (12 o'clock position)
        val sweepAngle = 360 * progress
        val gradient = Brush.sweepGradient(listOf(color, color))
        drawArc(
            brush = gradient,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(strokeWidth)
        )

    }
}


@ExperimentalAnimationApi
@Composable
fun TimerButtons(activity: MainActivity,timerState: TimerViewModel ) {
    val toggle by timerState.viewState.collectAsState()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {

            timerState.resetTimer()
            AppConstant.isNotify =false
            AppConstant.cancelAllNotification(activity)
        }) {
            Icon(painter = painterResource(R.drawable.ic_stop), contentDescription = "stop button")
        }

        ButtonLayout(activity,timerState)
    }
}

@Composable
fun ButtonLayout(activity: MainActivity,timerState: TimerViewModel ) {
    val toggle by timerState.viewState.collectAsState()
    var text = ""
    var color: Color = MaterialTheme.colors.primaryVariant
    var textColor: Color = Color.White

    when (toggle.toggle) {
        ButtonState.START -> {
            text = "Start"
            color = MaterialTheme.colors.primaryVariant
            textColor = Color.White
        }
        ButtonState.PAUSE -> {
            text = "Pause"
            color = MaterialTheme.colors.secondary
            textColor = Color.Black
        }
        ButtonState.RESUME -> {
            text = "Resume"
            color = MaterialTheme.colors.secondaryVariant
            textColor = Color.Black
        }
        else -> {}
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(modifier = Modifier
            .clickable {
                timerState.resetTimer()
            }
            .padding(10.dp)
            .size(80.dp)
            .clip(CircleShape)
            .background(Color.DarkGray)
            .fillMaxWidth()) {
            Text(
                text = "Stop", color = Color.White, modifier = Modifier
                    .align(Alignment.Center)
                    .padding(8.dp)
            )
        }

        Box(modifier = Modifier
            .clickable {

                timerState.buttonSelection()

                if(toggle.status==Status.STARTED)
                {
                    AppConstant.cancelAllNotification(activity)
                    AppConstant.isNotify =true
                    setAlarmManager(activity,toggle.remainingTime)

                }
                else
                {
                    AppConstant.isNotify =false
                    AppConstant.cancelAllNotification(activity)
                }


            }
            .padding(10.dp)
            .size(80.dp)
            .clip(CircleShape)
            .background(color)
            .fillMaxWidth()) {
            Text(
                text = text, color = textColor, modifier = Modifier
                    .align(Alignment.Center)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun TimerHeader() {
    Text(
        text = "Count Down Timer",
        fontSize = 20.sp,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp),
        style = MaterialTheme.typography.h4
    )
}

fun setAlarmManager(context: Context,remainingTime:Long)
{

    val alarmTime=System.currentTimeMillis()+ remainingTime

    AppLog.e("alarmManager start $remainingTime")

    try
    {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, TimerNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)

        // Schedule the alarm to trigger after a delay (e.g., 0 milliseconds for immediate execution)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,alarmTime , pendingIntent)
        AppLog.e("alarmManager  end")
        AppConstant.isNotify
    }
    catch (ex: Exception)
    {
        AppLog.e("alarmManager  Exception"+ex)
    }

}

private fun isTimeLessThan10Seconds(time: Long) = time < 10000L