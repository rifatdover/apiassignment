package com.zensar.api.client

import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import okhttp3.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import java.nio.file.Files

internal class FailedDataFixerInterceptorTest {
    lateinit var interceptor: FailedDataFixerInterceptor

    @RelaxedMockK
    lateinit var chain: Interceptor.Chain

    @RelaxedMockK
    lateinit var response: Response

    @RelaxedMockK
    lateinit var request: Request


    @BeforeEach
    internal fun setUpEach() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        interceptor = FailedDataFixerInterceptor()
    }

    @Test
    fun `test succesful intercept`() {

        every { chain.request() }.returns(request)

        every { chain.proceed(request) }.returns(response)

        every { response.isSuccessful }.returns(true)

        every { response.body() }.returns(createSuccessFullResponseBody())

        val returnedResponse = interceptor.intercept(chain)

        verify { chain.request() }

        verify { chain.proceed(request) }

        verify { chain.connection() }

        assertEquals(response, returnedResponse)

        confirmVerified(chain)

    }

    @Test
    fun `test failed data`() {
        every { chain.request() }.returns(request)

        every { request.url() }.returns(HttpUrl.get("http://localhost:1881"))

        every { response.isSuccessful }.returns(false)

        every { chain.proceed(request) }.returns(response)

        val returnedResponse = interceptor.intercept(chain)

        val dummyResponse = mockkClass(Response::class, relaxed = true)

        every { dummyResponse.body() }.returns(createDummyResponseBody())

        every { dummyResponse.isSuccessful }.returns(true)

        assertEquals(returnedResponse.isSuccessful, true)

        assertEquals(returnedResponse.body()?.string(), dummyResponse.body()?.string())

    }

    @AfterEach
    internal fun tearDown() {
        unmockkAll()
    }

    private fun createDummyResponseBody(): ResponseBody {
        return ResponseBody
                .create(MediaType
                        .parse("application/json"),
                        readDataFromClassPath("data/empty.json")
                )
    }

    private fun createSuccessFullResponseBody(): ResponseBody {
        return ResponseBody
                .create(MediaType
                        .parse("application/json"),
                        readDataFromClassPath("data/filleddata.json")
                )
    }

    private fun readDataFromClassPath(path: String): ByteArray {
        val resource = ClassPathResource(path).file
        return Files.readAllBytes(resource.toPath())
    }
}