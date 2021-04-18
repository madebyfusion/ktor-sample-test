package de.deluxesoftware.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

/**
 * Singleton to manage database.
 *
 * Holds config for HikariCP pooling
 * and lazily opened [connection][db]
 */
object DbSettings {
    private val config = HikariConfig().apply {
        jdbcUrl = "jdbc:mysql://127.0.0.1/kotlin"
        driverClassName = "com.mysql.cj.jdbc.Driver"
        username = "root"
        password = ""
        maximumPoolSize = 100
        validate()
    }

    private val dataSource = HikariDataSource(config)
    val db by lazy {
        Database.connect(dataSource)
    }
}
