package rajeev.ranjan.recipeapp.core.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(context: Context, baseUrl: String, appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        androidContext(context)
        modules(featuredModule(context, baseUrl = baseUrl))
    }
}