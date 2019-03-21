package com.zensar.api.client

import okhttp3.*
import org.springframework.core.io.ClassPathResource
import java.nio.file.Files

/**
 * Utility Class for creating dummy response in case of error.
 */
class ClientUtil {
    companion object {
        fun createDummyResponse(endpoint: String): Response {
            return Response.Builder()
                    .request(Request.Builder().url(endpoint).build())
                    .protocol(Protocol.HTTP_1_1)
                    .message("Success")
                    .body(ResponseBody
                            .create(MediaType
                                    .parse("application/json"),
                                    readDummyDataFromClassPath()
                            )
                    ).code(200)
                    .build()
        }

        private fun readDummyDataFromClassPath(): ByteArray {
            val resource = ClassPathResource(
                    "data/data.json").file
            return Files.readAllBytes(resource.toPath())
        }
    }

}