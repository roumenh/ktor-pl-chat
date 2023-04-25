package com.romanhruska.room

import io.ktor.websocket.*

data class Member(
    val username: String,
    val sessionId: String,
    val socket: WebSocketSession // what we use to actually send something to that member.
)
