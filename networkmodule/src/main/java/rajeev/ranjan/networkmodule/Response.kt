package rajeev.ranjan.networkmodule

sealed class ResponseWrapper<T>(val data: T? = null) {
    class Success<T>(data: T?) : ResponseWrapper<T>(data)
    class Error<T>(val cause: Throwable? = null, data: T? = null) : ResponseWrapper<T>(data)

    val isSuccess: Boolean get() = this is ResponseWrapper.Success<T>
    val isFailure: Boolean get() = this is ResponseWrapper.Error<T>


    inline fun onSuccess(action: (T) -> Unit): ResponseWrapper<T> {
        if (this is ResponseWrapper.Success && data != null) action(data)
        return this
    }

    inline fun onFailure(action: (Throwable) -> Unit): ResponseWrapper<T> {
        if (this is ResponseWrapper.Error && cause != null) action(cause)
        return this
    }
}