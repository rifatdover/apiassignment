package com.zensar.api.client

import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import org.springframework.core.io.ClassPathResource
import java.nio.file.Files

internal class ClientUtilTest {

    @Test
    fun `test dummy response`() {
        val retRes = ClientUtil.createDummyResponse("http://localhost:1234")

        assertEquals(retRes.isSuccessful, true)

        assertEquals(retRes.body()?.string(), createDummyResponseBody().string())

    }

    companion object {
        fun createDummyResponseBody(): ResponseBody {
            return ResponseBody
                    .create(MediaType
                            .parse("application/json"),
                            readDataFromClassPath("data/empty.json")
                    )
        }

        fun readDataFromClassPath(path: String): ByteArray {
            val resource = ClassPathResource(path).file
            return Files.readAllBytes(resource.toPath())
        }
    }
}