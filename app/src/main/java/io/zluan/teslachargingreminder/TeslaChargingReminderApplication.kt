package io.zluan.teslachargingreminder

import android.app.Application
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
        context = this
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    /**
     * Set up WorkManager background job to fetch new network data every 30 minutes.
     *
     * The min interval for WorkManager to work is 15 mins.
     * TODO: add another worker for refreshing access_token every 45 days.
     */
    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(45, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            repeatingRequest
        )
    }

    companion object {
        lateinit var context: TeslaChargingReminderApplication
            private set
    }
}
