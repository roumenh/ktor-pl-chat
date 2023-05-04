package com.romanhruska.di

import com.romanhruska.data.MessageDataSource
import com.romanhruska.data.MessageDataSourceImpl
import com.romanhruska.data.mysql.DbManager
import com.romanhruska.data.mysql.DbManagerImpl
import com.romanhruska.room.RoomController
import org.koin.dsl.module
import org.ktorm.database.Database
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import java.util.*

val mainModule = module {

    single {
        KMongo.createClient()
            .coroutine
            .getDatabase("message_db")
    } // returns CoroutineDatabase


    single<Database> {
        val props = Properties()
        props.load(javaClass.classLoader.getResourceAsStream("keys/keys"))
        Database.connect(
            url = props.getProperty("db.url"),
            driver = props.getProperty("db.driver"),
            user = props.getProperty("db.user"),
            password = props.getProperty("db.password")
        )
    }
     // should return Database

    single<MessageDataSource> {
        MessageDataSourceImpl(get())
    }

    single<DbManager> {
        DbManagerImpl(get())
    }

    single {
        RoomController(get())
    }

}