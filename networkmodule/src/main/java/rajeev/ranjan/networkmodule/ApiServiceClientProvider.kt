package rajeev.ranjan.networkmodule

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

class ApiServiceClientProvider(
    private val context: Context, private val baseUrl: String
) : IApiServiceClientProvider {
    override fun provideBaseUrl(): String {
        return baseUrl
    }

    override fun provideEngineFactory(): HttpClientEngine {
        return OkHttp.create {
            addNetworkInterceptor(ChuckerInterceptor.Builder(context).build())
        }
    }
}
