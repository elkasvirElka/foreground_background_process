package com.raywenderlich.android.rwdc2018.service

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.raywenderlich.android.rwdc2018.app.PhotosUtils

//broadcast receiver +
class FetchIntentService : IntentService("FetchIntentService") {

    companion object {
        private const val TAG = "FetchIntentService"
        private const val ACTION_FETCH = "ACTION_FETCH"
        const val FETCH_COMPLETE = "FETCH_COMPLETE"

        fun startActionFetch(context: Context) {
            val intent = Intent(context, FetchIntentService::class.java).apply {
                action = ACTION_FETCH
            }
            context.startService(intent)
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action){
            ACTION_FETCH  -> {
                handleActionFetch()
            }
        }
    }


    private fun handleActionFetch(){
        try {
            PhotosUtils.fetchJsonString()
        }catch (e: InterruptedException){
            Log.e(TAG, "Error downloading JSON: "+ e.message)
        }
    }
    private fun broadcastFetchComplete(){
        val intent = Intent(FETCH_COMPLETE)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

}
