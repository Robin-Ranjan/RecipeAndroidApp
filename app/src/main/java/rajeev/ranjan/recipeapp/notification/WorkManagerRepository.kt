package rajeev.ranjan.recipeapp.notification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class WorkManagerRepository(
    private val workManager: WorkManager
) {

    companion object {
        private const val RECIPE_NOTIFICATION_WORK_NAME = "recipe_notification_periodic_work"
        private const val RECIPE_NOTIFICATION_CHAIN_WORK = "recipe_notification_chain_work"
    }

    // Option 1: Using OneTimeWork with chaining for 10-second intervals
    fun scheduleRecipeNotificationsEvery10Seconds() {
        Log.d("WorkManagerRepository", "Scheduling recipe notifications every 10 seconds...")

        // Cancel any existing work first
        cancelRecipeNotifications()

        // Start the chain
        scheduleNextNotificationWork()
    }

    private fun scheduleNextNotificationWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(false) // Allow when battery is low for testing
            .build()

        val workRequest = OneTimeWorkRequestBuilder<RecipeNotificationWorker>()
            .setConstraints(constraints)
            .setInitialDelay(10, TimeUnit.SECONDS) // 10 second delay
            .addTag("recipe_notifications_chain")
            .build()

        workManager.enqueueUniqueWork(
            RECIPE_NOTIFICATION_CHAIN_WORK,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )

        Log.d("WorkManagerRepository", "Next notification work scheduled in 10 seconds")
    }

    // Option 2: Modified periodic work (minimum 15 minutes due to Android limitations)
    fun scheduleRecipeNotificationsMinimumInterval() {
        Log.d("WorkManagerRepository", "Scheduling recipe notifications with minimum interval...")

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(true)
            .build()

        // Minimum interval is 15 minutes for PeriodicWork
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

        Log.d("WorkManagerRepository", "Recipe notifications scheduled with 15-minute minimum interval")
    }

    fun cancelRecipeNotifications() {
        Log.d("WorkManagerRepository", "Cancelling all recipe notifications...")
        workManager.cancelUniqueWork(RECIPE_NOTIFICATION_WORK_NAME)
        workManager.cancelUniqueWork(RECIPE_NOTIFICATION_CHAIN_WORK)
        workManager.cancelAllWorkByTag("recipe_notifications_chain")
        Log.d("WorkManagerRepository", "Recipe notifications cancelled")
    }

    fun getWorkStatus(): LiveData<List<WorkInfo>> {
        return workManager.getWorkInfosForUniqueWorkLiveData(RECIPE_NOTIFICATION_WORK_NAME)
    }

    // For immediate execution
    fun runNotificationCheckNow() {
        Log.d("WorkManagerRepository", "Running immediate notification check...")

        val immediateWork = OneTimeWorkRequestBuilder<RecipeNotificationWorker>()
            .addTag("recipe_notifications_immediate")
            .build()

        workManager.enqueue(immediateWork)
    }
}