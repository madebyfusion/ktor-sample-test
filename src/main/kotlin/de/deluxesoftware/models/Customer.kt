package de.deluxesoftware.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Customer(
    var id: Int? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val servers: List<Server>
)

object Customers : IntIdTable() {
    val firstName = varchar("first_name", 50)
    val lastName = varchar("last_name", 50)
    val email = varchar("email", 50)
}

class CustomerEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CustomerEntity>(Customers)

    var firstName by Customers.firstName
    var lastName by Customers.lastName
    var email by Customers.email

    private val servers by ServerEntity referrersOn Servers.customer

    fun toCustomer() = Customer(id.value, firstName, lastName, email, servers.map { it.toServer() })
}
