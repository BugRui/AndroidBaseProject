package com.bugrui.jetpackproject.widget

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import java.util.ArrayList

/**
 * @Author: BugRui
 * @CreateDate: 2019/8/2 17:23
 * @Description: BehaviorFragmentPagerAdapter
 */
class BehaviorFragmentPagerAdapter(
    fm: FragmentManager,
    behavior: Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) :
    FragmentPagerAdapter(fm, behavior) {

    private val mFragmentList = ArrayList<Fragment>()

    fun addFragment(fragment: Fragment?) {
        if (fragment == null) return
        this.mFragmentList.add(fragment)
    }

    fun removeFragment(fragment: Fragment?) {
        if (fragment == null) return
        this.mFragmentList.remove(fragment)
    }

    fun addFragments(fragments: List<Fragment>) {
        this.mFragmentList.addAll(fragments)
    }

    fun clearFragments() {
        this.mFragmentList.clear()
    }

    fun getFragments(): ArrayList<Fragment> {
        return mFragmentList
    }

    fun <T : Fragment> getFragment(position: Int): T {
        return getItem(position) as T
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }
}
