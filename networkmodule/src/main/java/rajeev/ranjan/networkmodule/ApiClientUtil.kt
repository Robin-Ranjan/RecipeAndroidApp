package rajeev.ranjan.networkmodule

object ApiClientUtil {
    private var apiKey: String? = null

    fun setApiKey(key: String?) {
        apiKey = key
    }

    fun getApiKey(): String? = apiKey

    fun clearApiKey() {
        apiKey = null
    }
}

