package com.example.schedulertodo.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Note(
    val title: String,
    val note: String,
    val date:String,
    val cat:String,
    val stat:String
):Serializable{
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}