package ktu.edu.myapplicationtest.utils

object Constants {
    const val MEAL_TYPE:String = "MealType"
    const val ACTIVITY_LEVEL:String = "Activity_level"
    const val GENDER:String = "Gender"
    const val KMI:String = "Kmi"

    fun mealTypes():ArrayList<String>{
        val list = ArrayList<String>()
        list.add("pusryčiai")
        list.add("pietūs")
        list.add("vakarienė")
        list.add("užkandžiai")
        return list
    }

    fun genders(): ArrayList<String>{
        val list = ArrayList<String>()
        list.add("Vyras")
        list.add("Moteris")
        return list
    }
    fun kmis(){
        val list = ArrayList<String>()
        list.add("Normalus")
        list.add("I laipsnio nutukimas")
        list.add("II laipsnio nutukimas")
        list.add("III laipsnio nutukimas")

    }

    fun activityLevels(): ArrayList<String>{
        val list = ArrayList<String>()
        list.add("Pasyvus/Neaktyvus")
        list.add("Vidutinis aktyvumas")
        list.add("Didelis aktyvumas")
        list.add("Labai didelis aktyvumas")
        return list
    }
}