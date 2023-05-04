package com.romanhruska.data.mysql

import com.romanhruska.data.model.Message
import org.ktorm.database.Database
import org.ktorm.database.asIterable
import org.ktorm.dsl.from
import org.ktorm.dsl.insert
import org.ktorm.dsl.select

class DbManagerImpl(
    private val database: Database
): DbManager {
    override suspend fun getAllMessages(): List<Message> {
        val messages = database.from(MessagesTable).select()
        return messages.rowSet.asIterable().map { row ->
            Message(
                row[MessagesTable.text] ?: "",
                row[MessagesTable.username] ?: "",
                row[MessagesTable.timestamp] ?: 0,
                row[MessagesTable.id] ?: "",
            )
        }
    }

    override suspend fun insertMessage(message: Message) {
        database.insert(MessagesTable) {
            set(it.text, message.text)
            set(it.username, message.username)
            set(it.timestamp, message.timestamp)
            set(it.id, message.id)
        }
        // returns Int (as how many rows added)
    }
}