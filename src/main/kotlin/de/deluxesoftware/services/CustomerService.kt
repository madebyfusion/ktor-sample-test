package de.deluxesoftware.services

import de.deluxesoftware.db.DbSettings.db
import de.deluxesoftware.models.Customer
import de.deluxesoftware.models.CustomerEntity
import de.deluxesoftware.models.Customers
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update

class CustomerService {
    suspend fun getAllCustomers() = newSuspendedTransaction(Dispatchers.IO, db = db) {
        CustomerEntity.all().map(CustomerEntity::toCustomer)
    }

    suspend fun getCustomer(id: Int): Customer? = newSuspendedTransaction(Dispatchers.IO, db = db) {
        CustomerEntity.findById(id)?.toCustomer()
    }

    suspend fun addCustomer(customer: Customer): Customer = newSuspendedTransaction(Dispatchers.IO, db = db) {
        CustomerEntity.new {
            firstName = customer.firstName
            lastName = customer.lastName
            email = customer.email
        }.toCustomer()
    }

    suspend fun deleteCustomer(customerId: Int) = newSuspendedTransaction(Dispatchers.IO, db = db) {
        CustomerEntity[customerId].delete()
    }

    suspend fun updateCustomer(customer: Customer): Customer = newSuspendedTransaction(Dispatchers.IO, db = db) {
        val id = customer.id
        if (id == null) {
            addCustomer(customer)
        } else {
            Customers.update({ Customers.id eq id }) {
                it[firstName] = customer.firstName
                it[lastName] = customer.lastName
                it[email] = customer.email
            }
            customer
        }
    }
}
