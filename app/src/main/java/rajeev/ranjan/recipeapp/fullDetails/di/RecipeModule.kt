package rajeev.ranjan.recipeapp.fullDetails.di


import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import rajeev.ranjan.recipeapp.fullDetails.viewModel.RecipeDetailsViewModel

val recipeModule = module {
    viewModelOf(::RecipeDetailsViewModel)
}