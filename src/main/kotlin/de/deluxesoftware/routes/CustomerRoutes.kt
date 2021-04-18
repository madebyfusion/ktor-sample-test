package de.deluxesoftware.routes

import de.deluxesoftware.Messages.INVALID_ID
import de.deluxesoftware.models.Customer
import de.deluxesoftware.services.CustomerService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Route.customerRouting() {

    val customerService by closestDI().instance<CustomerService>()

    route("/customer") {
        get {
            call.respond(customerService.getAllCustomers())
        }

        route("{id}") {
            get {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respondText(
                    INVALID_ID,
                    status = HttpStatusCode.BadRequest
                )
                val customer =
                    customerService.getCustomer(id) ?: return@get call.respondText(
                        "No customer with id $id",
                        status = HttpStatusCode.NotFound
                    )
                call.respond(customer)
            }

            get("/servers") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respondText(
                    INVALID_ID,
                    status = HttpStatusCode.BadRequest
                )
                val customer =
                    customerService.getCustomer(id) ?: return@get call.respondText(
                        "No customer with id $id",
                        status = HttpStatusCode.NotFound
                    )
                call.respond(customer.servers)
            }
        }

        post {
            val customerRequest = call.receive<Customer>()
            val customer = customerService.addCustomer(customerRequest)
            call.respond(HttpStatusCode.Accepted, customer)
        }

        put {
            val customer = call.receive<Customer>()
            val updated = customerService.updateCustomer(customer)
            call.respond(HttpStatusCode.OK, updated)
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respondText(
                INVALID_ID,
                status = HttpStatusCode.BadRequest
            )
            customerService.deleteCustomer(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Application.registerCustomerRoutes() {
    routing {
        customerRouting()
    }
}
