package com.raywenderlich.android.rwdc2018.service

import android.app.IntentService
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.raywenderlich.android.rwdc2018.app.SongUtils

class DownloadIntentService : IntentService("DownloadIntentService") {

    companion object {
        private const val TAG = "DownloadIntentService"
        private const val ACTION_DOWNLOAD = "ACTION_DOWNLOAD"
        private const val EXTRA_URL = "EXTRA_URL"

        const val DOWNLOAD_COMPLETE = "DOWNLOAD_COMPLETE"
        const val DOWNLOAD_COMPLETE_KEY = "DOWNLOAD_COMPLETE_KEY"

        fun startActionDownload(context: Context, param: String) {
            val intent = Intent(context, DownloadIntentService::class.java).apply {
                action = ACTION_DOWNLOAD
                putExtra(EXTRA_URL, param)
            }
            context.startService(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Creating service")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "Destroying service")
    }

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_DOWNLOAD -> {
                handleActionDownload(intent.getStringExtra(EXTRA_URL))
            }
        }
    }

    private fun handleActionDownload(param: String) {
        Log.i(TAG, "Starting download for $param")
        SongUtils.download(param)
        Log.i(TAG, "Ending download for $param")

        Log.i(TAG, "Sending broadcast for $param")
        broadcastDownloadComplete(param)
    }

    private fun broadcastDownloadComplete(param: String) {
        var intent = Intent(DOWNLOAD_COMPLETE)
        intent.putExtra(DOWNLOAD_COMPLETE_KEY, param)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

}
