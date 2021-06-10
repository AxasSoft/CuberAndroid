package ru.wood.cuber.ui.diametrs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import ru.wood.cuber.Loger
import ru.wood.cuber.adapters.MyPagerAdapter
import ru.wood.cuber.databinding.FragmentDiametrContainerBinding
import ru.wood.cuber.ui.TreesFragment

private const val CONTAINER = "param1"

open class DiametrContainer : Fragment(){
    object Container{
        var id: Long? =null
        var length: Double? =null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            Container.id = it.getLong(CONTAINER)
            Loger.log(Container.id.toString()+"----------------------")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding=FragmentDiametrContainerBinding.inflate(inflater)
        val view=binding.root

        val tabLayout=binding.tabs
        val viewPager=binding.pager
        val adapter: FragmentStateAdapter = MyPagerAdapter(requireActivity())
        viewPager.adapter = adapter
        TabLayoutMediator(
            tabLayout, viewPager
        ) { tab, position -> }.attach()
        return view
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Long) =
                DiametrContainer().apply {
                    arguments = Bundle().apply {
                        putLong(CONTAINER, param1)
                    }
                }
    }
    fun getContainer():Long{
        return Container.id!!
    }
}