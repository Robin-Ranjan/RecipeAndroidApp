package rajeev.ranjan.recipeapp.recopiDetails.di

import org.koin.dsl.module
import rajeev.ranjan.recipeapp.recopiDetails.repository.RecipeDetailsRepository

val receiptDetails = module {
    single { RecipeDetailsRepository(get(), get(), get(), get()) }
}