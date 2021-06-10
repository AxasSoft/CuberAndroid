package ru.wood.cuber.adapters

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.wood.cuber.ui.diametrs.Diametr1
import ru.wood.cuber.ui.diametrs.Diametr2

class MyPagerAdapter (@NonNull fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return Diametr1()
            1 -> return Diametr2()
        }
        return Diametr1()
    }

    override fun getItemCount(): Int {
        return 2
    }

}