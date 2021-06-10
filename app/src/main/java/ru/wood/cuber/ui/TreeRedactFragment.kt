package ru.wood.cuber.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import ru.wood.cuber.Loger
import ru.wood.cuber.R
import ru.wood.cuber.SimpleDialogFragment
import ru.wood.cuber.utill.Utill.BUNDLE_QUANTITY
import ru.wood.cuber.utill.Utill.DECREASE
import ru.wood.cuber.utill.Utill.DIAMETERS
import ru.wood.cuber.utill.Utill.INCREASE
import ru.wood.cuber.utill.Utill.LENGTHS
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.databinding.FragmentTreeRedactBinding
import ru.wood.cuber.utill.Utill.BUNDLE_CONTAINER_ID
import ru.wood.cuber.utill.Utill.BUNDLE_TREE_ID
import ru.wood.cuber.view_models.TreeRedactViewModel

@AndroidEntryPoint
class TreeRedactFragment : Fragment() {
    private var navController: NavController? =null
    private var supportToolbar : ActionBar? =null
    private var manager: FragmentManager?=null
    private val viewModel: TreeRedactViewModel by activityViewModels()
    private var currentEntity: TreePosition?=null
    private var idOfContain : Long? =null

    override fun onDestroy() {
        super.onDestroy()
        Param.apply {
             newLength = 0.0
             newDiameter= 0
             newQuantity =0
        }
    }

    object Param{
        var lastLength: Double = 0.0
        var lastDiameter: Int = 0
        var lastQuantity: Int =0

        var newLength: Double = 0.0
        var newDiameter: Int = 0
        var newQuantity: Int =0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding= FragmentTreeRedactBinding.inflate(inflater)
        val view =binding.root
        binding.fragment=this

