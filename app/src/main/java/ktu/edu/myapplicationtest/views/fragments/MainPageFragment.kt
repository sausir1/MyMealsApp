package ktu.edu.myapplicationtest.views.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ktu.edu.myapplicationtest.R
import ktu.edu.myapplicationtest.app.MyMealsApp
import ktu.edu.myapplicationtest.databinding.DialogListOptionsBinding
import ktu.edu.myapplicationtest.databinding.FragmentMainPageBinding
import ktu.edu.myapplicationtest.model.entities.Meal
import ktu.edu.myapplicationtest.utils.Constants
import ktu.edu.myapplicationtest.viewmodel.*
import ktu.edu.myapplicationtest.views.activities.AddUpdateMealActivity
import ktu.edu.myapplicationtest.views.activities.RegisterUserActivity
import ktu.edu.myapplicationtest.views.adapters.DialogListAdapter
import ktu.edu.myapplicationtest.views.adapters.MealListAdapter
import kotlin.math.round

class MainPageFragment : Fragment() {

    private lateinit var adapterMeal: MealListAdapter
    private lateinit var hBinding: FragmentMainPageBinding

    private val userVM: UserViewModel by viewModels{
        UserViewModelFactory((requireActivity().application as MyMealsApp).repoUser)
    }
    private val mealVM: MealsViewModel by viewModels{
        MealsViewModelFactory((requireActivity().application as MyMealsApp).repoMeals)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        hBinding = FragmentMainPageBinding.inflate(layoutInflater)
        getRecycler()
        hBinding.addCurrentBtn.setOnClickListener {
            mealVM.allMeals.observe(viewLifecycleOwner, {meals->
                addToCurrent(meals)
            })
        }
        val exists2 = false
        val userData = userVM.allData
        val exists = userVM.exists
        exists.observe(viewLifecycleOwner, { bool ->
            if (bool == false) {
                val intent = Intent(this.requireActivity(), RegisterUserActivity::class.java)
                startActivity(intent)
                return@observe
            } else {
                userData.observe(viewLifecycleOwner,{ user ->
                    mealVM.getWhereTrue.observe(viewLifecycleOwner, {meals ->
                        var carbs = 0
                        var prots = 0
                        var fats = 0
                        var cals = 0
                        for (meal in meals)
                        {
                            cals += meal.calories
                            carbs += meal.carbs
                            prots += meal.proteins
                            fats += meal.fats
                        }

                        hBinding.apply {
                            mainBar.progress = round((cals)/ user.calories * 100).toInt()
                            wholeCount.text = (cals).toString()
                            fatsBar.progress = round(fats / user.fats * 100).toInt()
                            carbsBar.progress = round(carbs / user.fats * 100).toInt()
                            protsBar.progress = round(prots / user.prots * 100).toInt()
                            anglCount.text = carbs.toString()
                            fatsCount.text = fats.toString()
                            protsCount.text = prots.toString()
                        }
                    })

                    hBinding.neededCount.text = user.calories.toString()

                })
            }})
        return hBinding.root
    }

    private fun getRecycler(){
        val recycler = hBinding.rvCurrentPlan as RecyclerView
        recycler.layoutManager = LinearLayoutManager(hBinding.root.context)
        mealVM.getWhereTrue.observe(viewLifecycleOwner){
                meals->
            meals.let{
                if(it.isNotEmpty()){
                    adapterMeal =  MealListAdapter(meals, {
                            meal ->
                    },
                        {meal ->
                        val index = meals.indexOf(meal)
                        adapterMeal.removeFromList(meal)
                        mealVM.setCurrent(meal,false)
                        adapterMeal.notifyItemRemoved(index)
                        Toast.makeText(hBinding.root.context,"Meal deleted from plan",Toast.LENGTH_SHORT).show()
                    })
                    recycler.adapter = adapterMeal
                    adapterMeal.updateMealsList(it)
                }
            }
        }
    }


    private fun addToCurrent(meals:List<Meal>){
        val rDialog = Dialog(this.requireContext())
        val binding: DialogListOptionsBinding = DialogListOptionsBinding.inflate(layoutInflater)
        rDialog.setContentView(binding.root)

        binding.dialogTitleTv.text = "PASIRINKITE VALGĮ"

        binding.rvDialogList.layoutManager = LinearLayoutManager(this.requireContext())
        val OptionsAdapter = MealListAdapter(meals,{meal->
            mealVM.setCurrent(meal, true)
        },{})
        binding.rvDialogList.adapter = OptionsAdapter
        rDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main_page,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_meal->{
                startActivity(Intent(requireActivity(),AddUpdateMealActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == resultCode){
            val intent = Intent(this.context,MainPageFragment::class.java)
            startActivity(intent)
        }
    }

    companion object {
        const val SUCCESS_REG = 1
        const val UNSUCCESS_REG = 2
    }
}