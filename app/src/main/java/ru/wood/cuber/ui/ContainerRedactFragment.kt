package ru.wood.cuber.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import ru.wood.cuber.R
import ru.wood.cuber.SimpleDialogFragment2
import ru.wood.cuber.databinding.FragmentContainerRedactBinding
import ru.wood.cuber.utill.Utill.BUNDLE_CONTAINER_ID
import ru.wood.cuber.view_models.ContainRedactViewModel
@AndroidEntryPoint
class ContainerRedactFragment : Fragment() {
    private var navController: NavController? =null
    private var supportToolbar : ActionBar? =null
    private var manager: FragmentManager?=null
    private val viewModel: ContainRedactViewModel by viewModels()
    private var idOfContain : Long? =null

    object Param{
        var lastName: String = ""
        var lastWeight: Long = 0

        var newName: String = ""
        var newWeight: Long = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idOfContain=arguments?.getLong(BUNDLE_CONTAINER_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val binding=FragmentContainerRedactBinding.inflate(inflater)
        val view = binding.root
        binding.fragment=this
        init(binding)

        with(viewModel){
            loadContainer(idOfContain!!)
            containerLive.observe(viewLifecycleOwner, {
                it?.let {
                    Param.apply {
                        lastName=it.name!!
                        lastWeight= it.weight!!
                        (binding.nameTv as TextView).text=lastName
                        (binding.weightTv as TextView).text=lastWeight.toString()
                        newName= lastName
                        newWeight= lastWeight
                    }
                    containerLive.value=null
                }
            })
            paramsIsSaved.observe(viewLifecycleOwner,{
                if (it!= null && it){
                    navController?.popBackStack()
                    paramsIsSaved.value=null
                }
            })
        }

        val toolbar=binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_left)
            isClickable
            backClickListener(this)
        }

        return view
    }

    private fun backClickListener(toolbar: MaterialToolbar){
        toolbar.setOnClickListener {
            when(paramIsChanged()){
                true -> letSavingParams()
                false -> Navigation.findNavController(it).popBackStack()
            }
        }
    }

    private fun letSavingParams(){
        quitDialog {
            viewModel.saveNewParams(
                container = idOfContain!!,
                name = Param.newName,
                weight = Param.newWeight
            )
        }
    }

    //-------------------------------------------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!?????????
    private fun quitDialog(positiveFunction: ()-> Unit){
        val dialog = SimpleDialogFragment2("Сохранить?",
            positiveFunction={positiveFunction()},
            negativeFunction= {navController?.popBackStack()}
        ).show(manager!!, "simpleDialog")
    }

    fun paramIsChanged(): Boolean{
        Param.apply {
         return  !(lastName== newName &&
                 lastWeight== newWeight)
        }
    }

    private fun init(binding : FragmentContainerRedactBinding){
        manager = requireActivity().supportFragmentManager
        navController= Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        supportToolbar= (activity as AppCompatActivity).supportActionBar?.apply {
            hide()
        }
    }

    fun containNameChange (s: CharSequence, start: Int, count: Int, after:Int){
        if (s.length!=0){
            Param.newName= s.toString()
        }
    }
    fun weightChange (s: CharSequence, start: Int, count: Int, after:Int){
        try {
            if (s.length!=0){
                Param.newWeight= s.toString().toLong()
            }
        } catch (t: Throwable){}
    }
    override fun onDestroyView() {
        super.onDestroyView()
        supportToolbar?.show()
    }
}