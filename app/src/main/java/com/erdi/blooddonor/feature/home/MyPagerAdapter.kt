package com.erdi.blooddonor.feature.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.erdi.blooddonor.feature.home.allposts.AllPostsFragment
import com.erdi.blooddonor.feature.home.postinmycity.PostInMyCityFragment
import com.erdi.blooddonor.feature.home.postinmydistrict.PostInMyDistrictFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllPostsFragment()
            1 -> PostInMyCityFragment()
            else -> PostInMyDistrictFragment()
        }
    }
}
