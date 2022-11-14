package ru.kekulta.kotlingrid.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import ru.kekulta.kotlingrid.adapter.ImageData.IMAGE_DRAWABLES
import ru.kekulta.kotlingrid.fragment.ImageFragment

class ImagePagerAdapter(fragment: Fragment) :
    // Note: Initialize with the child fragment manager.
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