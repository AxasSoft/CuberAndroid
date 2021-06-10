package ru.wood.cuber.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import ru.wood.cuber.R
import ru.wood.cuber.data.MyСontainer
import ru.wood.cuber.databinding.ItemContainerSwipeBinding
import ru.wood.cuber.view_models.ContainsViewModel
import java.util.ArrayList

class SwipeRecyclerAdapter_UNUSED (context: Context, objects: List<MyСontainer>?, viewModel : ContainsViewModel):
    RecyclerSwipeAdapter<SwipeRecyclerAdapter_UNUSED.SimpleViewHolder>() {
    lateinit var positionListener: OnPositionClickListener
    lateinit var counterListener: OnCounterClickListener

    var mContext: Context? = context
    var list: ArrayList<MyСontainer> = objects as ArrayList<MyСontainer>
    val viewModel=viewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemContainerSwipeBinding = ItemContainerSwipeBinding.inflate(inflater, parent, false)
        return SimpleViewHolder(binding.getRoot())
    }

    override fun onBindViewHolder(viewHolder: SimpleViewHolder, position: Int) {

        val item: MyСontainer = list[position]
        viewHolder.binding?.entity = list[position]
        viewHolder.binding?.swipe?.setShowMode(SwipeLayout.ShowMode.PullOut)
        //viewModel.getQuantity(item.id,viewHolder.binding!!)

        //dari kanan
        viewHolder.binding!!.swipe.addDrag(
            SwipeLayout.DragEdge.Right, viewHolder.binding!!.swipe.findViewById(R.id.bottom_wraper))

        viewHolder.binding!!.swipe.addSwipeListener(object : SwipeLayout.SwipeListener {
            override fun onStartOpen(layout: SwipeLayout?) {}
            override fun onOpen(layout: SwipeLayout?) {}
            override fun onStartClose(layout: SwipeLayout?) {}
            override fun onClose(layout: SwipeLayout?) {}
            override fun onUpdate(layout: SwipeLayout?, leftOffset: Int, topOffset: Int) {}
            override fun onHandRelease(layout: SwipeLayout?, xvel: Float, yvel: Float) {}
        })

        viewHolder.binding!!.swipe.getSurfaceView().setOnClickListener(View.OnClickListener { v ->
            val bundle = Bundle()
            bundle.putInt("id", list[position].id.toInt())
            Navigation.findNavController(v).navigate(R.id.treesFragment, bundle)
        })

        viewHolder.binding!!.Delete.setOnClickListener(View.OnClickListener { v ->
            //viewModel.deleteExactly(list[position].id) //Удаление из БД

            mItemManger.removeShownLayouts(viewHolder.binding!!.swipe)
            list.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, list.size)

            mItemManger.closeAllItems()
            Toast.makeText(v.context, "Deleted " + viewHolder.binding!!.entity?.name.toString(),
                Toast.LENGTH_SHORT).show()
        })

        mItemManger.bindView(viewHolder.itemView, position)
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
        var binding: ItemContainerSwipeBinding?= DataBindingUtil.bind(itemView)

        init {
            binding!!.include.clicableLayout.setOnClickListener(this)

        }

        override fun onClick(v: View?) {
            if(v?.id==R.id.clicable_layout){
                positionListener.OnPositionClick(v,list[position].id.toInt())
            }
        }
    }

    interface OnPositionClickListener{
        fun OnPositionClick(view: View, id: Int)
    }
    interface OnCounterClickListener{
        fun OnCounterClick(view: View, entity: MyСontainer, position: Int)
    }
}
