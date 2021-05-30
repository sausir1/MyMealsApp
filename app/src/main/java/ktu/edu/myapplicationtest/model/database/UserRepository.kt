package ktu.edu.myapplicationtest.model.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import ktu.edu.myapplicationtest.model.entities.Meal
import ktu.edu.myapplicationtest.model.entities.User

class UserRepository(private val userDao: UserDao) {


    @WorkerThread
    suspend fun register(user:User){
        userDao.register(user)
    }




    @WorkerThread
    suspend fun update(user:User){
        userDao.update(user)
    }
    val exists: LiveData<Boolean> = userDao.exists().asLiveData()

    val userData : LiveData<User> = userDao.getUserData().asLiveData()


}