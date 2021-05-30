package ktu.edu.myapplicationtest.app

import android.app.Application
import ktu.edu.myapplicationtest.model.database.MealRepository
import ktu.edu.myapplicationtest.model.database.MyMealsDatabase
import ktu.edu.myapplicationtest.model.database.UserRepository

class MyMealsApp:Application() {

    private val database by lazy  { MyMealsDatabase.getDatabase(this@MyMealsApp)}

    val repoMeals by lazy { MealRepository(database.mealDao())}
    val repoUser by lazy {UserRepository(database.userDao())}
}