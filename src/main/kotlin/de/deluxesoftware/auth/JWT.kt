package de.deluxesoftware.auth

import com.auth0.jwt.interfaces.Payload
import io.ktor.auth.Credential
import io.ktor.auth.Principal

class JWTCredential(val payload: Payload) : Credential
class JWTPrincipal(val payload: Payload) : Principal
