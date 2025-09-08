package rajeev.ranjan.recipeapp.core.base_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import rajeev.ranjan.recipeapp.R
import rajeev.ranjan.recipeapp.ui.theme.AppColor


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppBottomSheet(
    onDismiss: () -> Unit,
    type: DragHandleType = DragHandleType.CROSS,
    content: @Composable (state: SheetState, scope: CoroutineScope) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = Modifier.navigationBarsPadding(),
        sheetState = sheetState,
        onDismissRequest = {
            scope.launch { sheetState.hide() }
            onDismiss()
        },
        containerColor = Color.Transparent,
        dragHandle = {}
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            BottomSheetDragHandle(onDismiss = {
                scope.launch {
                    sheetState.hide()
                    onDismiss()
                }
            }, type = type)

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = AppColor.WHITE
            ) {
                content(sheetState, scope)
            }
        }
    }
}

enum class DragHandleType {
    CROSS,
    BACK
}

@Composable
fun BottomSheetDragHandle(
    type: DragHandleType = DragHandleType.CROSS,
    onDismiss: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(vertical = 16.dp),
        contentAlignment = when (type) {
            DragHandleType.CROSS -> Alignment.Center
            DragHandleType.BACK -> Alignment.CenterStart
        }
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.7f))
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            when (type) {
                DragHandleType.CROSS -> {
                    Icon(
                        painter = painterResource(R.drawable.cross_icon),
                        contentDescription = "Close",
                        tint = AppColor.WHITE,
                        modifier = Modifier.size(16.dp)
                    )
                }

                DragHandleType.BACK -> {
                    Icon(
                        painter = painterResource(R.drawable.back_icon),
                        contentDescription = "Back",
                        tint = AppColor.WHITE,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}