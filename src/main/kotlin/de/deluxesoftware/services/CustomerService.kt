package de.deluxesoftware.services

import de.deluxesoftware.db.DbSettings.db
import de.deluxesoftware.models.Customer
import de.deluxesoftware.models.CustomerEntity
import de.deluxesoftware.models.Customers
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update

/**
 * Service to perform CRUD actions with [Customer] objects
 *
 * @constructor Creates new customer service
 */
class CustomerService {

    /**
     * Selects all [Customers] from database
     * @return list of [Customer]
     */
    suspend fun getAllCustomers() = newSuspendedTransaction(Dispatchers.IO, db = db) {
        CustomerEntity.all().map(CustomerEntity::toCustomer)
    }

    /**
     * Searches for customer with given [id]
     * @param id of the [Customer]
     * @return the found customer or *null*
     */
    suspend fun getCustomer(id: Int): Customer? = newSuspendedTransaction(Dispatchers.IO, db = db) {
        CustomerEntity.findById(id)?.toCustomer()
    }

    /**
     * Inserts a new [customer] into the database
     * @param customer to insert
     * @return [customer] *with* id
     */
    suspend fun addCustomer(customer: Customer): Customer = newSuspendedTransaction(Dispatchers.IO, db = db) {
        CustomerEntity.new {
            firstName = customer.firstName
            lastName = customer.lastName
            email = customer.email
        }.toCustomer()
    }

    /**
     * Deletes [Customer] with given [customerId]
     * does nothing if the customer *does not* exist
     */
    suspend fun deleteCustomer(customerId: Int) = newSuspendedTransaction(Dispatchers.IO, db = db) {
        CustomerEntity[customerId].delete()
    }

    /**
     * Updates an existing [customer] or [inserts][addCustomer] if the [customer] is new
     * @return same [customer], or in case of insertion, with given id
     */
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
