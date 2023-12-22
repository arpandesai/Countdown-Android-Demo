package com.mobilefirst.countdown.notify

import android.util.Log

/*General Log Statement.
...Disabled in release build*/
object AppLog {

    const val TAG="CountdownAndroidDemo:"

    
    fun e(message: String?) {

        Log.e(TAG, message.orEmpty())
    }

    
    fun l(message: String?) {

        Log.e(TAG, message.orEmpty())
    }

    
    fun i(message: String?) {

        Log.i(TAG, message.orEmpty())
    }

    
    fun v(message: String?) {

        Log.v(TAG, message.orEmpty())
    }


}
