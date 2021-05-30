package ktu.edu.myapplicationtest.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val id : Int,
    val name:String,
    val age: Int,
    val active: String,
    val weight: Double,
    val height: Double,
    val gender:String,
    val kmi: Double,
    val pep: Double,
    val carbs:Double,
    val fats:Double,
    val prots:Double,
    val calories:Double
)
