package ru.wood.cuber.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.daimajia.swipe.SwipeLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import ru.wood.cuber.Loger
import ru.wood.cuber.R
import ru.wood.cuber.ViewDialog
import ru.wood.cuber.adapters.RecyclerCallback
import ru.wood.cuber.adapters.SwipeRecyclerAdapter2
import ru.wood.cuber.data.MyСontainer
import ru.wood.cuber.databinding.FragmentContainBinding
import ru.wood.cuber.databinding.ItemContainerSwipeBinding
import ru.wood.cuber.managers.ExcelManager
import ru.wood.cuber.utill.Utill.BUNDLE_CONTAINER_ID
import ru.wood.cuber.view_models.ContainsViewModel
import ru.wood.cuber.view_models.ExcelViewModel
import ru.wood.cuber.volume.Volume

@AndroidEntryPoint
class ContainFragment : Fragment() {
    private lateinit var navController: NavController
    private val viewModel: ContainsViewModel by viewModels()
    private val excelViewModel: ExcelViewModel by viewModels()
    private lateinit var adapter: SwipeRecyclerAdapter2<MyСontainer, ItemContainerSwipeBinding>
    private var idOfCalculate: Long? =null
    private  var actionBar: ActionBar? =null
    private var viewIsVisible : Boolean=false
    private lateinit var editText: EditText
    private lateinit var myCustomView: View
    private lateinit var currentList : List<MyСontainer>
    private lateinit var excelManager : ExcelManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        excelManager= ExcelManager(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionBar=(activity as AppCompatActivity).supportActionBar
        actionBar?.apply {
            title = "Мои расчеты"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowCustomEnabled(true)
            customView=myCustomView
            customView.visibility = View.GONE
        }
        viewIsVisible = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding=FragmentContainBinding.inflate(inflater);
        val view=binding.root
        binding.fragment=this
        navController= NavHostFragment.findNavController(this)

        /*(activity as AppCompatActivity).supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_HOME
            displayOptions = ActionBar.DISPLAY_SHOW_TITLE
            setDisplayHomeAsUpEnabled(true)
            title="3434634634"
        }*/
        val recycler=binding.recycler

        //for action bar
        myCustomView=inflater.inflate(R.layout.custom_view_search, null).also {
            editText=it.findViewById<EditText>(R.id.edit_text_search)
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s!!.length>2){
                        val text=s.toString()
                        println(s.toString())
                        currentList.let {
                            println("3")
                            val list=search(text,it)
                            viewModel.liveData.value=list
                            /*if (list?.size>0){
                                viewModel.liveData.value=list
                            }*/
                        }
                    }  else if (s.isEmpty()){
                        idOfCalculate?.let { it1 -> viewModel.refreshList(it1); println("4") }
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }

        idOfCalculate= arguments?.getLong(BUNDLE_CONTAINER_ID)
        with(viewModel){
            idOfCalculate?.let {refreshList(it)}
            liveData.observe(viewLifecycleOwner, {
                if (it == null ) {
                    println("1 it")
                    return@observe
                }
                if (it.isNotEmpty()){
                    currentList=it
                }

                adapter = SwipeRecyclerAdapter2(it, R.layout.item_container_swipe,
                    object : RecyclerCallback<ItemContainerSwipeBinding, MyСontainer> {
                        override fun bind(
                            binder: ItemContainerSwipeBinding,
                            entity: MyСontainer,
                            position: Int,
                            itemView: View
                        ) {
                            swipeHolderAction(binder, entity, position, itemView)
                            subscribeClickPosition(binder.include.clicableLayout, entity.id)

                            Loger.log("id ${entity.id}")

                            //Расчет кол-ва
/*
                                commonQuantity.observe(this@ContainFragment.viewLifecycleOwner , {
                                    it?.let {
                                        binder.include.quantity.text=it.toString()
                                    }
                                })*/

                        }
                    })
                // val adapter= SwipeRecyclerAdapter_UNUSED(requireContext(),it, viewModel)
                recycler.adapter = adapter
                //adapter.notifyDataSetChanged()
            })

        }
        excelViewModel.liveData.observe(viewLifecycleOwner,{
            it?.let{
                if (it.isEmpty()){
                    Toast.makeText(requireContext(),"У вас нет ни одной записи", Toast.LENGTH_SHORT).show()
                } else{
                    println("true \n"+it)
                    with(excelManager){
                        val workbook: HSSFWorkbook= this.createFile(it)
                        val ok=writeFile(workbook)
                        if (ok){openFile()}
                    }
                }
                excelViewModel.liveData.value=null
            }
        })

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        actionBar?.setDisplayShowCustomEnabled(false)
    }

