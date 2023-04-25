package com.romanhruska.data

import com.romanhruska.data.model.Message

// Abstraction, implementation can be for any kind of DB
interface MessageDataSource {

    suspend fun getAllMessages(): List<Message>

    suspend fun insertMessage(message: Message)
}