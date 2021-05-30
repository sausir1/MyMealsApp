package ktu.edu.myapplicationtest.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ktu.edu.myapplicationtest.model.database.MealRepository
import ktu.edu.myapplicationtest.model.database.UserRepository
import ktu.edu.myapplicationtest.model.entities.Meal
import ktu.edu.myapplicationtest.model.entities.User
import java.lang.IllegalArgumentException

class UserViewModel(private val repo:UserRepository) :ViewModel() {
    fun register(user: User) = viewModelScope.launch {
        repo.register(user)
    }
    val allData : LiveData<User> = repo.userData
    val exists : LiveData<Boolean> = repo.exists
    fun update(user: User) = viewModelScope.launch {
        repo.update(user)
    }

}
class UserViewModelFactory(private val repo: UserRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserViewModel::class.java)){
            return UserViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
