package com.zensar.api.service

import com.zensar.api.config.SPRING_PROFILE_TEST
import com.zensar.api.util.readData
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.context.WebApplicationContext

@ExtendWith(SpringExtension::class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
internal class BackendCallServiceTest {

    @Autowired
    lateinit var backendCallService: BackendCallService

    lateinit var mockWebServer: MockWebServer

    @BeforeEach
    fun setup(wac: WebApplicationContext) {
        mockWebServer = MockWebServer()
        mockWebServer.enqueue(MockResponse().setBody(readData("/data/filleddata.json")))
        mockWebServer.start(1881)
    }

    @Test
    fun getBackendAPI() {
        assertNotNull(backendCallService.backendAPI)
    }

    @Test
    fun `test bare products`() {
        runBlocking {
            val products = backendCallService.getProducts(labelType = "")
            assertNotNull(products)
            assertEquals(products?.size, 50)
        }
    }

    @Test
    fun `test products with ShowWasNow`() {
        runBlocking {
            val products = backendCallService.getProducts(labelType = "ShowWasNow")
            assertNotNull(products)
            assertEquals(products?.size, 50)
            assertEquals(products!![0].priceLabel, "Was £149, now £74")
        }
    }

    @Test
    fun `test products with ShowWasThenNow`() {
        runBlocking {
            val products = backendCallService.getProducts(labelType = "ShowWasThenNow")
            assertNotNull(products)
            assertEquals(products?.size, 50)
            assertEquals(products!![0].priceLabel, "Was £149, then £89, now £74")
        }
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()

    }
}