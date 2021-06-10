package ru.wood.cuber.ui

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.daimajia.swipe.SwipeLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.wood.cuber.Loger
import ru.wood.cuber.R
import ru.wood.cuber.utill.Utill
import ru.wood.cuber.utill.Utill.BUNDLE_QUANTITY
import ru.wood.cuber.ViewDialog
import ru.wood.cuber.adapters.RecyclerCallback
import ru.wood.cuber.adapters.SimpleRecyclerAdapter
import ru.wood.cuber.adapters.SwipeRecyclerAdapter2
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.data.VolumesTab
import ru.wood.cuber.databinding.FragmentTreesBinding
import ru.wood.cuber.databinding.ItemTreesSwipeBinding
import ru.wood.cuber.ui.diametrs.DiametrContainer
import ru.wood.cuber.utill.Utill.BUNDLE_CONTAINER_ID
import ru.wood.cuber.utill.Utill.BUNDLE_TREE_ID
import ru.wood.cuber.utill.Utill.BUNDLE_VOLUME
import ru.wood.cuber.utill.Utill.LENGTHS
import ru.wood.cuber.view_models.ContainsViewModel
import ru.wood.cuber.view_models.ResultViewModel
import ru.wood.cuber.view_models.TreesViewModel
import ru.wood.cuber.volume.Volume

@AndroidEntryPoint
class TreesFragment : Fragment(), SimpleRecyclerAdapter.OnPositionClickListener{
    private var navController: NavController? =null
    private val viewModel: TreesViewModel by activityViewModels()
    private val containers: ContainsViewModel by activityViewModels()
    private var adapter: SwipeRecyclerAdapter2<TreePosition, ItemTreesSwipeBinding>?=null
    private var currentPositionLength : Int = 6
    private lateinit var spinnerText: TextView
    private var actionBar: ActionBar?=null
    private var idOfContain : Long? =null
    private lateinit var binding:FragmentTreesBinding

    private lateinit var totalVolume:String
    private lateinit var totalQuantity:String

    fun createDiametrFrag(){
        val diametrFrag=DiametrContainer.newInstance(idOfContain!!)
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.diametr_container,diametrFrag).commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.diameters.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        idOfContain= arguments?.getLong(BUNDLE_CONTAINER_ID)
        createDiametrFrag()
        viewModel.commonLength?.let {
            val position=LENGTHS.indexOf(it)
            currentPositionLength=position
        }
        navController= Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        actionBar=(activity as AppCompatActivity).supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title=""
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding=FragmentTreesBinding.inflate(inflater)
        //binding.result.paintFlags = binding.result.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        val view= binding.root
        val recycler=binding.recycler
        val spinnerLength : Spinner = binding.spinnerLength
        spinnerText=binding.spinnerText

        val lengths :List<String> = ArrayList<String>().addSpinnerList(
                Utill.LENGTHS
        )

        val volume=binding.volume.apply {
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
            displayVolume()
            setOnClickListener {
                goToResult()
            }
        }

