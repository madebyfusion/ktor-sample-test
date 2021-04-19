package de.deluxesoftware

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import de.deluxesoftware.auth.JWTPrincipal
import de.deluxesoftware.auth.Login
import de.deluxesoftware.db.DbSettings
import de.deluxesoftware.models.Customers
import de.deluxesoftware.models.Servers
import de.deluxesoftware.routes.registerCustomerRoutes
import de.deluxesoftware.services.bindServices
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Locations
import io.ktor.response.respond
import io.ktor.serialization.json
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.ktor.di
import java.util.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {

    DbSettings.db
    createTables()

    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
            }
        )
    }

    install(CallLogging)
    install(StatusPages) {
        exception<EntityNotFoundException> {
            call.respond(HttpStatusCode.NotFound, it.message.toString())
        }
    }
    install(Locations)

    di {
        bindServices()
    }

    registerCustomerRoutes()

    try {
        val jwtIssuer = environment.config.property("jwt.domain").getString()
        val jwtAudience = environment.config.property("jwt.audience").getString()
        val jwtRealm = environment.config.property("jwt.realm").getString()
        val expirationTime = environment.config.property("jwt.validity").getString().toInt()
        val jwtSecret = environment.config.property("jwt.secret").getString()

        install(Authentication) {
            jwt {
                realm = jwtRealm
                verifier(makeJwtVerifier(jwtIssuer, jwtAudience))
                validate { credential ->
                    if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
                }
            }
        }

        fun obtainExpirationDate() = Date(System.currentTimeMillis() + expirationTime)
        fun generateToken(login: Login): String = JWT.create()
            .withSubject("Authentication")
            .withIssuer(jwtIssuer)
            .withClaim("id", login.id)
            .withClaim("username", login.username)
            .withClaim("password", login.password)
            .withExpiresAt(obtainExpirationDate())
            .sign(algorithm)
    } catch (e: Exception) {
        // TODO: Auth implementation
    }
}

private val algorithm = Algorithm.HMAC256("secret")
private fun makeJwtVerifier(issuer: String, audience: String): JWTVerifier = JWT
    .require(algorithm)
    .withAudience(audience)
    .withIssuer(issuer)
    .build()

private fun createTables() = transaction {
    addLogger(StdOutSqlLogger)
    SchemaUtils.create(Customers)
    SchemaUtils.create(Servers)
}

/**
 * Messages
 *
 * @constructor Create empty Messages
 */
object Messages {
    const val INVALID_ID = "Missing or malformed id"
}
