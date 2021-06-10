package ru.wood.cuber.ui.diametrs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.wood.cuber.Loger
import ru.wood.cuber.R
import ru.wood.cuber.data.VolumesTab
import ru.wood.cuber.databinding.FragmentDiametr1Binding
import ru.wood.cuber.view_models.TreesViewModel
import ru.wood.cuber.volume.Volume

class Diametr1 : DiametrContainer() {
    private val viewModel: TreesViewModel by activityViewModels()
    var diametr: Int?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDiametr1Binding.inflate(inflater)
        val view=binding.root
        binding.fragment=this


        return view
    }

    fun click_add( view: View){
        when(view.id){
            R.id.length_16->diametr=16
            R.id.length_17->diametr=17
            R.id.length_18->diametr=18
            R.id.length_19->diametr=19
            R.id.length_20->diametr=20
            R.id.length_21->diametr=21
            R.id.length_22->diametr=22
            R.id.length_23->diametr=23
            R.id.length_24->diametr=24
            R.id.length_25->diametr=25
            R.id.length_26->diametr=26
            R.id.length_27->diametr=27
            R.id.length_28->diametr=28
            R.id.length_29->diametr=29
            R.id.length_30->diametr=30
            R.id.length_31->diametr=31
            R.id.length_32->diametr=23
            R.id.length_33->diametr=33
            R.id.length_34->diametr=34
            R.id.length_35->diametr=35
        }

        lifecycleScope.launch {
            val result= async { withContext(Dispatchers.IO){
                Volume.calculateOne(diametr!!,viewModel.commonLength!!,1)
            }}
            val volume=result.await()

            viewModel.addNew(getContainer(), diametr, volume)
        }
        //------

    }
}