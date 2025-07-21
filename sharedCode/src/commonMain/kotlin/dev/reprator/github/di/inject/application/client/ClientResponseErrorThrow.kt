package dev.reprator.github.di.inject.application.client

import dev.reprator.github.util.CustomHttpExceptions
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import io.ktor.http.*

fun pluginClientResponseValidator(httpClientConfig: HttpClientConfig<*>) {

    httpClientConfig.HttpResponseValidator {
        validateResponse { response ->

            if (!response.status.isSuccess()) {
                val failureReason = when (response.status) {
                    HttpStatusCode.Unauthorized -> "Unauthorized request"
                    HttpStatusCode.Forbidden -> "${response.status.value} Missing API key."
                    HttpStatusCode.NotFound -> "Invalid Request"
                    HttpStatusCode.UpgradeRequired -> "Upgrade to VIP"
                    HttpStatusCode.RequestTimeout -> "Network Timeout"
                    in HttpStatusCode.InternalServerError..HttpStatusCode.GatewayTimeout ->
                        "${response.status.value} Server Error"

                    else -> "Network error!"
                }
                Napier.e { failureReason }

                throw mapResponse(response, failureReason)
            }
        }

        handleResponseExceptionWithRequest { exception, _ ->
            val clientException =
                exception as? ClientRequestException ?: return@handleResponseExceptionWithRequest

            val exceptionResponse = clientException.response

            Napier.e { exceptionResponse.toString() }

            throw mapResponse(exceptionResponse)
        }
    }
}


suspend fun mapResponse( response: HttpResponse, failureReason: String ?= null): Exception {
    return CustomHttpExceptions(
        response = response,
        failureReason = failureReason ?: response.bodyAsText(),
        cachedResponseText = response.bodyAsText(),
    )
}