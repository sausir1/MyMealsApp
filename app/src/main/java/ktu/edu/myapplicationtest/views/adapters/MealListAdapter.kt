package ktu.edu.myapplicationtest.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ktu.edu.myapplicationtest.databinding.MealListItemBinding
import ktu.edu.myapplicationtest.model.entities.Meal

class MealListAdapter(var meals: List<Meal>, val editClick:(Meal)->Unit,
                      val deleteClick:(Meal)->Unit): RecyclerView.Adapter<MealListAdapter.ViewHolder>() {
    class ViewHolder(view: MealListItemBinding):RecyclerView.ViewHolder(view.root){
        val carbsText = view.tvCarbsNr
        val nameText = view.mealName
        val protsText = view.tvProtsNr
        val fatsText = view.tvFatsNr
        val calsText = view.tvCaloriesNr
        val editBtn = view.editBtn
        val removeBtn = view.deleteBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MealListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = meals[position]
        holder.apply {
            carbsText.text = "${current.carbs}"
            nameText.text = current.name
            protsText.text = "${current.proteins}"
            fatsText.text = "${current.fats}"
            calsText.text = "${current.calories}"
            editBtn.contentDescription = "${current.id}"
            removeBtn.contentDescription = "${current.id}"
        }
        holder.editBtn.setOnClickListener{
            editClick(current)
        }
        holder.removeBtn.setOnClickListener{
            deleteClick(current)
        }
    }

    override fun getItemCount(): Int {
        return meals.size
    }

    fun removeFromList(meal: Meal){
        val n = meals.indexOf(meal)
        meals.toMutableList().removeAt(n)

    }


    fun updateMealsList(list:List<Meal>){
        meals = list
        notifyItemInserted(meals.size-1)
    }
}