package rajeev.ranjan

import android.app.Application
import androidx.work.Configuration
import org.koin.android.ext.android.get
import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import rajeev.ranjan.recipeapp.core.di.initKoin
import rajeev.ranjan.recipeapp.notification.WorkManagerRepository

class RecipeApplication : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        initKoin(context = applicationContext, baseUrl = "https://api.spoonacular.com/")

        val workManagerRepository = get<WorkManagerRepository>()
        workManagerRepository.scheduleRecipeNotificationsEvery10Seconds()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(KoinWorkerFactory())
            .build()
}