        val toolbar=binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_left)
            isClickable
            backClickListener(this)
        }

        commonInit(binding)
        val spinnerLength = spinnerLengthInit(binding)
        val spinnerDiameter = spinnerDiameterInit(binding)

        idOfContain=arguments?.getLong(BUNDLE_CONTAINER_ID)
        val id=arguments?.getLong(BUNDLE_TREE_ID)
        val quantity=arguments?.getInt(BUNDLE_QUANTITY)
        val editText=binding.editText

        Loger.log("onCreateView --------------LAST QUANTITY ${Param.lastQuantity} NEW QUANTITY ${Param.newQuantity}")

        with(viewModel){
            id?.let { getOneTree(it) }
            onePositionLiveData.observe(viewLifecycleOwner,{
                it?.let {
                    Loger.log("onePosition (LiveData) --------------LAST QUANTITY ${Param.lastQuantity} NEW QUANTITY ${Param.newQuantity}")
                    currentEntity=it
                    completion(currentEntity!!,spinnerLength,spinnerDiameter,editText,quantity!!)
                    onePositionLiveData.value=null
                }
            })
            paramsIsSaved.observe(viewLifecycleOwner,{
                it?.let {
                    if(it){
                        Loger.log("paramsIsSaved (liveData) --------------LAST QUANTITY ${Param.lastQuantity} NEW QUANTITY ${Param.newQuantity}")
                        when(quantityIsChanged()){
                            true-> {
                                Loger.log("letSavingQuantity2")
                                letSavingQuantity2(comparisonQuantity())}
                            else-> {
                                Loger.log("backStack")
                                backStack()}
                        }
                    }
                    paramsIsSaved.value=null
                }
            })
            redactFinished.observe(viewLifecycleOwner,{
                if (it!=null && it){
                    backStack()
                    redactFinished.value=null
                }
            })
        }
        return view
    }

    fun backStack(){
        navController?.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        supportToolbar?.show()
    }

    fun textChanged (s: CharSequence, start: Int, count: Int, after:Int){
        try {
            val quantity=s.toString().toInt()
            Param.newQuantity=quantity

        }catch (t: Throwable){
            Param.apply { newQuantity= lastQuantity }
        }
    }

    private fun chooseLengthPosition(tree: TreePosition,spinnerLength: Spinner){
        val position: Int=LENGTHS.indexOf(tree.length)
        spinnerLength.setSelection(position)
        Param.newLength=LENGTHS[position]

    }

    private fun chooseDiameterPosition(tree: TreePosition,spinnerDiameter: Spinner){
        val position: Int=DIAMETERS!!.indexOf(tree.diameter)
        spinnerDiameter.setSelection(position)
        Param.newDiameter=DIAMETERS!![position]

    }

    private fun completion(tree: TreePosition, spinnerLength: Spinner, spinnerDiameter: Spinner,editText: EditText, quantity: Int){
        Param.apply {
            lastLength=tree.length!!
            lastDiameter=tree.diameter!!
            lastQuantity=quantity
            newQuantity= lastQuantity
        }

        chooseLengthPosition(tree, spinnerLength)
        chooseDiameterPosition(tree, spinnerDiameter)

        (editText as TextView).text=quantity.toString()

        Loger.log("tree position")
        Loger.log((tree.length))
        Loger.log((tree.diameter))
        Loger.log(quantity.toString())
    }

    private fun backClickListener(toolbar: MaterialToolbar){
        toolbar.setOnClickListener {
            when {
                paramsIsChanged() -> {
                    Loger.log("let letSavingParams")
                    letSavingParams()
                }
                quantityIsChanged() -> {
                    Loger.log("let letSavingQuantity")
                    letSavingQuantity(comparisonQuantity())
                }
                else -> {
                    Loger.log("go to back stack")
                    backStack()
                }
            }
        }
    }

    private fun letSavingParams(){
        quitDialog {
            viewModel.saveNewParams(
                container = idOfContain!!,
                lastdiameter = Param.lastDiameter,
                lastLength = Param.lastLength,
                newDiameter =  Param.newDiameter,
                newLength = Param.newLength )
        }
    }

    private fun letSavingQuantity(comparisonResult : Int){
        Loger.log("letSavingQuantity and comparisonResult $comparisonResult")
        when(comparisonResult){
            INCREASE->{

                Loger.log("letSavingQuantity INCREASE")
                val count =Param.newQuantity-Param.lastQuantity   //увеличение +1
                quitDialog {viewModel.addPositions(
                    container = idOfContain!!,
                    count = count,
                    diameter = Param.newDiameter,
                    length = Param.newLength) }}
            DECREASE->{
                Loger.log("letSavingQuantity DECREASE")                                              //уменьшение -1
                val limit= Param.lastQuantity-Param.newQuantity
                quitDialog {viewModel.limitDelete(
                    container = idOfContain!!,
                    diameter =  Param.newDiameter,
                    length = Param.newLength,
                    limit = limit)  }}

        }
    }
    private fun letSavingQuantity2(comparisonResult : Int){
        Loger.log("letSavingQuantity2 and comparisonResult $comparisonResult")
        when(comparisonResult){
            INCREASE->{
                Loger.log("letSavingQuantity2 INCREASE")
                val count =Param.newQuantity-Param.lastQuantity   //увеличение +1
                viewModel.addPositions(
                    container = idOfContain!!,
                    count = count,
                    diameter = Param.newDiameter,
                    length = Param.newLength)}
            DECREASE->{
                Loger.log("letSavingQuantity2 DECREASE")                                                //уменьшение -1
                val limit= Param.lastQuantity-Param.newQuantity
                viewModel.limitDelete(
                    container = idOfContain!!,
                    diameter =  Param.newDiameter,
                    length = Param.newLength,
                    limit = limit)}

        }
    }

    private fun quitDialog(positiveFunction: ()-> Unit){
        val dialog =SimpleDialogFragment("Сохранить?",
                positiveAction= {positiveFunction()},
                negativeAction= {navController?.popBackStack()}
        ).show(manager!!, "simpleDialog")
    }

    private fun commonInit(binding : FragmentTreeRedactBinding){
        manager = requireActivity().supportFragmentManager
        navController= Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        supportToolbar= (activity as AppCompatActivity).supportActionBar?.apply {
            hide()
        }
    }
    private fun spinnerLengthInit(binding : FragmentTreeRedactBinding) : Spinner{
        val lengths :List<String> = ArrayList<String>().addSpinnerList(false,"Выберите длину", LENGTHS)
        return binding.spinnerLength.apply {
            setAdapter(lengths)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View,
                                            position: Int, id: Long) {
                    Param.newLength = LENGTHS[position]
                }

                override fun onNothingSelected(arg0: AdapterView<*>?) {}
            }
        }
    }
    private fun spinnerDiameterInit(binding : FragmentTreeRedactBinding): Spinner{
        val diameters :List<String> = ArrayList<String>().addSpinnerList(false,"Выберите диаметр", DIAMETERS as List<Int>)
        return binding.spinnerDiameter.apply {
            setAdapter(diameters)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View,
                                            position: Int, id: Long) {
                    Param.newDiameter= DIAMETERS!![position]
                }

                override fun onNothingSelected(arg0: AdapterView<*>?) {}
            }
        }
    }

    private fun comparisonQuantity(): Int{
        Param.apply {
            if (newQuantity> lastQuantity){
                return INCREASE
            } else if (newQuantity< lastQuantity){
                return DECREASE
            } else return 0
        }
    }

    private fun quantityIsChanged(): Boolean{
        Param.apply {
            return !(lastQuantity== newQuantity)
        }
    }

    private fun paramsIsChanged() : Boolean{
        Param.apply {
            return !(lastLength== newLength &&
                    lastDiameter== newDiameter)
        }
    }

    private fun <T : Number> ArrayList<String>.addSpinnerList(placeholder: Boolean,msg: String, numbers: List<T>): List<String>{
        if (placeholder){
            this.add(msg)
        }
        for (item: T in numbers){
            this.add(item.toString())
        }
        return this
    }

    private fun Spinner.setAdapter(list: List<String>){
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.adapter = adapter
    }
}