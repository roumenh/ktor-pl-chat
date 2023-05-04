package com.romanhruska.data.mysql

import com.romanhruska.data.model.Message
import com.romanhruska.plugins.DbHelper
import org.ktorm.database.Database
import org.ktorm.database.asIterable
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import org.ktorm.dsl.toLong

class DbManagerImpl(
    private val database: Database
): DbManager {
    override suspend fun getAllMessages(): List<Message> {
        val messages = database.from(MessagesTable).select()
        return messages.rowSet.asIterable().map { row ->
            Message(
                row[MessagesTable.text] ?: "",
                row[MessagesTable.username] ?: "",
                row[MessagesTable.timestamp]?.toLong() ?: 0,
                row[MessagesTable.id] ?: "",
            )
        }
    }

    override suspend fun insertMessage(message: Message) {
        TODO("Not yet implemented")
    }
}