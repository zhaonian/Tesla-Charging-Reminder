package io.zluan.teslachargingreminder

import android.app.Application
import android.util.Log
import androidx.work.*
import io.zluan.teslachargingreminder.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class TeslaChargingReminderApplication : Application() {

    private val applicationScope = CoroutineScope(context = Dispatchers.Default)

    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    /**
     * Set up WorkManager background job to fetch new network data every 30 minutes.
     *
     * The min interval for WorkManager to work is 15 mins.
     */
    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(30, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        Log.d("TeslaChargingReminderApplication", "WorkManager: Periodic Work request for sync is scheduled")
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            repeatingRequest
        )
    }
}
