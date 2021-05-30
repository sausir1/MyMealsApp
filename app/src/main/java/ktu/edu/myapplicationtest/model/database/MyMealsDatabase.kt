package ktu.edu.myapplicationtest.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ktu.edu.myapplicationtest.model.entities.Meal
import ktu.edu.myapplicationtest.model.entities.User


@Database(entities = arrayOf(Meal::class, User::class), version = 3, exportSchema = true)
abstract class MyMealsDatabase : RoomDatabase() {
    abstract fun mealDao():MealDao
    abstract fun userDao():UserDao


    companion object {
        @Volatile
        private var INSTANCE: MyMealsDatabase? = null

        fun getDatabase(context: Context): MyMealsDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyMealsDatabase::class.java,
                    "my_meals_database3"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }


}