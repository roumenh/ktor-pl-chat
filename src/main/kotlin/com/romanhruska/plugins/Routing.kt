package com.romanhruska.plugins

import com.romanhruska.room.RoomController
import com.romanhruska.routes.chatSocket
import com.romanhruska.routes.getAllMessages
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val roomController by inject<RoomController>() // thanks to this Koin will inject our RoomController
    install(Routing) {
        chatSocket(roomController)
        getAllMessages(roomController)
    }
}
