package rajeev.ranjan.recipeapp.core.di

import android.content.Context
import androidx.annotation.RequiresOptIn
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.KoinAppDeclaration

fun initKoin(context: Context, baseUrl: String, appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        androidLogger(Level.ERROR)
        appDeclaration()
        androidContext(context)
        modules(featuredModule(context, baseUrl = baseUrl))
    }
}