package rajeev.ranjan.networkmodule

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val ApiKeyPlugin = createClientPlugin("ApiKeyPlugin") {
    onRequest { request, _ ->
        val apiKey = ApiClientUtil.getApiKey()
        if (!apiKey.isNullOrBlank()) {
            request.url.parameters.append("apiKey", apiKey)
        }
    }
}

interface IApiServiceClientProvider {
    fun provideBaseUrl(): String
    fun provideEngineFactory(): HttpClientEngine

    val httpClient: HttpClient
        get() = HttpClient(provideEngineFactory()) {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.v(message)
                    }
                }.also {
                    Napier.base(DebugAntilog())
                }
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                        coerceInputValues = true
                    }
                )
            }
            install(ApiKeyPlugin)
            expectSuccess = true
            defaultRequest {
                contentType(ContentType.Application.Json)
                url(provideBaseUrl())
            }
        }
}
