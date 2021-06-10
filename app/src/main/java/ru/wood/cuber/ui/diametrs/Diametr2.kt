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
import ru.wood.cuber.databinding.FragmentDiametr2Binding
import ru.wood.cuber.view_models.TreesViewModel
import ru.wood.cuber.volume.Volume

class Diametr2 : DiametrContainer() {
    private val viewModel: TreesViewModel by activityViewModels()
    var diametr: Int?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDiametr2Binding.inflate(inflater)
        val view=binding.root
        binding.fragment=this

        Loger.log(getContainer())
        return view
    }

    fun click_add (view:View){
        when(view.id){
            R.id.length_36->diametr=36
            R.id.length_37->diametr=37
            R.id.length_38->diametr=38
            R.id.length_39->diametr=39
            R.id.length_40->diametr=40
            R.id.length_41->diametr=41
            R.id.length_42->diametr=42
            R.id.length_43->diametr=43
            R.id.length_44->diametr=44
            R.id.length_45->diametr=45
            R.id.length_46->diametr=46
            R.id.length_47->diametr=47
            R.id.length_48->diametr=48
            R.id.length_49->diametr=49
            R.id.length_50->diametr=50
            R.id.length_51->diametr=51
        }

        lifecycleScope.launch {
            val result= async { withContext(Dispatchers.IO){
                Volume.calculateOne(diametr!!,viewModel.commonLength!!,1)
            }
            }
            val volume=result.await()

            viewModel.addNew(getContainer(), diametr, volume)
        }
    }
}