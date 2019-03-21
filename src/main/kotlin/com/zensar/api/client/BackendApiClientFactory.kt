package com.zensar.api.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

val logger: Logger = LoggerFactory.getLogger(BackendApiClientFactory::class.java)

/**
 * Factory class for creating api
 */
class BackendApiClientFactory(objectMapper: ObjectMapper, baseUrl: String, key: String) {

    private val client = OkHttpClient().newBuilder()
            .addNetworkInterceptor(HttpLoggingInterceptor().apply {
                level = if (logger.isDebugEnabled) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            })
            .addInterceptor(AuthenticationKeyInterceptor(key))
            .addInterceptor(FailedDataFixerInterceptor())
            .build()

    private val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    val api = retrofit.create(BackendApi::class.java)
}

/**
 * API key interceptor
 *
 */
internal class AuthenticationKeyInterceptor(val key: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("key", key)
                .build()

        val request = chain
                .request()
                .newBuilder()
                .url(url).build()

        return chain.proceed(request)
    }

}

/**
 * FailedDataFixerInterceptor provides every
 * time successful response for backend api calls
 *
 * Created this for note:
 * Trapping errors with the data provided by the API:
 * If there is invalid data on the input file then write out an empty string or
 * assume a value of zero rather than throwing exceptions or creating complex error logging.
 *
 * Sometimes api returns error!!!
 *
 */
internal class FailedDataFixerInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request = chain.request()
            logger.debug("""Sending request:
                | ${request.url()},
                | ${chain.connection()},
                | ${request.headers()}""".trimMargin()
            )

            val response = chain.proceed(request)

            logger.debug("""Received response for:
                   | ${response.request().url()}
                   | Status: ${response.isSuccessful}
                   | """.trimMargin()
            )

            if (response.isSuccessful) {
                return response
            }
        } catch (e: Exception) {
            logger.error(e.message, e)
        }
        val url = chain.request().url()
        return ClientUtil.createDummyResponse("${url.scheme()}://${url.host()}")
    }

}

/**
 * Backend Api interface definition for retrofit
 */
interface BackendApi {
    @GET("v1/categories/{path}/products")
    fun getProducts(@Path("path") path: String? = "600001506"): Deferred<BackendProducts>

}