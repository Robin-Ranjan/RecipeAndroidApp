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
import kotlinx.coroutines.launch
import rajeev.ranjan.recipeapp.ui.theme.AppColor
import rajeev.ranjan.recipeapp.ui.theme.AppTheme
import rajeev.ranjan.recipeapp.ui.theme.Gap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderBottomSheet(
    onDismiss: () -> Unit,
    onReminderSet: (String) -> Unit
) {
    var selectedTime by remember { mutableStateOf("30m") }

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
                style = AppTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = AppColor.PRIMARY_BLACK
                )
            )

            Gap(height = 4.dp)

            // Subtitle
            Text(
                text = "You will be reminded in",
                style = AppTheme.typography.bodySmall.copy(
                    color = AppColor.GREY_2
                )
            )

            Gap(height = 20.dp)

            // Time selection buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val timeOptions = listOf("30m", "1h 30m", "2h")

                timeOptions.forEach { time ->
                    Button(
                        onClick = {
                            scope.launch {
                                state.hide()
                                onReminderSet(time)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTime == time) AppColor.ORANGE else Color.Transparent,
                            contentColor = if (selectedTime == time) AppColor.WHITE else AppColor.PRIMARY_BLACK
                        ),
                        border = if (selectedTime != time) BorderStroke(
                            1.dp,
                            AppColor.GREY_2
                        ) else null,
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(
                            text = time,
                            style = AppTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }

            Gap(height = 24.dp)
        }
    }
}