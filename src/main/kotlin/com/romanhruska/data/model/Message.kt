package com.romanhruska.data.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Message(
    val text: String,
    val username: String,
    val timestamp: Long,
    @BsonId         // is this needed? probably yeah
    val id: String = ObjectId().toString()  //will generate random object ID
)
