package com.tomsky.androiddemo.viewpager2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 *
 * Created by wangzhitao on 2021/01/09
 *
 **/
class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    private val list = ArrayList<Int>()

    fun setData(items: List<Int>) {
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }

    fun addData(items: List<Int>) {
        list.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return ViewPagerFragment.newInstance(list[position])
    }

}