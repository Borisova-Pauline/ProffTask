package com.tomli.profftask.databases

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data")
data class UserData(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val email: String?,
    val password: String?,
    val language: String?,
    val guess_animal_count: Int?,
    val right_choice_count: Int?
)
