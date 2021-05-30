package ktu.edu.myapplicationtest.viewmodel

import android.view.View
import androidx.annotation.WorkerThread
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ktu.edu.myapplicationtest.model.database.MealRepository
import ktu.edu.myapplicationtest.model.entities.Meal
import java.lang.IllegalArgumentException

class MealsViewModel(private val repo:MealRepository) :ViewModel() {

    fun insert(meal: Meal) = viewModelScope.launch {
        repo.insertMeal(meal)
    }

    fun update(meal:Meal) = viewModelScope.launch {
        repo.update(meal)
    }
    val getWhereFalse : LiveData<List<Meal>> = repo.allMealsFalse
    val getWhereTrue : LiveData<List<Meal>> = repo.allMealsTrue



    fun setCurrent(meal:Meal, status:Boolean) = viewModelScope.launch{
        repo.setFalse(meal,status)
    }
    val allMeals : LiveData<List<Meal>> = repo.getAllMeals.asLiveData()

    fun delete(meal: Meal) = viewModelScope.launch {
        repo.deleteMeal(meal)
    }

}

class MealsViewModelFactory(private val repo:MealRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MealsViewModel::class.java)){
            return MealsViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}