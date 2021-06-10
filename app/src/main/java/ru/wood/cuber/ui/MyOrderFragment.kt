package ru.wood.cuber.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.daimajia.swipe.SwipeLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import ru.wood.cuber.R
import ru.wood.cuber.ViewDialog
import ru.wood.cuber.adapters.RecyclerCallback
import ru.wood.cuber.adapters.SwipeRecyclerAdapter2
import ru.wood.cuber.data.MyOrder
import ru.wood.cuber.databinding.FragmentMyOrderBinding
import ru.wood.cuber.databinding.ItemCalculateSwipeBinding
import ru.wood.cuber.utill.Utill.BUNDLE_CONTAINER_ID
import ru.wood.cuber.managers.ExcelManager
import ru.wood.cuber.view_models.ExcelViewModel
import ru.wood.cuber.view_models.OrderViewModel

@AndroidEntryPoint
class MyOrderFragment : BaseFragment() {
    private lateinit var navController: NavController
    private val viewModel: OrderViewModel by viewModels()
    private val excelViewModel: ExcelViewModel by viewModels()
    private lateinit var excelManager : ExcelManager
    private lateinit var adapter:SwipeRecyclerAdapter2<MyOrder,ItemCalculateSwipeBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        excelManager= ExcelManager(requireContext())
        showSplashScreen(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(false)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding= FragmentMyOrderBinding.inflate(inflater)
        val view=binding.root
        binding.fragment=this
        navController=findNavController(this)

        val recycler=binding.recycler
        with(viewModel){
            refreshList()
            liveData.observe(viewLifecycleOwner,{
                if (it==null){return@observe}
                adapter=SwipeRecyclerAdapter2(it,R.layout.item_calculate_swipe,
                        object : RecyclerCallback<ItemCalculateSwipeBinding, MyOrder> {
                            override fun bind(binder: ItemCalculateSwipeBinding, entity: MyOrder, position: Int, itemView: View) {
                                swipeHolderAction(binder, entity, position, itemView)
                                subscribeClickPosition(binder.include.clicableLayout, entity.id)
                            }
                        })
                recycler.adapter=adapter
                adapter.notifyDataSetChanged()
                hideSplashScreen(requireActivity())
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

    fun createNew( view: View){
        ViewDialog.showCreateCalculationDialog(requireContext(),"Введите номер расчета"){
            viewModel.addNew(it)
        }
    }

    fun subscribeClickPosition(clicableLayout: View, idPosition: Long){
        clicableLayout.setOnClickListener {
            val bundle= Bundle()
            bundle.putLong(BUNDLE_CONTAINER_ID, idPosition)
            navController.navigate(R.id.action_myCalculationsFragment_to_containersFragment,bundle)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        requireActivity().menuInflater.inflate(R.menu.my_calculate_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.upload -> {
                lifecycleScope.launch (Dispatchers.IO){
                    excelViewModel.getAllRow()
                }
                return true
            }
            /* R.id.download_pdf-> {
                 return true
             }*/
            else ->{
                return super.onOptionsItemSelected(item)}
        }
    }
    private fun swipeHolderAction(binder: ItemCalculateSwipeBinding, entity: MyOrder, position: Int, itemView: View){
        with(binder){
            this.include.entity=entity
            swipe.setShowMode(SwipeLayout.ShowMode.PullOut)

            lifecycleScope.launch(Dispatchers.Main){
                val result=async { withContext(Dispatchers.IO){
                    viewModel.containerslist(entity.id)
                }}
                val quantity="${result.await().size} шт."
                binder.include.quantity.text=quantity
            }

            swipe.addDrag(
                    SwipeLayout.DragEdge.Right, binder.swipe.findViewById(R.id.bottom_wraper))
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
                viewModel.deletePosition(entity)

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
/*
    fun uploadExcel(){
        lifecycleScope.launch(Dispatchers.Main){
            val list = async { withContext(Dispatchers.IO){
                excelViewModel.getAllRow()
            } }

            with(excelManager){
                val workbook: HSSFWorkbook= this.createFile(list.await())
                val ok=writeFile(workbook)
                if (ok){openFile()}
            }
        }
    }*/
}