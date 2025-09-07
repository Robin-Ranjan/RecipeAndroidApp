package rajeev.ranjan.recipeapp.homeScreen.di

import android.content.Context
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import rajeev.ranjan.recipeapp.core.RecipeRepository
import rajeev.ranjan.recipeapp.core.RecipeRepositoryImpl
import rajeev.ranjan.recipeapp.core.roomDb.RecipeDatabase
import rajeev.ranjan.recipeapp.homeScreen.api.HomeApi
import rajeev.ranjan.recipeapp.homeScreen.repository.HomeRepository
import rajeev.ranjan.recipeapp.homeScreen.repository.HomeRepositoryImpl
import rajeev.ranjan.recipeapp.homeScreen.viewModel.HomeScreenViewModel

val homeModule = module {
    single { HomeApi(get()) }
    singleOf(::HomeRepositoryImpl).bind<HomeRepository>()
    viewModelOf(::HomeScreenViewModel)

    single { HomeApi(get()) }
    single<RecipeRepository> { RecipeRepositoryImpl(get(), get(), get()) }

    // Add Room database
    single {
        Room.databaseBuilder(
            androidContext(),
            RecipeDatabase::class.java,
            "recipe_database"
        ).build()
    }
    single { get<RecipeDatabase>().recipeDao() }
    single { get<RecipeDatabase>().ingredientDao() }
}