package com.zensar.api.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler
import com.zensar.api.config.ApplicationProperties
import com.zensar.api.config.SPRING_PROFILE_TEST
import com.zensar.api.util.readData
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
internal class BackendApiClientFactoryTest {

    @Autowired
    lateinit var deserializationProblemHandler: DeserializationProblemHandler

    @Autowired
    lateinit var applicationProperties: ApplicationProperties

    @Test
    fun `test client`() {
        val backendApiClientFactory = BackendApiClientFactory(
                getObjectMapper(),
                applicationProperties.appBackend,
                applicationProperties.key
        )
        assertNotNull(backendApiClientFactory)
        assertNotNull(backendApiClientFactory.api)
    }

    @Test
    fun `test filled mock`() {
        val mockWebServer = MockWebServer()
        mockWebServer.enqueue(MockResponse()
                .setBody(readData("/data/filleddata.json")))
        val backendApiClientFactory = BackendApiClientFactory(getObjectMapper()
                , "http://${mockWebServer.hostName}:${mockWebServer.port}", "")

        assertNotNull(backendApiClientFactory.api)

        runBlocking {

            val response = backendApiClientFactory.api.getProducts().await()
            assertNotNull(response)
            assertNotNull(response.products)

            response.products?.isNotEmpty()?.let { assertTrue(it) }
        }
        mockWebServer.shutdown()


    }


    @Test
    fun `test error mock`() {
        val mockWebServer = MockWebServer()
        mockWebServer.enqueue(MockResponse()
                .setResponseCode(500)
                .setBody("")
        )
        val backendApiClientFactory = BackendApiClientFactory(getObjectMapper()
                , "http://${mockWebServer.hostName}:${mockWebServer.port}", "")

        assertNotNull(backendApiClientFactory.api)

        runBlocking {

            val response = backendApiClientFactory.api.getProducts().await()
            assertNotNull(response)
            assertNotNull(response.products)

            response.products?.isNotEmpty()?.let { assertTrue(it) }
        }
        mockWebServer.shutdown()


    }

    private fun getObjectMapper(): ObjectMapper {
        return ObjectMapper()
                .addHandler(deserializationProblemHandler)
    }
}