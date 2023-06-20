package com.rnd.baseproject.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
data class Note (
    /**If want to give collum a different name  use @ColumnInfo(name= "xyz") */
                 val title:String,
                 val note:String
): Serializable {
    /**We make It serializable because we are passing it from one frag to another*/
    //here we define columns
    //makes id autogenerate and primary key
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}