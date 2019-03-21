package com.zensar.api.web.rest

import com.zensar.api.config.SPRING_PROFILE_TEST
import com.zensar.api.util.readData
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@ExtendWith(SpringExtension::class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
internal class CategoriesResourceTest {

    lateinit var mockMvc: MockMvc
    lateinit var mockWebServer: MockWebServer

    @BeforeEach
    fun setup(wac: WebApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build()
        mockWebServer = MockWebServer()
        mockWebServer.start(1881)
    }

    @Test
    fun `test mock initialized`() {
        assertNotNull(mockMvc)
    }

    @Test
    fun `test bare products`() {
        mockWebServer.enqueue(MockResponse().setBody(readData("/data/filleddata.json")))

        this.mockMvc.perform(get("/products"))
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].productId").isNotEmpty)
    }

    @Test
    fun `test products with ShowWasNow`() {
        mockWebServer.enqueue(MockResponse().setBody(readData("/data/filleddata.json")))

        this.mockMvc.perform(get("/products?labelType=ShowWasNow"))
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].productId").isNotEmpty)
                .andExpect(jsonPath("$[0].priceLabel").isString)
                .andExpect(jsonPath("$[0].priceLabel").value("Was £149, now £74"))
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()

    }
}