package ktu.edu.myapplicationtest.model.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ktu.edu.myapplicationtest.model.entities.Meal


@Dao
interface MealDao {

    @Query("SELECT * FROM meals ORDER BY ID DESC")
    fun getAll() : Flow<List<Meal>>


    //@SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM meals WHERE isInPlan=:status")
    fun getWhere(status: Boolean) : Flow<List<Meal>>

    @Query("UPDATE meals SET isInPlan = :fl WHERE id=:id")
    suspend fun setFalse(fl:Boolean,id:Int)

    @Update
    suspend fun update(meal: Meal)

    @Insert
    suspend fun insert(meal:Meal)

    @Delete
    suspend fun delete(meal:Meal)
}