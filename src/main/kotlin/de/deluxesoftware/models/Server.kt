package de.deluxesoftware.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Server(val id: Int? = null, val host: String, val port: Int)

object Servers : IntIdTable() {
    val host = varchar("host", 16)
    val port = integer("port")
    val customer = reference("customer", Customers)
}

class ServerEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ServerEntity>(Servers)

    var host by Servers.host
    var port by Servers.port
    var customer by CustomerEntity referencedOn Servers.customer

    fun toServer() = Server(id.value, host, port)
}
