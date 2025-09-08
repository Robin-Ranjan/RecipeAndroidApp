package rajeev.ranjan.recipeapp.search.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import rajeev.ranjan.recipeapp.search.viewModel.SearchViewmodel

val searchModule = module {
    factoryOf(::SearchViewmodel)
}