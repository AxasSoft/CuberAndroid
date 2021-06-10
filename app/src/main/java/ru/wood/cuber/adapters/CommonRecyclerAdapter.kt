package ru.wood.cuber.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import ru.wood.cuber.data.BaseItem
import ru.wood.cuber.databinding.ItemTreePositionBinding

class CommonRecyclerAdapter<T : BaseItem, VM : ViewDataBinding> (
    private val values: List<T>,
    layoutId: Int,
    binding: RecyclerCallback<VM, T>
) :
    RecyclerView.Adapter<CommonRecyclerAdapter<T,VM>.MyViewHolder>() {
    lateinit var positionListener: OnPositionClickListener
    private val layoutId = layoutId
    private var bindingInterface: RecyclerCallback<VM, T> =binding

    override fun getItemCount() = values.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(layoutId,parent,false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item: T = values[position]
        holder.bindData(item, position)
    }
    fun setPositionClickListener(listener: OnPositionClickListener){
        positionListener=listener
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        lateinit var binding: VM

        init {
            binding = DataBindingUtil.bind(itemView)!!
        }
        fun bindData(entity: T , position: Int) {
            bindingInterface.bind(binding, entity, position, itemView)
            binding.executePendingBindings()
        }

        override fun onClick(v: View?) {
            positionListener.onPositionClick(v!!, values[position].id.toInt())
        }
    }
    interface OnPositionClickListener{
        fun onPositionClick(view: View, id: Int)
    }
}