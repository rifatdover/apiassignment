package com.zensar.api.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * Properties specific to application.
 *
 *
 * Properties are configured in the application.yml file.
 *
 */
@Configuration
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
class ApplicationProperties {
    lateinit var appBackend: String
    lateinit var key: String
}