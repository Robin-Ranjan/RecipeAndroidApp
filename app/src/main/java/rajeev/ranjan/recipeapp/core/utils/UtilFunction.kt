package rajeev.ranjan.recipeapp.core.utils

import androidx.core.text.HtmlCompat

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

fun parseHtmlToText(html: String?): String {
    if (html.isNullOrBlank()) return ""
    return HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim()
}
