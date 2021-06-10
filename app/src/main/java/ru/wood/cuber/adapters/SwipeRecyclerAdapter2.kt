package ru.wood.cuber.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import ru.wood.cuber.R
import ru.wood.cuber.data.BaseItem
import ru.wood.cuber.data.MyСontainer
import java.util.*

class SwipeRecyclerAdapter2<T : BaseItem, VM : ViewDataBinding>(
    objects: List<T>?,
    layoutId: Int,
    binding: RecyclerCallback<VM, T>
):
    RecyclerSwipeAdapter<SwipeRecyclerAdapter2<T, VM>.SimpleViewHolder>() {
    lateinit var positionListener: OnPositionClickListener
    lateinit var counterListener: OnCounterClickListener

    private val layoutId = layoutId
    private var bindingInterface: RecyclerCallback<VM, T> =binding

    var list: ArrayList<T> = objects as ArrayList<T>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return SimpleViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: SimpleViewHolder, position: Int) {

        val item: T = list[position]
        viewHolder.bindData(item, position)
    }
    fun setPostionClickListener(listener: OnPositionClickListener){
        positionListener=listener
    }
    fun setCounterClickListener(listener: OnCounterClickListener){
        counterListener=listener
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipe
    }

    inner class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        lateinit var binding: VM


        init {
            binding = DataBindingUtil.bind(itemView)!!
            itemView.setOnClickListener(this)
        }
        fun bindData(entity: T , position: Int) {
            bindingInterface.bind(binding, entity, position, itemView)
            binding.executePendingBindings()
        }

        override fun onClick(v: View?) {
            positionListener.OnPositionClick(v!!, list[position].id.toInt())
        }
    }

    interface OnPositionClickListener{
        fun OnPositionClick(view: View, id: Int)
    }
    interface OnCounterClickListener{
        fun OnCounterClick(view: View, entity: MyСontainer, position: Int)
    }
}

