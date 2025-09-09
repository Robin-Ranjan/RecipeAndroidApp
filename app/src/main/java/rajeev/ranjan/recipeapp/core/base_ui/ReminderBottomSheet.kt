package rajeev.ranjan.recipeapp.core.base_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import rajeev.ranjan.recipeapp.ui.theme.AppColor
import rajeev.ranjan.recipeapp.ui.theme.AppTheme
import rajeev.ranjan.recipeapp.ui.theme.Gap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderBottomSheet(
    onDismiss: () -> Unit,
    onReminderSet: (ReminderTimes) -> Unit
) {
    AppBottomSheet(
        onDismiss = onDismiss
    ) { state, scope ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            // Title
            Text(
                text = "Set a Reminder",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = AppTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.W700,
                    color = AppColor.PRIMARY_BLACK,
                    fontSize = 24.sp,
                    lineHeight = 24.sp
                )
            )

            Gap(height = 4.dp)

            // Subtitle
            Text(
                text = "You will be reminded in",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = AppTheme.typography.bodySmall.copy(
                    color = AppColor.SECONDARY,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.W400
                )
            )

            Gap(height = 16.dp)

            HorizontalDivider(thickness = 1.dp, color = AppColor.NEUTRAL_LIGHT)

            Gap(height = 16.dp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                ReminderTimes.entries.forEach { time ->
                    Button(
                        onClick = {
                            scope.launch {
                                onReminderSet(time)
                                state.hide()
                                onDismiss()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = AppColor.ORANGE
                        ),
                        border = BorderStroke(
                            1.dp,
                            AppColor.NEUTRAL_LIGHT,
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = time.label,
                            style = AppTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.W700,
                                fontSize = 16.sp,
                                lineHeight = 20.sp,
                                color = AppColor.ORANGE
                            )
                        )
                    }
                }
            }
            Gap(height = 16.dp)
        }
    }
}

enum class ReminderTimes(private val hours: Double, val label: String) {
    THIRTY_MINUTES(0.5, "30m"),
    ONE_HOUR_THIRTY_MINUTES(1.5, "1h 30m"),
    TWO_HOURS(2.0, "2h");

    val millis: Long get() = (hours * 60 * 60 * 1000).toLong()
}

