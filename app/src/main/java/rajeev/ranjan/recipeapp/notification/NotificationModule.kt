package rajeev.ranjan.recipeapp.notification

import androidx.work.WorkManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val notificationModule = module {
    single<NotificationHelper> {
        NotificationHelper(androidContext())
    }

    // WorkManager
    single<WorkManager> {
        WorkManager.getInstance(androidContext())
    }

    single<WorkManagerRepository> {
        WorkManagerRepository(get())
    }

}