package rajeev.ranjan

import android.app.Application
import rajeev.ranjan.recipeapp.core.di.initKoin

class RecipeApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(context = applicationContext, baseUrl = "https://api.spoonacular.com/")
    }
}