package com.tomsky.androiddemo.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tomsky.androiddemo.databinding.ActivityViewPager2Binding
import com.tomsky.androiddemo.viewpager2.ViewPagerAdapter

class ViewPager2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityViewPager2Binding

    private var pagerAdapter: ViewPagerAdapter? = null

    private var currentIndex = 0
    private val step = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewPager2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        pagerAdapter = ViewPagerAdapter(this)

        binding.viewPager.run {
            adapter = pagerAdapter
            offscreenPageLimit = 1
        }

        val list = ArrayList<Int>()
        var to = currentIndex + step
        for (i in currentIndex..to) {
            list.add(i)
        }
        currentIndex = to+1
        pagerAdapter?.setData(list)


        binding.addData.setOnClickListener {
            val list = ArrayList<Int>()
            var to = currentIndex + step
            for (i in currentIndex..to) {
                list.add(i)
            }
            currentIndex = to+1

            pagerAdapter?.addData(list)
        }
    }

}