package rajeev.ranjan.recipeapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rajeev.ranjan.recipeapp.ui.theme.AppColor
import rajeev.ranjan.recipeapp.ui.theme.AppTheme
import rajeev.ranjan.recipeapp.ui.theme.Gap
import rajeev.ranjan.recipeapp.ui.theme.UpdateStatusBarAppearance

@Composable
fun SplashScreen(
    onClick: () -> Unit
) {
    UpdateStatusBarAppearance(AppColor.PRIMARY_BLACK)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        Image(
            painter = painterResource(R.drawable.login_background_image),
            contentDescription = "Login Background",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .align(Alignment.BottomStart)
        ) {
            Text(
                text = "Welcome to",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
            )

            StyledAppName()

            Gap(height = 8.dp)

            Text(
                text = "Please sign in to continue",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            )

            Gap(height = 24.dp)

            Button(
                onClick = { onClick() },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColor.ORANGE
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google",
                    tint = AppColor.WHITE,
                    modifier = Modifier.size(17.dp)
                )

                Gap(width = 8.dp)
                Text(
                    text = "Continue with Google",
                    style = AppTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.W500,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = AppColor.WHITE
                    )
                )
            }
            Gap(height = 20.dp)
        }
    }
}

@Composable
fun StyledAppName() {
    Text(
        buildAnnotatedString {
            withStyle(
                style = AppTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.W700,
                    fontStyle = FontStyle.Italic,
                    fontSize = 48.sp,
                    lineHeight = 56.sp,
                    color = AppColor.WHITE
                ).toSpanStyle()
            ) {
                append("Rec")
            }

            withStyle(
                style = AppTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.W400,
                    fontStyle = FontStyle.Italic,
                    fontSize = 48.sp,
                    lineHeight = 56.sp,
                    color = AppColor.WHITE
                ).toSpanStyle()
            ) {
                append("iii")
            }

            withStyle(
                style = AppTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.W700,
                    fontStyle = FontStyle.Italic,
                    fontSize = 48.sp,
                    lineHeight = 56.sp,
                    color = AppColor.WHITE
                ).toSpanStyle()
            ) {
                append("pe")
            }
        }
    )
}
