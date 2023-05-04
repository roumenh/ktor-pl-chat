package com.romanhruska.data.mysql

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object MessagesTable: Table<Nothing>("Messages") {

    val id = varchar("id")
    val username = varchar("username")
    val text = varchar("text")
    val timestamp = int("timestamp")
}