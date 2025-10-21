package com.tomli.profftask.databases

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
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

    @Query("select * from user_data order by (guess_animal_count+right_choice_count) limit 3")
    fun getThreeBestCount(): Flow<List<UserData>>

    @Query("update user_data set right_choice_count=:count where id=:id")
    suspend fun setNewRightChoiceCount(id: Int, count: Int)

    @Query("update user_data set guess_animal_count=:count where id=:id")
    suspend fun setNewGuessAnimalCount(id: Int, count: Int)

    @Query("update user_data set image_uri=:image where id=:id")
    suspend fun changeIcon(id: Int, image: ByteArray)

    @Query("WITH sums_table AS (" +
            "    SELECT *, (right_choice_count + guess_animal_count) as total_sum" +
            "    FROM user_data), known_row AS (" +
            "    SELECT * FROM sums_table WHERE id = :id), next_row AS (" +
            "    SELECT * FROM sums_table" +
            "    WHERE total_sum = (" +
            "        SELECT MAX(total_sum) " +
            "        FROM sums_table " +
            "        WHERE total_sum < (SELECT total_sum FROM known_row) " +
            "    )), upper_rows AS ( " +
            "    SELECT * " +
            "    FROM sums_table " +
            "    WHERE total_sum > (SELECT total_sum FROM next_row) " +
            "    ORDER BY total_sum ASC " +
            "    LIMIT 2), all_rows AS ( " +
            "    SELECT * FROM known_row " +
            "    UNION " +
            "    SELECT * FROM next_row " +
            "    UNION " +
            "    SELECT * FROM upper_rows) " +
            "SELECT * FROM all_rows " +
            "ORDER BY total_sum DESC;")
    suspend fun getForWidget(id: Int): List<UserData>
}