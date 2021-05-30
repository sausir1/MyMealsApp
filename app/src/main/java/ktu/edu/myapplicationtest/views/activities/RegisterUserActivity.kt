package ktu.edu.myapplicationtest.views.activities

import android.app.Dialog
import kotlin.math.round
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ktu.edu.myapplicationtest.R
import ktu.edu.myapplicationtest.app.MyMealsApp
import ktu.edu.myapplicationtest.databinding.ActivityAddUpdateMealBinding
import ktu.edu.myapplicationtest.databinding.ActivityRegisterUserBinding
import ktu.edu.myapplicationtest.databinding.DialogListItemBinding
import ktu.edu.myapplicationtest.databinding.DialogListOptionsBinding
import ktu.edu.myapplicationtest.model.entities.User
import ktu.edu.myapplicationtest.utils.Constants
import ktu.edu.myapplicationtest.viewmodel.UserViewModel
import ktu.edu.myapplicationtest.viewmodel.UserViewModelFactory
import ktu.edu.myapplicationtest.views.adapters.DialogListAdapter
import kotlin.math.pow

class RegisterUserActivity : AppCompatActivity() {

    private val userVM: UserViewModel by viewModels{
        UserViewModelFactory((application as MyMealsApp).repoUser)
    }

    private lateinit var rDialog:Dialog
    private lateinit var rBinding:ActivityRegisterUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var isExisting = false
        val exists = userVM.exists
        exists.observe(this, {bool ->
            if(bool){
                userVM.allData.observe(this@RegisterUserActivity, {user->
                    rBinding.apply{
                        userNameIn.tooltipText =  user.name
                        userActivityIn.tooltipText = user.active
                        userAge.tooltipText = user.toString()
                        userGenderIn.tooltipText = user.gender
                        userHeightIn.tooltipText = user.height.toString()
                        userWeightIn.tooltipText = user.weight.toString()
                        isExisting = true
                }})
            }
        })
        rBinding = ActivityRegisterUserBinding.inflate(layoutInflater)

        rBinding.userActivityIn.setOnClickListener{
            dialogPop(resources.getString(R.string.user_activity),Constants.activityLevels(),Constants.ACTIVITY_LEVEL)
        }
        rBinding.userGenderIn.setOnClickListener{
            dialogPop(resources.getString(R.string.user_gender),Constants.genders(),Constants.GENDER)
        }

        rBinding.saveUserBtn.setOnClickListener {
            val name = rBinding.userNameIn.text.toString()
            val age = rBinding.userAge.text.toString()
            val activity = rBinding.userActivityIn.text.toString()
            val weight = rBinding.userWeightIn.text.toString()
            val height = rBinding.userHeightIn.text.toString()
            val gender = rBinding.userGenderIn.text.toString()
            if(height.isNotBlank() && weight.isNotBlank()&&activity.isNotBlank()
                    && age.isNotBlank() && name.isNotBlank() && gender.isNotBlank())
            {
                val kmi = round(weight.toInt()/ (height.toDouble()/100).pow(2))
                val act_coef = getActivityCoef(activity)
                val pep = getPEP(act_coef, gender,weight.toDouble(),height.toDouble(),age.toInt())
                val carbs = pep * 0.60
                val fats = pep * 0.20
                val prots = pep * 0.20
                val user = User(name,age.toInt(),activity,weight.toDouble(), height.toDouble(),
                        gender,kmi,pep,carbs,fats,prots, round(pep))
                if(isExisting){
                    userVM.update(user)
                } else {
                    userVM.register(user)
                }
                Toast.makeText(this@RegisterUserActivity,
                        "Vartotojo duomenys išsaugoti!",
                        Toast.LENGTH_LONG).show()
                //setResult(RESULT_FIRST_USER)
                finish()
            } else{
                Toast.makeText(this@RegisterUserActivity,
                        "Visi laukai yra privalomi!",
                        Toast.LENGTH_LONG).show()
            }

        }

        setContentView(rBinding.root)
    }
    private fun getPEP(coef:Double, gender:String,  weight:Double, height:Double, age:Int) : Double
    {
        val pea = getPEA(gender,weight,height,age)
        return pea * coef
    }

    private fun getPEA(gender:String,weight: Double,height: Double,age: Int) : Double
    {
        if(gender == Constants.genders()[0]) //vyras
        {
            return 66.5 + (13.75 * weight) + (5.003 * height)-(6.755*age)
        }
        return 665 + (9.563*weight)+(1.85*height)- (4.676*age)
    }
    private fun getActivityCoef(name:String):Double {
        val index = Constants.activityLevels().indexOf(name)
        when(index){
            0 -> {
                return 1.5
            } 1 -> {
                return 1.7
            } 2 ->{
                return 2.0
            }
        }
        return 2.2
    }
    private fun dialogPop(title:String, options:List<String>, selected :String){
        rDialog = Dialog(this)
        val binding: DialogListOptionsBinding = DialogListOptionsBinding.inflate(layoutInflater)
        rDialog.setContentView(binding.root)

        binding.dialogTitleTv.text = title

        binding.rvDialogList.layoutManager = LinearLayoutManager(this)
        val OptionsAdapter = DialogListAdapter(this,options,selected)
        binding.rvDialogList.adapter = OptionsAdapter
        rDialog.show()
    }

    fun selectItem(item:String, selection:String)
    {
        when(selection){
            Constants.GENDER -> {
                rDialog.dismiss()
                rBinding.userGenderIn.setText(item)
            }
            Constants.ACTIVITY_LEVEL -> {
                rDialog.dismiss()
                rBinding.userActivityIn.setText(item)
            }

        }
    }
}