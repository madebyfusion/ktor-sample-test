package de.deluxesoftware

import de.deluxesoftware.routes.registerCustomerRoutes
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun `get all customers with ok 200`() {
        withTestApplication({ registerCustomerRoutes() }) {
            handleRequest(HttpMethod.Get, "/customer").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}