    fun backStack(){
        navController.popBackStack()
    }
    fun createNew(view: View){
        ViewDialog.showCreateCalculationDialog(requireContext(), "Введите номер контейнера"){
            viewModel.addNew(it, idOfCalculate!!)
        }
    }

    fun goToTrees(v: View){
        Navigation.findNavController(v).navigate(R.id.action_containersFragment_to_treesFragment)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        requireActivity().menuInflater.inflate(R.menu.contains_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.upload -> {
                lifecycleScope.launch (Dispatchers.IO){
                    excelViewModel.getAllRow()
                }
                return true
            }
            R.id.search -> {
                if (viewIsVisible) {
                    Loger.log("2 $viewIsVisible")
                    actionBar!!.customView.visibility = View.GONE
                    viewIsVisible = false
                } else {
                    Loger.log("3 $viewIsVisible")
                    actionBar!!.customView.visibility = View.VISIBLE
                    viewIsVisible = true
                }
                return true
            }
            android.R.id.home -> {
                backStack(); return true
            }
            else ->{
                return super.onOptionsItemSelected(item)}
        }
    }

    fun subscribeClickPosition(clicableLayout: View, idPosition: Long){
        clicableLayout.setOnClickListener {
            val bundle= Bundle()
            bundle.putLong(BUNDLE_CONTAINER_ID, idPosition)
            navController.navigate(R.id.action_containersFragment_to_treesFragment, bundle)
        }
    }

    private fun swipeHolderAction(
        binder: ItemContainerSwipeBinding,
        entity: MyСontainer,
        position: Int,
        itemView: View
    ){
        with(binder){
            this.entity=entity
            val quantity=binder.include.quantity

            //------------------------------------------------
            lifecycleScope.launch {
               val result= async { withContext(Dispatchers.IO){
                   Loger.log(viewModel.getQuantity(entity.id).toString() + "в корутинах //////////")
                   viewModel.getQuantity(entity.id) }
               }
                quantity.text="${result.await().toString()} шт."
            }

            //------------------------------------------------
            lifecycleScope.launch(Dispatchers.Main){
                val list= async{withContext(Dispatchers.IO){
                    viewModel.loadListTrees(entity.id) }
                }
                val result= Volume.total(list.await())
                //val totalVolume=result.toString() + " м³"
                val totalVolume="%.2f".format(result) + " м³"
                binder.include.volume.text= totalVolume
            }
            //-------------------------------------------------

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
                viewModel.deltePosition(entity, idOfCalculate!!)

                adapter.apply {
                    mItemManger.removeShownLayouts(binder.swipe)
                    list.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, adapter.list.size)
                    mItemManger.closeAllItems()
                }

                Toast.makeText(requireContext(), "Позиция удалена", Toast.LENGTH_SHORT).show()
            })

        }
        adapter.mItemManger.bindView(itemView, position)
    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }
}

private fun search(text: String,list: List<MyСontainer> ):List<MyСontainer> {
    println("extension begin *******")
    var result= arrayListOf<MyСontainer>()
    for (container in list){
        val name=container.name!!
        if (name.contains(text, ignoreCase = true)){
            println("true")
            result.add(container)
        } else println("false")
    }
    return result
}