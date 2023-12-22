package com.mobilefirst.countdown.notify.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mobilefirst.countdown.notify.AppConstant
import com.mobilefirst.countdown.notify.AppLog

class TimerNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?)
    {
        AppLog.e("TimerNotificationReceiver  onReceive")
        if(AppConstant.isNotify)
        {
            AppConstant.showNotification(context!!, "Timer Finished", "Your timer has finished!")
        }

    }

}
