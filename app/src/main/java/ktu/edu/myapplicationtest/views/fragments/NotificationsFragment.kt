package ktu.edu.myapplicationtest.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ktu.edu.myapplicationtest.R
import ktu.edu.myapplicationtest.app.MyMealsApp
import ktu.edu.myapplicationtest.databinding.FragmentNotificationsBinding
import ktu.edu.myapplicationtest.viewmodel.NotificationsViewModel
import ktu.edu.myapplicationtest.viewmodel.UserViewModel
import ktu.edu.myapplicationtest.viewmodel.UserViewModelFactory
import ktu.edu.myapplicationtest.views.activities.RegisterUserActivity

class NotificationsFragment : Fragment() {

    private val userVM: UserViewModel by viewModels{
        UserViewModelFactory((requireActivity().application as MyMealsApp).repoUser)
    }

private lateinit var nBinding:FragmentNotificationsBinding
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        nBinding = FragmentNotificationsBinding.inflate(layoutInflater)
        userVM.allData.observe(viewLifecycleOwner, {data ->
            data?.let{
                nBinding.activityTv.text = data.active
                nBinding.ageTv.text = data.age.toString()
                nBinding.userNameTv.text = data.name
                nBinding.calsTv.text = String.format("%.2f", data.calories)
                nBinding.kmiTv.text = data.kmi.toString()
                nBinding.weightTv.text = data.weight.toString()
                nBinding.heightTv.text = data.height.toString()
            }
        })
        nBinding.editInfoBtn.setOnClickListener {
            val intent = Intent(this.requireActivity(), RegisterUserActivity::class.java)
            startActivity(intent)
        }
        return nBinding.root
    }
}