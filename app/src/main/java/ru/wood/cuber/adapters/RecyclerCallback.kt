package ru.wood.cuber.adapters

import android.view.View
import androidx.databinding.ViewDataBinding

interface RecyclerCallback<VM : ViewDataBinding?, T> {
    fun bind(binder: VM, entity: T, position: Int, itemView: View)
}