package com.romanhruska.data.mysql

import com.romanhruska.data.model.Message

interface DbManager {

    suspend fun getAllMessages(): List<Message>

    suspend fun insertMessage(message: Message)

}