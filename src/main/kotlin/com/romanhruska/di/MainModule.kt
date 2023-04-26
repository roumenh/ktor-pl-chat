package com.romanhruska.di

import com.romanhruska.data.MessageDataSource
import com.romanhruska.data.MessageDataSourceImpl
import com.romanhruska.room.RoomController
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {

    single {
        KMongo.createClient()
            .coroutine
            .getDatabase("message_db")
    } // returns CoroutineDatabase

    single<MessageDataSource> {
        MessageDataSourceImpl(get())
    }

    single {
        RoomController(get())
    }

}