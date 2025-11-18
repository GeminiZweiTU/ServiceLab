package com.example.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CountdownService : Service() {

    companion object {
        const val TAG = "CountdownService"
        const val EXTRA_SECONDS = "extra_seconds"
    }

    private val serviceScope = CoroutineScope(Dispatchers.Default)
    private var countdownJob: Job? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val seconds = intent?.getIntExtra(EXTRA_SECONDS, -1) ?: -1
        if (seconds <= 0) {
            Log.w(TAG, "Invalid seconds: $seconds (startId=$startId). Stopping self.")
            stopSelf(startId)
            return START_NOT_STICKY
        }

        Log.i(TAG, "Starting countdown from $seconds (startId=$startId)")

        countdownJob?.cancel()
        countdownJob = serviceScope.launch {
            var remaining = seconds
            while (isActive && remaining > 0) {
                Log.d(TAG, "T-minus $remaining")
                delay(1000)
                remaining--
            }
            if (isActive) Log.i(TAG, "Countdown complete!") else Log.i(TAG, "Countdown cancelled.")
            stopSelf(startId)
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null // Not a bound service

    override fun onDestroy() {
        Log.i(TAG, "Service destroyed. Cancelling jobs.")
        countdownJob?.cancel()
        serviceScope.cancel()
        super.onDestroy()
    }
}