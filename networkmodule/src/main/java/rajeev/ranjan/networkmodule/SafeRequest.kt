package rajeev.ranjan.networkmodule

import io.ktor.client.call.body
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.ParametersBuilder

object SafeRequest {

    suspend inline fun <reified T> IApiServiceClientProvider.safeRequest(
        method: HttpMethod,
        rawPath: String,
        pathParams: Map<String, String?> = emptyMap(),
        queryParams: Map<String, String?> = emptyMap(),
        requestBody: Any? = null,
    ): ResponseWrapper<T> {
        return ResponseWrapperUtil.getResponseSafely {
            this@safeRequest.httpClient.request(rawPath) {
                this.method = method
                if (queryParams.isNotEmpty()) {
                    url {
                        queryParams.forEach { (key, value) ->
                            parameters.appendIfNotNull(key, value)
                        }
                    }
                }
                if (pathParams.isNotEmpty()) {
                    rawPath.replacePathParams(pathParams)
                }
                requestBody?.let { setBody(it) }
            }.body()
        }
    }

    fun ParametersBuilder.appendIfNotNull(name: String, value: Any?) {
        if (value != null) {
            this.append(name, value.toString())
        }
    }

    fun String.replacePathParams(params: Map<String, String?>): String {
        var path = this
        params.forEach { (key, value) ->
            if (value != null)
                path = path.replace("{$key}", value)
        }
        return path
    }


    // GET
    suspend inline fun <reified T> IApiServiceClientProvider.safeGet(
        rawPath: String,
        pathParams: Map<String, String?> = emptyMap(),
        queryParams: Map<String, String?> = emptyMap(),
    ): ResponseWrapper<T> = safeRequest(
        method = HttpMethod.Get,
        rawPath = rawPath,
        pathParams = pathParams,
        queryParams = queryParams,
        requestBody = null
    )

    // POST
    suspend inline fun <reified T> IApiServiceClientProvider.safePost(
        rawPath: String,
        requestBody: Any,
        pathParams: Map<String, String?> = emptyMap(),
        queryParams: Map<String, String?> = emptyMap(),
    ): ResponseWrapper<T> = safeRequest(
        method = HttpMethod.Post,
        rawPath = rawPath,
        pathParams = pathParams,
        queryParams = queryParams,
        requestBody = requestBody
    )

    // PUT
    suspend inline fun <reified T> IApiServiceClientProvider.safePut(
        rawPath: String,
        requestBody: Any,
        pathParams: Map<String, String?> = emptyMap(),
        queryParams: Map<String, String?> = emptyMap(),
    ): ResponseWrapper<T> = safeRequest(
        method = HttpMethod.Put,
        rawPath = rawPath,
        pathParams = pathParams,
        queryParams = queryParams,
        requestBody = requestBody
    )

    // DELETE
    suspend inline fun <reified T> IApiServiceClientProvider.safeDelete(
        rawPath: String,
        requestBody: Any,
        pathParams: Map<String, String?> = emptyMap(),
        queryParams: Map<String, String?> = emptyMap(),
    ): ResponseWrapper<T> = safeRequest(
        method = HttpMethod.Delete,
        rawPath = rawPath,
        pathParams = pathParams,
        queryParams = queryParams,
        requestBody = requestBody
    )

}