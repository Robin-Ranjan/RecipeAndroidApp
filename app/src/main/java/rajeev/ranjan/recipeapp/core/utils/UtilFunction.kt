package rajeev.ranjan.recipeapp.core.utils

fun Boolean?.orDefault() = this ?: false

fun Double?.orDefault() = this ?: 0.0
fun Int?.orDefault() = this ?: 0

fun String?.orDefault() = this ?: ""