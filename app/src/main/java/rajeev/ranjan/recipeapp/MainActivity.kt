package rajeev.ranjan.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import rajeev.ranjan.networkmodule.ApiClientUtil
import rajeev.ranjan.recipeapp.core.navigation.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApiClientUtil.setApiKey("07ebc6319f984bf9a580d1ed158bf685")
//        ApiClientUtil.setApiKey("a60f0f6a8fad4f18ad531a79bc12bda7")
        enableEdgeToEdge()
        setContent {
            App(intent)
        }
    }
}