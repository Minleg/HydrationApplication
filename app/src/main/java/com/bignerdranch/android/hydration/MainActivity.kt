package com.bignerdranch.android.hydration

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var waterViewPager: ViewPager2
    private lateinit var waterTabLayout: TabLayout

    private val waterViewModel: WaterViewModel by lazy {
        val app = application as HydrationApplication
        WaterViewModelFactory(app.waterRepository, app.daysRepository).create(
            WaterViewModel::class.java,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        waterViewPager = findViewById(R.id.water_view_pager)
        waterTabLayout = findViewById(R.id.water_days_tab_layout)

        waterViewModel.allRecords.observe(this) { records ->
            Log.d("MAIN_ACTIVITY", "$records")
        }

        // val days = resources.getStringArray(R.array.days)
        val days = waterViewModel.getWeekdays()

        val pagerAdapter = WaterViewPagerAdapter(this, days)
        waterViewPager.adapter = pagerAdapter

        TabLayoutMediator(waterTabLayout, waterViewPager) { tab, position ->
            tab.text = days[position]
        }.attach()

        val todayIndex = waterViewModel.getTodayIndex()
        waterViewPager.setCurrentItem(todayIndex, false)

//        val tuesday = WaterRecord("Tuesday", 4)
//        waterViewModel.insertNewRecord(tuesday)
//
//        val wednesday = WaterRecord("Wednesday", 2)
//        waterViewModel.insertNewRecord(wednesday)

//        supportFragmentManager.beginTransaction()
//            .add(R.id.content, HydrationFragment.newInstance("Monday"))
//            .commit()
    }
}
