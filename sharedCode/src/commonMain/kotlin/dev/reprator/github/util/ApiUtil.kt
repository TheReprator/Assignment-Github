package dev.reprator.github.util

import androidx.compose.runtime.Stable
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.prepareRequest
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.ParametersBuilder
import io.ktor.http.path
import io.ktor.serialization.JsonConvertException
import kotlinx.io.IOException

suspend inline fun <reified T> HttpClient.safeRequest(
    block: HttpRequestBuilder.() -> Unit,
): AppResult<T> = try {
        val response: HttpResponse = this.prepareRequest { block() }.execute()
        val responseData: T = response.body()
        AppSuccess(responseData)
    } catch (e: CustomHttpExceptions) {
        AppError(message= e.message, throwable = e, errorCode = e.response.status.value)
    } catch (e: JsonConvertException) {
        AppError(message = "Failed to parse successful response: ${e.message}", throwable = e)
    } catch (e: HttpRequestTimeoutException) {
        AppError(message = "Request timed out: ${e.message}", throwable = e)
    } catch (e: ConnectTimeoutException) {
        AppError(message = "Connection timed out: ${e.message}", throwable = e)
    } catch (e: SocketTimeoutException) {
        AppError(message = "Socket operation timed out: ${e.message}", throwable = e)
    } catch (e: IOException) {
        AppError(message = "Network error: ${e.message}", throwable = e)
    } catch (e: Exception) {
        AppError(message = "An unexpected error occurred: ${e.message}", throwable = e)
    }


suspend inline fun <reified T> HttpClient.hitApiWithClient(
    endPoint: String,
    methodName: HttpMethod = HttpMethod.Get,
    crossinline changeBlock: ParametersBuilder.() -> Unit = {},
    crossinline block: HttpRequestBuilder.() -> Unit = {}
) = safeRequest<T> {
    url {
        changeBlock(parameters)
        path(endPoint)
        method = methodName
    }
    block(this@safeRequest)
}

@Stable
class CustomHttpExceptions(
    response: HttpResponse,
    failureReason: String?,
    cachedResponseText: String,
) : ResponseException(response, cachedResponseText) {
    override val message: String = "Status: ${response.status}" + " Failure: $failureReason"
}
