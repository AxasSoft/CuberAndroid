package ru.wood.cuber.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.databinding.ItemTreePositionBinding

class SimpleRecyclerAdapter (
    private val values: List<TreePosition>
) :
    RecyclerView.Adapter<SimpleRecyclerAdapter.MyViewHolder>() {
    lateinit var positionListener: OnPositionClickListener

    override fun getItemCount() = values.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding= ItemTreePositionBinding.inflate(inflater,parent,false)

        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding?.entity=values[position]
    }
    fun setPositionClickListener(listener: OnPositionClickListener){
        positionListener=listener
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var binding: ItemTreePositionBinding?=null

        init {
            binding= DataBindingUtil.bind(itemView)
        }

        override fun onClick(v: View?) {
            positionListener.onPositionClick(v!!, values[position].id.toInt())
        }
    }
    interface OnPositionClickListener{
        fun onPositionClick(view: View, id: Int)
    }
}