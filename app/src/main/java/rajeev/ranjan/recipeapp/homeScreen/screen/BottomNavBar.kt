package rajeev.ranjan.recipeapp.homeScreen.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import rajeev.ranjan.recipeapp.R
import rajeev.ranjan.recipeapp.ui.theme.AppColor
import rajeev.ranjan.recipeapp.ui.theme.AppTheme

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Fav
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface(
        color = AppColor.WHITE,
        shadowElevation = 8.dp,
        border = BorderStroke(width = 1.dp, color = AppColor.GREY_2)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route
                val iconTint = if (selected) AppColor.ORANGE else AppColor.SECONDARY
                val textColor = if (selected) AppColor.ORANGE else AppColor.SECONDARY

                Column(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = item.title,
                        tint = iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = item.title,
                        style = AppTheme.typography.bodySmall.copy(
                            color = textColor,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.W500
                        )
                    )
                }
            }
        }
    }
}

sealed class BottomNavItem(
    val title: String,
    val icon: Int,
    val route: String,
) {
    data object Home : BottomNavItem("Home", R.drawable.home_icon, "home")
    data object Fav : BottomNavItem("Favorite", R.drawable.fav_icon, "favorite")
}