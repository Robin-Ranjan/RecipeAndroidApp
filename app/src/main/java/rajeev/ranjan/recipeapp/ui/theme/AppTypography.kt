package rajeev.ranjan.recipeapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import rajeev.ranjan.recipeapp.R

@Immutable
class AppTypography {
    private val defaultStyle: TextStyle
        @Composable get() {
            val fontFamily = FontFamily(
                Font(R.font.inter_regular, FontWeight.Normal),
                Font(R.font.inter_medium, FontWeight.Medium),
                Font(R.font.inter_semibold, FontWeight.SemiBold),
                Font(R.font.inter_bold, FontWeight.Bold)
            )
            return TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                color = AppColor.black900
            )
        }

    val h1: TextStyle
        @Composable get() = defaultStyle.copy(
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp,
            lineHeight = 32.sp,

            color = AppColor.black900
        )

    val bodySmall: TextStyle
        @Composable get() = defaultStyle.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 14.sp,
            letterSpacing = 0.sp,
            color = Color(0xFF495362)
        )
}
