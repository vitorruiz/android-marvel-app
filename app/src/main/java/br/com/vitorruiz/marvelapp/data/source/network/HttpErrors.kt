package br.com.vitorruiz.marvelapp.data.source.network

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class NetworkError(val code: Int, val msg: String, val body: HttpErrorBody?) :
    Throwable("$code: $msg")

class BadRequest(body: HttpErrorBody?) : NetworkError(400, "Bad Request", body)
class Unauthorized : NetworkError(401, "Unauthorized", null)
class Forbidden(body: HttpErrorBody?) : NetworkError(403, "Forbidden", body)
class NotFound(body: HttpErrorBody?) : NetworkError(404, "Not Found", body)
class MethodNotAllowed(body: HttpErrorBody?) : NetworkError(405, "Method Not Allowed", body)
class InternalServerError(body: HttpErrorBody?) :
    NetworkError(500, "Internal Server NetworkError", body)

class BadGateway(body: HttpErrorBody?) : NetworkError(502, "Bad Gateway", body)
class ServiceUnavailable : NetworkError(503, "Service Unavailable", null)

object HttpErrorHandler {
    fun resolveError(
        error: Throwable,
        onFail: (HttpErrorBody) -> Unit,
        onError: (RestError) -> Unit
    ) {
        try {
            if (error is NetworkError) {
                if (error.body != null) {
                    onFail(error.body)
                } else {
                    onFail(
                        HttpErrorBody(
                            System.currentTimeMillis(),
                            error.code,
                            error.msg,
                            error.msg
                        )
                    )
                }
            } else if (error is UnknownHostException || error is SocketTimeoutException || error is ConnectException) {
                onError(RestError(error, true))
            } else {
                onError(RestError(error))
            }
        } catch (e: Exception) {
            onError(RestError((e)))
        }
    }
}

class HttpErrorBody(val timestamp: Long, val status: Int, val error: String, val message: String)
class RestError(val exception: Throwable, val isConnectionError: Boolean = false)