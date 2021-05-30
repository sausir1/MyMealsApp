package ktu.edu.myapplicationtest.views.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ktu.edu.myapplicationtest.databinding.DialogListItemBinding
import ktu.edu.myapplicationtest.views.activities.MainActivity
import ktu.edu.myapplicationtest.views.activities.RegisterUserActivity
import ktu.edu.myapplicationtest.views.fragments.MainPageFragment

class DialogListAdapter(
        private val activity:Activity,
        private val list:List<String>,
        private val selection:String
        ) :RecyclerView.Adapter<DialogListAdapter.ViewHolder>()
{
    class ViewHolder(view:DialogListItemBinding):RecyclerView.ViewHolder(view.root)
    {
        val tvText = view.itemTv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DialogListItemBinding.inflate(LayoutInflater.from(activity), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = list[position]
        holder.tvText.text = current
        holder.itemView.setOnClickListener{
            if(activity is RegisterUserActivity){
                activity.selectItem(current,selection)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}