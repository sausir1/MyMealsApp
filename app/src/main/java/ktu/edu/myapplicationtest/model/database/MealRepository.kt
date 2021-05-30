package ktu.edu.myapplicationtest.model.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import ktu.edu.myapplicationtest.model.entities.Meal

class MealRepository(private val mealDao:MealDao) {


    @Suppress
    @WorkerThread
    suspend fun insertMeal(meal: Meal){
        mealDao.insert(meal)
    }

    @WorkerThread
    suspend fun setFalse(meal: Meal, status:Boolean){
        mealDao.setFalse(status,meal.id)
    }

    val allMealsFalse = mealDao.getWhere(false).asLiveData()
    val allMealsTrue = mealDao.getWhere(true).asLiveData()
    val getAllMeals: Flow<List<Meal>> = mealDao.getAll()
    @Suppress
    @WorkerThread
    suspend fun update(meal: Meal){
        mealDao.update(meal)
    }

    @Suppress
    @WorkerThread
    suspend fun deleteMeal(meal:Meal){
        mealDao.delete(meal)
    }
}