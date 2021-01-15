package io.zluan.teslachargingreminder.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.zluan.teslachargingreminder.repository.StatsRepository
import retrofit2.HttpException

/** Worker to refresh the data. */
class RefreshDataWorker(appContext: Context, params: WorkerParameters)
    : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val statsRepository = StatsRepository(applicationContext)
        try {
            statsRepository.refreshChargeState()
        } catch (e: HttpException) {
            return Result.retry()
        }
        return Result.success()
    }

    companion object {
        const val WORK_NAME = "io.zluan.teslachargingreminder.work.RefreshDataWorker"
    }
}
