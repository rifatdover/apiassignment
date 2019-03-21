package com.zensar.api

import com.zensar.api.config.ApplicationProperties
import com.zensar.api.config.SPRING_PROFILE_DEVELOPMENT
import com.zensar.api.config.SPRING_PROFILE_PRODUCTION
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.core.env.Environment
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.*
import javax.annotation.PostConstruct

const val SPRING_PROFILE_DEFAULT = "spring.profiles.default"
const val protocol = "http"


@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties::class)
class ApiApplication(val env: Environment) {

    companion object {
        val log = LoggerFactory.getLogger(ApiApplication::class.java)
    }

    /**
     * Initializes api.
     *
     * Spring profiles can be configured with a program argument --spring.profiles.active=your-active-profile
     */
    @PostConstruct
    fun initApplication() {
        val activeProfiles = Arrays.asList<String>(*env.activeProfiles)
        if (activeProfiles.contains(SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run " + "with both the 'dev' and 'prod' profiles at the same time.")
        }
    }
}

fun main(args: Array<String>) {
    val app = SpringApplication(ApiApplication::class.java)
    addDefaultProfile(app)
    logApplicationStartup(app.run(*args).environment)
}

/**
 *  The default profile to use when no other profiles are defined
 *  This cannot be set in the 'application.yml' file.
 */
fun addDefaultProfile(app: SpringApplication) {
    val defProperties = mapOf(SPRING_PROFILE_DEFAULT to SPRING_PROFILE_DEVELOPMENT)
    app.setDefaultProperties(defProperties)
}

private fun logApplicationStartup(env: Environment) {
    val serverPort = env.getProperty("server.port")
    var contextPath = env.getProperty("server.servlet.context-path")
    if (contextPath.isNullOrEmpty()) {
        contextPath = "/"
    }
    var hostAddress = "localhost"
    try {
        hostAddress = InetAddress.getLocalHost().hostAddress
    } catch (e: UnknownHostException) {
        ApiApplication.log.warn("The host name could not be determined, using `localhost` as fallback")
    }

    ApiApplication.log.info("\n----------------------------------------------------------\n\t" +
            "Application '{}' is running! Access URLs:\n\t" +
            "Local: \t\t{}://localhost:{}{}\n\t" +
            "External: \t{}://{}:{}{}\n\t" +
            "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.activeProfiles)
}