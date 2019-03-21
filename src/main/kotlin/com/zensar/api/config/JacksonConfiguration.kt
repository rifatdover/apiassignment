package com.zensar.api.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


/**
 * Configuration class for Jackson Parser.
 */
@Configuration
class JacksonConfiguration {

    /**
     * Support for Java date and time API.
     * @return the corresponding Jackson module.
     */
    @Bean
    fun javaTimeModule(): JavaTimeModule {
        return JavaTimeModule()
    }

    @Bean
    fun jdk8TimeModule(): Jdk8Module {
        return Jdk8Module()
    }

    /**
     * Error free deserialization module.
     * This helps if data has wrong fields like price.now as object
     * parses successfully
     *
     */
    @Bean
    fun deserializerModule(deserializationProblemHandler: DeserializationProblemHandler): SimpleModule {
        return object : SimpleModule() {
            override fun setupModule(context: SetupContext?) {
                context?.addDeserializationProblemHandler(deserializationProblemHandler)
                super.setupModule(context)
            }
        }
    }


    @Bean
    fun problemHandler(): DeserializationProblemHandler {
        return object : DeserializationProblemHandler() {
            override fun handleUnexpectedToken(ctxt: DeserializationContext?,
                                               targetType: Class<*>?,
                                               t: JsonToken?,
                                               p: JsonParser?,
                                               failureMsg: String?): Any? {
                return null
            }
        }
    }
}