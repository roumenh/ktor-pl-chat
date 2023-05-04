package com.romanhruska.plugins

import io.ktor.server.application.*
import org.ktorm.database.Database


    private var dbUrl = ""
    private var dbUser = ""
    private var dbPwd = ""

    fun Application.configureDbVariables() {
        dbUrl = environment.config.propertyOrNull("ktor.db.config.db_url")?.getString() ?: ""
        dbUser = environment.config.propertyOrNull("ktor.db.config.db_user")?.getString() ?: ""
        dbPwd = environment.config.propertyOrNull("ktor.db.config.db_pwd")?.getString() ?: ""
    }

class DbHelper {
    fun database() = Database.connect(
        dbUrl,
        user = dbUser,
        password = dbPwd
    )
}
