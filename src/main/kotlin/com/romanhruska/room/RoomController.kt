package com.romanhruska.room

import com.romanhruska.data.MessageDataSource
import com.romanhruska.data.model.Message
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController (
    private val messageDataSource: MessageDataSource
) {
    private val members = ConcurrentHashMap<String, Member>() // key is username, value is Member

    fun onJoin(
        username: String,
        sessionId: String,
        socket: WebSocketSession
    ) {
        if(members.containsKey(username)) {
            throw MemberAlreadyExistsException()
        }
        members[username] = Member(
            username = username,
            sessionId = sessionId,
            socket = socket
        )
    }

    suspend fun sendMessage(senderUsername: String, message: String) {
        val messageEntity = Message(  // moved out of the foreach lambda
            text = message,
            username = senderUsername,
            timestamp = System.currentTimeMillis()
        )
        messageDataSource.insertMessage(messageEntity)
        members.values.forEach { member ->
            // now we want to send the message to all members in the room...
            val parsedMessage = Json.encodeToString(messageEntity)  // import kotlinx.serialization.encodeToString
            member.socket.send(Frame.Text(parsedMessage))
        }
    }

    suspend fun getAllMessages(): List<Message> {
        return messageDataSource.getAllMessages()
    }

    suspend fun tryDisconnect(username: String) {
        members[username]?.socket?.close()

        // remove room member from our hashmap
        if(members.containsKey(username)) {
            members.remove(username)
        }

    }

}