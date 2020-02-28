package com.carrati.lebooks.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookstore_conf")
data class StoreBookLocal(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var writer: String = "",
    var thumb_url: String = "",
    var price: Int = 0,
    var favor: Boolean = false
)