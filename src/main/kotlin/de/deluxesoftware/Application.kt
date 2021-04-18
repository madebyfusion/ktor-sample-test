package de.deluxesoftware

import de.deluxesoftware.db.DbSettings
import de.deluxesoftware.models.Customers
import de.deluxesoftware.models.Servers
import de.deluxesoftware.routes.registerCustomerRoutes
import de.deluxesoftware.services.bindServices
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.serialization.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.ktor.di

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {

    DbSettings.db
    createTables()

    install(ContentNegotiation) {
        json(Json { prettyPrint = true })
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

    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(Customers)
    }
}

private fun createTables() = transaction {
    SchemaUtils.create(Customers)
    SchemaUtils.create(Servers)
}

object Messages {
    const val INVALID_ID = "Missing or malformed id"
}
