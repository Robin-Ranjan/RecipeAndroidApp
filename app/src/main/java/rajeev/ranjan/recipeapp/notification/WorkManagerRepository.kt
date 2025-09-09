package rajeev.ranjan.recipeapp.notification

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class WorkManagerRepository(
    private val workManager: WorkManager
) {

    companion object {
        private const val RECIPE_NOTIFICATION_WORK_NAME = "recipe_notification_periodic_work"
        private const val RECIPE_NOTIFICATION_CHAIN_WORK = "recipe_notification_chain_work"
    }

    fun scheduleRecipeNotificationsEvery1Hour() {
        // Cancel any existing work first
        cancelRecipeNotifications()

        // Start the chain
        scheduleNextNotificationWork()
    }

    private fun scheduleNextNotificationWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(false)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<RecipeNotificationWorker>()
            .setConstraints(constraints)
            .setInitialDelay(1, TimeUnit.HOURS)
            .addTag("recipe_notifications_chain")
            .build()

        workManager.enqueueUniqueWork(
            RECIPE_NOTIFICATION_CHAIN_WORK,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun scheduleRecipeNotificationsMinimumInterval() {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(true)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<RecipeNotificationWorker>(
            repeatInterval = 60,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .setInitialDelay(10, TimeUnit.SECONDS)
            .addTag("recipe_notifications")
            .build()

        workManager.enqueueUniquePeriodicWork(
            RECIPE_NOTIFICATION_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    private fun cancelRecipeNotifications() {
        workManager.cancelUniqueWork(RECIPE_NOTIFICATION_WORK_NAME)
        workManager.cancelUniqueWork(RECIPE_NOTIFICATION_CHAIN_WORK)
        workManager.cancelAllWorkByTag("recipe_notifications_chain")
    }
}