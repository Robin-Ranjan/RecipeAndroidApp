package rajeev.ranjan.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import rajeev.ranjan.networkmodule.ApiClientUtil
import rajeev.ranjan.networkmodule.ResponseWrapper
import rajeev.ranjan.recipeapp.core.navigation.App
import rajeev.ranjan.recipeapp.homeScreen.viewModel.HomeScreenViewModel
import rajeev.ranjan.recipeapp.ui.theme.RecipeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ApiClientUtil.setApiKey("07ebc6319f984bf9a580d1ed158bf685")
        ApiClientUtil.setApiKey("a60f0f6a8fad4f18ad531a79bc12bda7")
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = koinViewModel(),
) {

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    Text(
        text = "Hello $name! ${uiState.recipes}",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RecipeAppTheme {
        Greeting("Android")
    }
}