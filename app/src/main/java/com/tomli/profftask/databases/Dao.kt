package com.tomli.profftask.databases

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Query("insert into user_data (email, name, last_name, password, language, guess_animal_count, right_choice_count) values (:email,:name, :last_name, :password, :language, 0, 0)")
    suspend fun addUser(email: String, name: String, last_name: String, password: String, language: String)

    @Query("select * from user_data where email=:email and password=:password")
    suspend fun getUserOnLogin(email: String, password: String): UserData?

    @Query("select count(*) from user_data where email=:email and password=:password")
    suspend fun getUserOnLoginIsHave(email: String, password: String): Int

    @Query("select * from user_data where id=:id")
    suspend fun getUser(id: Int): UserData?
}