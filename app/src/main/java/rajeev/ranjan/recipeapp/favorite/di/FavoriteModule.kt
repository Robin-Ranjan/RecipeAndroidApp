package rajeev.ranjan.recipeapp.favorite.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import rajeev.ranjan.recipeapp.favorite.repository.FavoriteRecipeRepository
import rajeev.ranjan.recipeapp.favorite.roomDb.FavoriteRecipeDao
import rajeev.ranjan.recipeapp.favorite.roomDb.FavoriteRecipeDatabase
import rajeev.ranjan.recipeapp.favorite.viewModel.FavoriteViewModel

val favRecipe = module {
    single<FavoriteRecipeDatabase> {
        FavoriteRecipeDatabase.getDatabase(androidContext())
    }

    single<FavoriteRecipeDao> {
        get<FavoriteRecipeDatabase>().favoriteRecipeDao()
    }

    single<FavoriteRecipeRepository> {
        FavoriteRecipeRepository(get())
    }

    viewModelOf(::FavoriteViewModel)
}