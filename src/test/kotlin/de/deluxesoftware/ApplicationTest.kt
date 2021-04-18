package de.deluxesoftware

import de.deluxesoftware.routes.registerCustomerRoutes
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun `login with wrong email and check response`() {
        withTestApplication({ registerCustomerRoutes() }) {
            handleRequest(HttpMethod.Get, "/customer").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}
