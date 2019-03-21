package com.zensar.api.util

import org.springframework.core.io.ClassPathResource
import java.nio.file.Files

fun readData(path: String): String {
    val resource = ClassPathResource(path).file
    return Files.lines(resource.toPath())
            .reduce { u, t -> u + t }
            .get()
}