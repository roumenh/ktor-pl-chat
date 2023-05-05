package com.romanhruska.di

import com.romanhruska.data.MessageDataSource
import com.romanhruska.data.mongo.MongoMessageDataSourceImpl
import com.romanhruska.data.mysql.MysqlMessageDataSourceImpl
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
    } // returns CoroutineDatabase //TODO remove


    single<Database> {
        val props = Properties()
        props.load(javaClass.classLoader.getResourceAsStream("keys.properties"))
        Database.connect(
            url = props.getProperty("db.url"),
            driver = props.getProperty("db.driver"),
            user = props.getProperty("db.user"),
            password = props.getProperty("db.password")
        )
    }
     // should return Database

    single<MessageDataSource> {
        MongoMessageDataSourceImpl(get())
    }

    single<MessageDataSource> {
        MysqlMessageDataSourceImpl(get())  // this is where I decide what source of data to use
    }

    single {
        RoomController(get())
    }

}