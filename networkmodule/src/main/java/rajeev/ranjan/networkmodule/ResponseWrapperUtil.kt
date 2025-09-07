package rajeev.ranjan.networkmodule

object ResponseWrapperUtil {
    suspend fun <T> getResponseSafely(getResponse: suspend () -> T): ResponseWrapper<T> {
        return try {
            ResponseWrapper.Success(getResponse())
        } catch (e: Exception) {
            ResponseWrapper.Error(e)
        }
    }
}