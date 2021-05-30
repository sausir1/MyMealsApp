package ktu.edu.myapplicationtest.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class Meal(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo val name:String,
        @ColumnInfo val calories:Int,
        @ColumnInfo val carbs:Int,
        @ColumnInfo val proteins:Int,
        @ColumnInfo val fats:Int,
        @ColumnInfo(defaultValue = "") val image: String,
        @ColumnInfo val isInPlan:Boolean
)
