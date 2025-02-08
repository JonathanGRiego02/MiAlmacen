package pgl.proyecto.adaptadores

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import pgl.proyecto.actividades.ui.ajustes.FirstFragment
import pgl.proyecto.actividades.ui.ajustes.SecondFragment
import pgl.proyecto.actividades.ui.ajustes.ThirdFragment
import pgl.proyecto.actividades.ui.ajustes.FourthFragment

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FirstFragment()
            1 -> SecondFragment()
            2 -> ThirdFragment()
            3 -> FourthFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}