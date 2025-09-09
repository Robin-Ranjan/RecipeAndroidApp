package rajeev.ranjan.recipeapp.core.utils

fun Boolean?.orDefault() = this ?: false

fun Double?.orDefault() = this ?: 0.0
fun Int?.orDefault() = this ?: 0

fun String?.orDefault() = this ?: ""

fun String.asName(): String {
    val trimmed = this.trimStart()
    return if (trimmed.isNotEmpty())
        trimmed.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    else
        ""
}