        spinnerLength.apply {
            setAdapter(lengths)
            applyNewLength(this@apply,lengths )

            onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                    if (position!=currentPositionLength){
                        ViewDialog.showDialogOfLength(context,
                                positiveAction={currentPositionLength = position; },
                                commonAction={applyNewLength(this@apply,lengths )},
                                checkBoxAction={
                                    viewModel.apply {
                                        changeCommonLength(Utill.LENGTHS[position], idOfContain!!)
                                        //changeVolumes(idOfContain!!,Utill.LENGTHS[position])
                                    }}
                        )
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        with(viewModel){
            loadContainer(idOfContain!!)

            Loger.log("container $idOfContain")
            containerLive.observe(viewLifecycleOwner,{
                it?.let {
                    actionBar?.title = it.name
                    containerLive.value=null
                }
            })

            refreshList(idOfContain!!)
            liveData.observe(viewLifecycleOwner, {
                displayVolume()
                it?.let {
                    /*for (tree in it){
                        if (!viewModel.diameters.contains(tree.diameter)) {
                            viewModel.diameters.add(tree.diameter!!)
                        }
                    }*/
                    Loger.log("refreshinglist in Live data$it")
                    adapter = SwipeRecyclerAdapter2(it, R.layout.item_trees_swipe,
                            object : RecyclerCallback<ItemTreesSwipeBinding, TreePosition> {
                                override fun bind(
                                        binder: ItemTreesSwipeBinding,
                                        entity: TreePosition,
                                        position: Int,
                                        itemView: View
                                ) {
                                    swipeHolderAction(binder, entity, position, itemView)
                                    subscribeClickPosition(binder.include.clicableLayout, entity)
                                }
                            })
                    recycler.adapter = adapter
                    adapter!!.notifyDataSetChanged()

                    //actionBar?.title = "name"
                    liveData.value=null
                }
            })
        }
        containers.orderLive.observe(viewLifecycleOwner,{
            it?.let {
                createNew(it)
                containers.orderLive.value=null
            }
        })

        return view
    }

    fun backStack(){
        navController?.popBackStack()
    }
    fun goToResult(){
        Loger.log("click")
        idOfContain?.let {
            val bundle= Bundle()
            bundle.putLong(BUNDLE_CONTAINER_ID,it)
            bundle.putString(BUNDLE_VOLUME, totalVolume)
            bundle.putString(BUNDLE_QUANTITY, totalQuantity)
            navController?.navigate(R.id.action_treesFragment_to_resultFragment,bundle)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        requireActivity().menuInflater.inflate(R.menu.trees_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                backStack(); return true
            }
            R.id.redact -> {
                val bundle = Bundle()
                bundle.putLong(BUNDLE_CONTAINER_ID, idOfContain!!)
                navController?.navigate(R.id.action_treesFragment_to_containerRedactFragment,bundle)
                return true
            }
            R.id.add->{
                containers.getOrderId(idOfContain!!)
                return true
            }
            else ->{
                return super.onOptionsItemSelected(item)}
        }
    }

    override fun onPositionClick(view: View, id: Int) {

    }

    @SuppressLint("SetTextI18n")
    private fun displayVolume() {
        lifecycleScope.launch(Dispatchers.Main){
            val list= async{withContext(Dispatchers.IO){
                viewModel.loadList(idOfContain!!) }
            }
            val result= Volume.total(list.await())
            totalVolume="%.2f".format(result) + "м³"
            totalQuantity="${list.await().size} шт"
            binding.volume.text= "%.2f".format(result) + "м³"
            binding.quantity.text= totalQuantity
        }
    }

    private fun subscribeClickPosition(clicableLayout: View, entity: TreePosition){
        clicableLayout.setOnClickListener {
            val bundle= Bundle()
            bundle.putLong(BUNDLE_CONTAINER_ID,idOfContain!!)
            bundle.putLong(BUNDLE_TREE_ID, entity.id)
            bundle.putInt(BUNDLE_QUANTITY,entity.quantity)
            Loger.log("•••• "+entity.quantity)
            navController?.navigate(R.id.action_treesFragment_to_treeRedactFragment, bundle)
        }
    }

    private fun applyNewLength(spinner: Spinner , lengths :List<String>){
        currentPositionLength.let {
            spinner.setSelection(it)
            spinnerText.text = lengths[it] + "м"
            viewModel.commonLength= Utill.LENGTHS[it]
        }
    }

    private fun swipeHolderAction(
            binder: ItemTreesSwipeBinding,
            entity: TreePosition,
            position: Int,
            itemView: View
    ){
        with(binder){

            val textView=binder.include.volume

            //------------------------------------------------
            lifecycleScope.launch {
                val result= async { withContext(Dispatchers.IO){
                    Volume.calculateOne(
                         entity.diameter!!,
                         entity.length!!,
                         entity.quantity) }
                }
                val volume=result.await()

                /*if (volume==null){
                    textView.text=""
                } else */
                textView.text= String.format("%.2f", volume)
            }
            //------------------------------------------------

            this.entity=entity
            swipe.setShowMode(SwipeLayout.ShowMode.PullOut)
            //dari kanan
            swipe.addDrag(
                    SwipeLayout.DragEdge.Right, binder.swipe.findViewById(R.id.bottom_wraper)
            )
            swipe.addSwipeListener(object : SwipeLayout.SwipeListener {
                override fun onStartOpen(layout: SwipeLayout?) {}
                override fun onOpen(layout: SwipeLayout?) {}
                override fun onStartClose(layout: SwipeLayout?) {}
                override fun onClose(layout: SwipeLayout?) {}
                override fun onUpdate(layout: SwipeLayout?, leftOffset: Int, topOffset: Int) {}
                override fun onHandRelease(layout: SwipeLayout?, xvel: Float, yvel: Float) {}
            })
            swipe.getSurfaceView().setOnClickListener(View.OnClickListener { v ->
                val bundle = Bundle()
                bundle.putLong("id", entity.id)
                Navigation.findNavController(v).navigate(R.id.treesFragment, bundle)
            })

            Delete.setOnClickListener(View.OnClickListener { v ->
                //viewModel.deleteExactly(list[position].id) //Удаление из БД
                viewModel.deletePosition(entity, idOfContain!!)

                adapter!!.apply {
                    mItemManger.removeShownLayouts(binder.swipe)
                    list.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, adapter!!.list.size)
                    mItemManger.closeAllItems()
                }

                Toast.makeText(requireContext(), "Позиция удалена", Toast.LENGTH_SHORT).show()
            })

        }
        adapter!!.mItemManger.bindView(itemView, position)
    }

    private fun <T : Number> ArrayList<String>.addSpinnerList(numbers: List<T>): MutableList<String>{
        for (item: T in numbers){
            this.add(item.toString())
        }
        return this
    }

    private fun Spinner.setAdapter(list: List<String>){
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                list
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.adapter = adapter
    }
    fun createNew(order: Long){
        ViewDialog.showCreateCalculationDialog(requireContext(), "Введите номер контейнера"){
            lifecycleScope.launch (Dispatchers.Main){
                val id=async { withContext(Dispatchers.IO){
                    containers.addNewAndGetId(it,order)
                }}
                actionBar?.title=it
                idOfContain=id.await()
                viewModel.refreshList(idOfContain!!)
                createDiametrFrag()
            }
        }
    }
}