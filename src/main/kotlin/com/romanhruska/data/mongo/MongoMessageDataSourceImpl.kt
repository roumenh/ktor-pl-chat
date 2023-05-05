package com.romanhruska.data.mongo

import com.romanhruska.data.model.Message
import com.romanhruska.data.MessageDataSource
import org.litote.kmongo.coroutine.CoroutineDatabase

class MongoMessageDataSourceImpl(
    private val db: CoroutineDatabase
) : MessageDataSource {

    private val messages = db.getCollection<Message>()
    // This will create messages collection, if it does not exist, it will create it once we insert 1st message

    override suspend fun getAllMessages(): List<Message> {
        return messages.find()
            .descendingSort(Message::timestamp)
            .toList()
    }

    override suspend fun insertMessage(message: Message) {
        messages.insertOne(message)
    }
}