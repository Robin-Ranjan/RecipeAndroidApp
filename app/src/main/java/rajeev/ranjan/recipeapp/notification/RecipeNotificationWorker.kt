package rajeev.ranjan.recipeapp.notification

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import org.koin.java.KoinJavaComponent.inject
import rajeev.ranjan.recipeapp.favorite.repository.FavoriteRecipeRepository
import java.util.concurrent.TimeUnit

class RecipeNotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    // Get dependencies from Koin
    private val favoriteRecipeRepository: FavoriteRecipeRepository by inject(
        FavoriteRecipeRepository::class.java
    )
    private val notificationHelper: NotificationHelper by inject(NotificationHelper::class.java)

    override suspend fun doWork(): Result {
        return try {
            Log.d("RecipeNotificationWorker", "Starting recipe notification check...")

            // Get all recipes that need notification
            val pendingNotifications = favoriteRecipeRepository.getPendingNotifications()

            Log.d(
                "RecipeNotificationWorker",
                "Found ${pendingNotifications.size} recipes ready for notification"
            )

            // Send notification for each eligible recipe
            pendingNotifications.forEach { recipe ->
                Log.d("RecipeNotificationWorker", "Sending notification for: ${recipe.title}")

                // Send notification
                notificationHelper.sendRecipeNotification(recipe)

                // Mark as notified to prevent duplicate notifications
                favoriteRecipeRepository.markAsNotified(recipe.recipeId)

                Log.d(
                    "RecipeNotificationWorker",
                    "Notification sent and marked for: ${recipe.title}"
                )
            }

            // Schedule the next work in the chain (for 10-second intervals)
            if (tags.contains("recipe_notifications_chain")) {
                scheduleNextWork()
            }

            Log.d("RecipeNotificationWorker", "Notification check completed successfully")
            Result.success()

        } catch (e: Exception) {
            Log.e("RecipeNotificationWorker", "Error in recipe notification worker", e)

            // Even on error, schedule next work to keep the chain going
            if (tags.contains("recipe_notifications_chain")) {
                scheduleNextWork()
            }

            Result.failure() // Don't retry automatically, let the chain handle it
        }
    }

    private fun scheduleNextWork() {
        try {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresBatteryNotLow(false)
                .build()

            // Use a unique name with timestamp to avoid conflicts
            val uniqueWorkName = "recipe_chain_${System.currentTimeMillis()}"

            val nextWorkRequest = OneTimeWorkRequestBuilder<RecipeNotificationWorker>()
                .setConstraints(constraints)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .addTag("recipe_notifications_chain")
                .build()

            WorkManager.getInstance(applicationContext)
                .enqueueUniqueWork(
                    uniqueWorkName,
                    ExistingWorkPolicy.KEEP, // Changed from REPLACE to KEEP
                    nextWorkRequest
                )

            Log.d("RecipeNotificationWorker", "Scheduled next work: $uniqueWorkName")
        } catch (e: Exception) {
            Log.e("RecipeNotificationWorker", "Failed to schedule next work", e)
        }
    }
}