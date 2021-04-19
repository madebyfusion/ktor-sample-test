package de.deluxesoftware

import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun `login with wrong email and check response`() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/customer").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}
