package rajeev.ranjan.recipeapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
@Composable
fun UpdateStatusBarAppearance(backgroundColor: Color) {
    val context = LocalContext.current

    LaunchedEffect(backgroundColor) {
        if (context is Activity) {
            StatusBarManager.setStatusBarAppearance(backgroundColor, context)
        }
    }
}

private object StatusBarManager {
    fun setStatusBarAppearance(backgroundColor: Color, activity: Activity) {
        val window = activity.window
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        // Determine if background is light or dark
        val isLightBackground = isColorLight(backgroundColor)

        // Set status bar content to dark when background is light, and vice versa
        windowInsetsController.isAppearanceLightStatusBars = isLightBackground
    }

    /**
     * Determines if a color is considered "light" based on luminance
     */
    private fun isColorLight(color: Color): Boolean {
        // Convert to RGB values (0-255)
        val red = (color.red * 255).toInt()
        val green = (color.green * 255).toInt()
        val blue = (color.blue * 255).toInt()

        // Calculate luminance using the standard formula
        // Values above 0.5 are considered light
        val luminance = (0.299 * red + 0.587 * green + 0.114 * blue) / 255
        return luminance > 0.5
    }
}
