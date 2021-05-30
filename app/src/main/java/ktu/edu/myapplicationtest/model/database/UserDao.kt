package ktu.edu.myapplicationtest.model.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ktu.edu.myapplicationtest.model.entities.Meal
import ktu.edu.myapplicationtest.model.entities.User


@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getUserData() : Flow<User>

    @Query("SELECT EXISTS(SELECT * FROM user)")
    fun exists(): Flow<Boolean>

    @Insert
    suspend fun register(user: User)


    @Update
    suspend fun update(user: User)

}