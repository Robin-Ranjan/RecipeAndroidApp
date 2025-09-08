package rajeev.ranjan.recipeapp.core.di

import android.content.Context
import org.koin.core.module.Module
import org.koin.dsl.module
import rajeev.ranjan.networkmodule.ApiServiceClientProvider
import rajeev.ranjan.networkmodule.IApiServiceClientProvider
import rajeev.ranjan.recipeapp.favorite.di.favRecipe
import rajeev.ranjan.recipeapp.homeScreen.di.homeModule
import rajeev.ranjan.recipeapp.notification.notificationModule
import rajeev.ranjan.recipeapp.recopiDetails.di.receiptDetails
import rajeev.ranjan.recipeapp.search.di.searchModule

private fun serviceClient(context: Context, baseUrl: String) = module {
    single<IApiServiceClientProvider> { ApiServiceClientProvider(context, baseUrl) }
}

internal fun featuredModule(context: Context, baseUrl: String): List<Module> = listOf(
    serviceClient(context, baseUrl),
    homeModule,
    searchModule,
    favRecipe,
    notificationModule,
    receiptDetails
)

