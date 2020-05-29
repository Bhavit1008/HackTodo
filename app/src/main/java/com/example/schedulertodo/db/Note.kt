package com.example.schedulertodo.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note (
    val title:String,
    val note:String
){
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0
}