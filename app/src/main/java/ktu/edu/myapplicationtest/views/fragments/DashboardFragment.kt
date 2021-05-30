package ktu.edu.myapplicationtest.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ktu.edu.myapplicationtest.R
import ktu.edu.myapplicationtest.app.MyMealsApp
import ktu.edu.myapplicationtest.databinding.ActivityAddUpdateMealBinding
import ktu.edu.myapplicationtest.databinding.FragmentDashboardBinding
import ktu.edu.myapplicationtest.model.entities.Meal
import ktu.edu.myapplicationtest.viewmodel.DashboardViewModel
import ktu.edu.myapplicationtest.viewmodel.MealsViewModel
import ktu.edu.myapplicationtest.viewmodel.MealsViewModelFactory
import ktu.edu.myapplicationtest.views.activities.AddUpdateMealActivity
import ktu.edu.myapplicationtest.views.adapters.MealListAdapter

class DashboardFragment : Fragment() {

    private lateinit var adapterMeal: MealListAdapter

    private lateinit var dashboardBinding : FragmentDashboardBinding
    private val mealVM: MealsViewModel by viewModels{
        MealsViewModelFactory((requireActivity().application as MyMealsApp).repoMeals)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardBinding = FragmentDashboardBinding.inflate(inflater,container,false)

        return dashboardBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler = dashboardBinding.rvMealList as RecyclerView
        recycler.layoutManager = LinearLayoutManager(dashboardBinding.root.context)
        mealVM.allMeals.observe(viewLifecycleOwner){
            meals->
                meals.let{
                    if(it.isNotEmpty()){
                        dashboardBinding.errorTv.visibility = View.INVISIBLE
                        adapterMeal =  MealListAdapter(meals, { meal ->
                            val index = meals.indexOf(meal)
                            Toast.makeText(dashboardBinding.root.context,"Clicked on ${meal.id} to Edit",Toast.LENGTH_SHORT).show()
                            val intent = Intent(this.requireContext(),AddUpdateMealActivity::class.java)
                            intent.putExtra("id", index)
                            startActivityForResult(intent,1)
                        },{meal ->
                            val index = meals.indexOf(meal)
                            adapterMeal.removeFromList(meal)
                            mealVM.delete(meal)
                            adapterMeal.notifyItemRemoved(index)
                            Toast.makeText(dashboardBinding.root.context,"Clicked on ${meal.id} to Delete",Toast.LENGTH_SHORT).show()
                        })
                        recycler.adapter = adapterMeal
                        adapterMeal.updateMealsList(it)
                    } else {
                        dashboardBinding.errorTv.visibility = View.VISIBLE
                    }
                }
        }
    }
}