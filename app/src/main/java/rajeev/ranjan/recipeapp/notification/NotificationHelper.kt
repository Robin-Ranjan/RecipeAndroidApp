package rajeev.ranjan.recipeapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import rajeev.ranjan.recipeapp.MainActivity
import rajeev.ranjan.recipeapp.R
import rajeev.ranjan.recipeapp.favorite.model.FavoriteRecipeEntity

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "recipe_notifications"
        const val CHANNEL_NAME = "Recipe Reminders"
        const val CHANNEL_DESCRIPTION = "Notifications for favorite recipe cooking reminders"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = CHANNEL_DESCRIPTION
            enableLights(true)
            lightColor = Color.GREEN
            enableVibration(true)
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun sendRecipeNotification(recipe: FavoriteRecipeEntity) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create deep link URI
        val deepLinkUri = createDeepLinkUri(recipe)

        // Create intent with deep link
        val deepLinkIntent =
            Intent(Intent.ACTION_VIEW, deepLinkUri, context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

        val pendingIntent = PendingIntent.getActivity(
            context,
            recipe.id.toInt(),
            deepLinkIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.home_icon)
            .setContentTitle("🍳 Recipe Reminder")
            .setContentText("Time to cook: ${recipe.title}")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Ready in ${recipe.readyInMinutes} minutes. Tap to search for similar recipes!")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(recipe.id.toInt(), notification)
    }

    private fun createDeepLinkUri(recipe: FavoriteRecipeEntity): Uri {
        return ("recipeapp://search?" +
                "query=${Uri.encode(recipe.title)}&" +
                "recipeId=${recipe.recipeId}&" +
                "fromNotification=true").toUri()
    }
}