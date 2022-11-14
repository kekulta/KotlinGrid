package ru.kekulta.kotlingrid

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import ru.kekulta.kotlingrid.ImageData.IMAGE_DRAWABLES

class ImagePagerAdapter(fragment: Fragment) :
    FragmentStatePagerAdapter(
        fragment.childFragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
    override fun getCount(): Int {
        return IMAGE_DRAWABLES.size
    }

    override fun getItem(position: Int): Fragment {
        return ImageFragment.newInstance(IMAGE_DRAWABLES[position])
    }
}