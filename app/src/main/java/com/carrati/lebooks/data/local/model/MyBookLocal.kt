package com.carrati.lebooks.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mybooks_conf")
data class MyBookLocal (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var writer: String = "",
    var thumb_url: String = ""
)